package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.interfaces.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.dimension.v1.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.client.*;
import net.minecraft.client.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.stat.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class EnderSpliceItem extends Item implements ExtendedEnchantable {
	
	public EnderSpliceItem(Settings settings) {
		super(settings);
	}
	
	public static boolean isSameWorld(World world1, World world2) {
		return world1.getRegistryKey().getValue().toString().equals(world2.getRegistryKey().getValue().toString());
	}
	
	public static void setTeleportTargetPos(@NotNull ItemStack itemStack, World world, Vec3d pos) {
		NbtCompound nbtCompound = itemStack.getOrCreateNbt();
		
		// Remove player tags, if present
		if (nbtCompound.contains("TargetPlayerName")) {
			nbtCompound.remove("TargetPlayerName");
		}
		if (nbtCompound.contains("TargetPlayerUUID")) {
			nbtCompound.remove("TargetPlayerUUID");
		}
		
		// Add pos
		nbtCompound.putDouble("PosX", pos.getX());
		nbtCompound.putDouble("PosY", pos.getY());
		nbtCompound.putDouble("PosZ", pos.getZ());
		nbtCompound.putString("Dimension", world.getRegistryKey().getValue().toString());
		itemStack.setNbt(nbtCompound);
	}
	
	public static void setTeleportTargetPlayer(@NotNull ItemStack itemStack, ServerPlayerEntity player) {
		NbtCompound nbtCompound = itemStack.getOrCreateNbt();
		
		// Override target pos, if present
		if (nbtCompound.contains("PosX")) {
			nbtCompound.remove("PosX");
		}
		if (nbtCompound.contains("PosY")) {
			nbtCompound.remove("PosY");
		}
		if (nbtCompound.contains("PosZ")) {
			nbtCompound.remove("PosZ");
		}
		if (nbtCompound.contains("Dimension")) {
			nbtCompound.remove("Dimension");
		}
		
		// Add player
		nbtCompound.putString("TargetPlayerName", player.getName().getString());
		nbtCompound.putUuid("TargetPlayerUUID", player.getUuid());
		itemStack.setNbt(nbtCompound);
	}
	
	public static boolean hasTeleportTarget(ItemStack itemStack) {
		NbtCompound nbtCompound = itemStack.getNbt();
		if (nbtCompound == null) {
			return false;
		}
		
		return nbtCompound.contains("PosX") || nbtCompound.contains("TargetPlayerName");
	}
	
	public static void clearTeleportTarget(ItemStack itemStack) {
		NbtCompound nbtCompound = itemStack.getOrCreateNbt();
		
		if (nbtCompound.contains("PosX")) {
			nbtCompound.remove("PosX");
		}
		if (nbtCompound.contains("PosY")) {
			nbtCompound.remove("PosY");
		}
		if (nbtCompound.contains("PosZ")) {
			nbtCompound.remove("PosZ");
		}
		if (nbtCompound.contains("Dimension")) {
			nbtCompound.remove("Dimension");
		}
		if (nbtCompound.contains("TargetPlayerName")) {
			nbtCompound.remove("TargetPlayerName");
		}
		if (nbtCompound.contains("TargetPlayerUUID")) {
			nbtCompound.remove("TargetPlayerUUID");
		}
		
		itemStack.setNbt(nbtCompound);
	}
	
	@Override
	public ItemStack finishUsing(ItemStack itemStack, World world, LivingEntity user) {
		if (world.isClient) {
			if (getTeleportTargetPos(itemStack).isEmpty() && getTeleportTargetPlayerUUID(itemStack).isEmpty()) {
				interactWithEntityClient();
			}
		} else {
			ServerPlayerEntity playerEntity = user instanceof ServerPlayerEntity ? (ServerPlayerEntity) user : null;
			Criteria.CONSUME_ITEM.trigger(playerEntity, itemStack);
			
			boolean resonance = EnchantmentHelper.getLevel(SpectrumEnchantments.RESONANCE, itemStack) > 0;
			
			// If Dimension & Pos stored => Teleport to that position
			Optional<Pair<String, Vec3d>> teleportTargetPos = getTeleportTargetPos(itemStack);
			if (teleportTargetPos.isPresent()) {
				RegistryKey<World> targetWorldKey = RegistryKey.of(Registry.WORLD_KEY, new Identifier(teleportTargetPos.get().getLeft()));
				World targetWorld = world.getServer().getWorld(targetWorldKey);
				if (teleportPlayerToPos(world, user, playerEntity, targetWorld, teleportTargetPos.get().getRight(), resonance)) {
					decrementWithChance(itemStack, world, playerEntity);
				}
			} else {
				// If UUID stored => Teleport to player, if online
				Optional<UUID> teleportTargetPlayerUUID = getTeleportTargetPlayerUUID(itemStack);
				if (teleportTargetPlayerUUID.isPresent()) {
					if (teleportPlayerToPlayerWithUUID(world, user, playerEntity, teleportTargetPlayerUUID.get(), resonance)) {
						decrementWithChance(itemStack, world, playerEntity);
					}
				} else {
					// Nothing stored => Store current position
					setTeleportTargetPos(itemStack, playerEntity.getEntityWorld(), playerEntity.getPos());
					world.playSound(null, playerEntity.getBlockPos(), SpectrumSoundEvents.ENDER_SPLICE_BOUND, SoundCategory.PLAYERS, 1.0F, 1.0F);
				}
			}
			playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
		}
		
		return itemStack;
	}
	
	private static void decrementWithChance(ItemStack itemStack, World world, ServerPlayerEntity playerEntity) {
		if (EnchantmentHelper.getLevel(SpectrumEnchantments.INDESTRUCTIBLE, itemStack) > 0) {
			return;
		}
		if (!playerEntity.getAbilities().creativeMode) {
			int unbreakingLevel = EnchantmentHelper.getLevel(Enchantments.UNBREAKING, itemStack);
			if (unbreakingLevel == 0) {
				itemStack.decrement(1);
			} else {
				itemStack.decrement(Support.getIntFromDecimalWithChance(1.0 / (1 + unbreakingLevel), world.random));
			}
		}
	}
	
	@Environment(EnvType.CLIENT)
	public void interactWithEntityClient() {
		// If aiming at an entity: trigger entity interaction
		HitResult hitResult = MinecraftClient.getInstance().crosshairTarget;
		if (hitResult.getType() == HitResult.Type.ENTITY) {
			EntityHitResult entityHitResult = (EntityHitResult) hitResult;
			if (entityHitResult.getEntity() instanceof PlayerEntity playerEntity) {
				SpectrumC2SPacketSender.sendBindEnderSpliceToPlayer(playerEntity);
			}
		}
	}
	
	private boolean teleportPlayerToPlayerWithUUID(World world, LivingEntity user, PlayerEntity playerEntity, UUID targetPlayerUUID, boolean hasResonance) {
		PlayerEntity targetPlayer = PlayerOwned.getPlayerEntityIfOnline(targetPlayerUUID);
		if (targetPlayer != null) {
			return teleportPlayerToPos(targetPlayer.getEntityWorld(), user, playerEntity, targetPlayer.getEntityWorld(), targetPlayer.getPos(), hasResonance);
		}
		return false;
	}
	
	private boolean teleportPlayerToPos(World world, LivingEntity user, PlayerEntity playerEntity, World targetWorld, Vec3d targetPos, boolean hasResonance) {
		boolean isSameWorld = isSameWorld(user.getEntityWorld(), targetWorld);
		Vec3d currentPos = playerEntity.getPos();
		if (hasResonance || isSameWorld) {
			world.playSound(playerEntity, currentPos.getX(), currentPos.getY(), currentPos.getZ(), SpectrumSoundEvents.PLAYER_TELEPORTS, SoundCategory.PLAYERS, 1.0F, 1.0F);
			
			if (!isSameWorld) {
				FabricDimensions.teleport(user, (ServerWorld) targetWorld, new TeleportTarget(targetPos.add(0, 0.25, 0), new Vec3d(0, 0, 0), user.getYaw(), user.getPitch()));
			} else {
				user.requestTeleport(targetPos.getX(), targetPos.y + 0.25, targetPos.z); // +0.25 makes it look way more lively
			}
			world.playSound(playerEntity, targetPos.getX(), targetPos.y, targetPos.z, SpectrumSoundEvents.PLAYER_TELEPORTS, SoundCategory.PLAYERS, 1.0F, 1.0F);
			
			// make sure the sound plays even when the player currently teleports
			if (playerEntity instanceof ServerPlayerEntity) {
				world.playSound(null, playerEntity.getBlockPos(), SpectrumSoundEvents.PLAYER_TELEPORTS, SoundCategory.PLAYERS, 1.0F, 1.0F);
				world.playSound(null, playerEntity.getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
			return true;
		} else {
			user.stopUsingItem();
			world.playSound(null, currentPos.getX(), currentPos.getY(), currentPos.getZ(), SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 1.0F, 1.0F);
			return false;
		}
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (world.isClient) {
			startSoundInstance(user);
		}
		return ItemUsage.consumeHeldItem(world, user, hand);
	}
	
	@Environment(EnvType.CLIENT)
	public void startSoundInstance(PlayerEntity user) {
		MinecraftClient.getInstance().getSoundManager().play(new EnderSpliceChargingSoundInstance(user));
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 48;
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		// If Dimension & Pos stored => Teleport to that position
		Optional<Pair<String, Vec3d>> teleportTargetPos = getTeleportTargetPos(itemStack);
		if (teleportTargetPos.isPresent()) {
			String dimensionDisplayString = Support.getReadableDimensionString(teleportTargetPos.get().getLeft());
			Vec3d pos = teleportTargetPos.get().getRight();
			tooltip.add(Text.translatable("item.spectrum.ender_splice.tooltip.bound_pos", (int) pos.x, (int) pos.y, (int) pos.z, dimensionDisplayString));
			return;
		} else {
			// If UUID stored => Teleport to player, if online
			Optional<UUID> teleportTargetPlayerUUID = getTeleportTargetPlayerUUID(itemStack);
			if (teleportTargetPlayerUUID.isPresent()) {
				Optional<String> teleportTargetPlayerName = getTeleportTargetPlayerName(itemStack);
				if (teleportTargetPlayerName.isPresent()) {
					tooltip.add(Text.translatable("item.spectrum.ender_splice.tooltip.bound_player", teleportTargetPlayerName.get()));
				} else {
					tooltip.add(Text.translatable("item.spectrum.ender_splice.tooltip.bound_player", "???"));
				}
				return;
			}
		}
		
		tooltip.add(Text.translatable("item.spectrum.ender_splice.tooltip.unbound"));
	}
	
	public Optional<Pair<String, Vec3d>> getTeleportTargetPos(@NotNull ItemStack itemStack) {
		NbtCompound nbtCompound = itemStack.getNbt();
		if (nbtCompound != null && nbtCompound.contains("PosX") && nbtCompound.contains("PosY") && nbtCompound.contains("PosZ") && nbtCompound.contains("Dimension")) {
			String dimensionKeyString = nbtCompound.getString("Dimension");
			double x = nbtCompound.getDouble("PosX");
			double y = nbtCompound.getDouble("PosY");
			double z = nbtCompound.getDouble("PosZ");
			Vec3d pos = new Vec3d(x, y, z);
			
			return Optional.of(new Pair<>(dimensionKeyString, pos));
		}
		return Optional.empty();
	}
	
	public Optional<UUID> getTeleportTargetPlayerUUID(@NotNull ItemStack itemStack) {
		NbtCompound nbtCompound = itemStack.getNbt();
		if (nbtCompound != null && nbtCompound.contains("TargetPlayerUUID")) {
			return Optional.of(nbtCompound.getUuid("TargetPlayerUUID"));
		}
		return Optional.empty();
	}
	
	public Optional<String> getTeleportTargetPlayerName(@NotNull ItemStack itemStack) {
		NbtCompound nbtCompound = itemStack.getNbt();
		if (nbtCompound != null && nbtCompound.contains("TargetPlayerName")) {
			return Optional.of(nbtCompound.getString("TargetPlayerName"));
		}
		return Optional.empty();
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return stack.getCount() == 1;
	}
	
	@Override
	public Set<Enchantment> getAcceptedEnchantments() {
		return Set.of(SpectrumEnchantments.RESONANCE, SpectrumEnchantments.INDESTRUCTIBLE, Enchantments.UNBREAKING);
	}
	
	@Override
	public int getEnchantability() {
		return 50;
	}
	
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		super.appendStacks(group, stacks);
		if (this.isIn(group)) {
			stacks.add(SpectrumEnchantmentHelper.getMaxEnchantedStack(this));
		}
	}
	
}
