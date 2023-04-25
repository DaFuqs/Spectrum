package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public class HerdingStaffItem extends Item implements InkPowered {
	
	public static final InkColor USED_COLOR = InkColors.LIGHT_GRAY;
	public static InkCost LURE_COST = new InkCost(USED_COLOR, 5);
	public static InkCost TURN_TO_MEMORY_COST = new InkCost(USED_COLOR, 1000);
	
	public HerdingStaffItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		super.usageTick(world, user, stack, remainingUseTicks);
		if (user instanceof PlayerEntity player && !InkPowered.tryDrainEnergy(player, LURE_COST)) {
			user.stopUsingItem();
		}
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		super.appendTooltip(itemStack, world, tooltip, tooltipContext);
		
		tooltip.add(Text.translatable("item.spectrum.herding_staff.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("item.spectrum.herding_staff.tooltip2").formatted(Formatting.GRAY));
		addInkPoweredTooltip(tooltip);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (InkPowered.tryDrainEnergy(user, LURE_COST)) {
			if (world.isClient) {
				startSoundInstance(user);
			}
			return ItemUsage.consumeHeldItem(world, user, hand);
		}
		return super.use(world, user, hand);
	}
	
	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		World world = user.world;
		Vec3d pos = entity.getPos();
		if (!world.isClient) {
			if (turnEntityToMemory(user, entity)) {
				SpectrumS2CPacketSender.playParticleWithPatternAndVelocity(null, (ServerWorld) world, entity.getPos(), SpectrumParticleTypes.LIGHT_GRAY_SPARKLE_RISING, VectorPattern.EIGHT, 0.2F);
				SpectrumS2CPacketSender.playParticleWithExactVelocity((ServerWorld) world, entity.getPos(), SpectrumParticleTypes.LIGHT_GRAY_EXPLOSION, 1, Vec3d.ZERO);
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SpectrumSoundEvents.RADIANCE_STAFF_PLACE, SoundCategory.PLAYERS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
			} else {
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
			}
		}
		return ActionResult.success(world.isClient);
	}
	
	private boolean turnEntityToMemory(PlayerEntity user, LivingEntity entity) {
		if (!entity.isAlive() || entity.isRemoved() || entity.hasPassengers()) {
			return false;
		}
		if (entity.getType().isIn(SpectrumEntityTypeTags.HERDING_STAFF_BLACKLISTED)) {
			return false;
		}
		SpawnGroup spawnGroup = entity.getType().getSpawnGroup();
		if (spawnGroup == SpawnGroup.MISC || spawnGroup == SpawnGroup.MONSTER) {
			return false;
		}
		if (!InkPowered.tryDrainEnergy(user, TURN_TO_MEMORY_COST)) {
			return false;
		}
		
		ItemStack memoryStack = MemoryItem.getMemoryForEntity(entity);
		MemoryItem.setTicksToManifest(memoryStack, 1);
		
		Vec3d entityPos = entity.getPos();
		ItemEntity itemEntity = new ItemEntity(entity.world, entityPos.getX(), entityPos.getY(), entityPos.getZ(), memoryStack);
		itemEntity.setVelocity(new Vec3d(0.0, 0.2, 0.0));
		entity.world.spawnEntity(itemEntity);
		entity.remove(Entity.RemovalReason.DISCARDED);
		
		return true;
	}
	
	@Environment(EnvType.CLIENT)
	public void startSoundInstance(PlayerEntity user) {
		MinecraftClient.getInstance().getSoundManager().play(new NaturesStaffUseSoundInstance(user));
	}
	
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}
	
	@Override
	public List<InkColor> getUsedColors() {
		return List.of(USED_COLOR);
	}
	
}
