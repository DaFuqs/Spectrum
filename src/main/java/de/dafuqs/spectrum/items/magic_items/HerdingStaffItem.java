package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.energy.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.sound.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.client.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.*;
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
	public static final InkCost LURE_COST = new InkCost(USED_COLOR, 5);
	public static final InkCost TURN_NEUTRAL_TO_MEMORY_COST = new InkCost(USED_COLOR, 1000);
	public static final InkCost TURN_HOSTILE_TO_MEMORY_COST = new InkCost(USED_COLOR, 10000);
	
	public static final Identifier UNLOCK_HOSTILE_MEMORIZING_ID = SpectrumCommon.locate("milestones/unlock_hostile_memorizing");
	
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
		
		if (!GenericClaimModsCompat.canInteractWith(world, entity, user)) {
			return ActionResult.FAIL;
		}
		
		if (!world.isClient && entity instanceof MobEntity mobEntity) {
			if (turnEntityToMemory(user, mobEntity)) {
				SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world, entity.getPos(), SpectrumParticleTypes.LIGHT_GRAY_SPARKLE_RISING, 10, Vec3d.ZERO, new Vec3d(0.2, 0.2, 0.2));
				SpectrumS2CPacketSender.playParticleWithExactVelocity((ServerWorld) world, entity.getPos(), SpectrumParticleTypes.LIGHT_GRAY_EXPLOSION, 1, Vec3d.ZERO);
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SpectrumSoundEvents.RADIANCE_STAFF_PLACE, SoundCategory.PLAYERS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
			} else {
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
			}
		}
		return ActionResult.success(world.isClient);
	}
	
	private boolean turnEntityToMemory(PlayerEntity user, MobEntity entity) {
		if (!entity.isAlive() || entity.isRemoved() || entity.hasPassengers()) {
			return false;
		}
		if (entity.getType().isIn(ConventionalEntityTypeTags.BOSSES) || entity.getType().isIn(SpectrumEntityTypeTags.HERDING_STAFF_BLACKLISTED)) {
			return false;
		}
		SpawnGroup spawnGroup = entity.getType().getSpawnGroup();
		if (spawnGroup == SpawnGroup.MISC) {
			return false;
		}
		if (spawnGroup == SpawnGroup.MONSTER && (user.isCreative() || AdvancementHelper.hasAdvancement(user, UNLOCK_HOSTILE_MEMORIZING_ID))) {
			if (!InkPowered.tryDrainEnergy(user, TURN_HOSTILE_TO_MEMORY_COST)) {
				return false;
			}
		} else if (!InkPowered.tryDrainEnergy(user, TURN_NEUTRAL_TO_MEMORY_COST)) {
			return false;
		}
		
		entity.playAmbientSound();
		entity.playSpawnEffects();
		
		ItemStack memoryStack = MemoryItem.getMemoryForEntity(entity);
		MemoryItem.setTicksToManifest(memoryStack, 1);
		MemoryItem.setSpawnAsAdult(memoryStack, true);
		
		Vec3d entityPos = entity.getPos();
		ItemEntity itemEntity = new ItemEntity(entity.world, entityPos.getX(), entityPos.getY(), entityPos.getZ(), memoryStack);
		itemEntity.setVelocity(new Vec3d(0.0, 0.15, 0.0));
		entity.world.spawnEntity(itemEntity);
		entity.remove(Entity.RemovalReason.DISCARDED);
		
		return true;
	}
	
	@Environment(EnvType.CLIENT)
	public void startSoundInstance(PlayerEntity user) {
		MinecraftClient.getInstance().getSoundManager().play(new HerdingStaffUseSoundInstance(user));
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.SPEAR;
	}
	
	@Override
	public List<InkColor> getUsedColors() {
		return List.of(USED_COLOR);
	}
	
}
