package de.dafuqs.spectrum.items.magic_items;

import de.dafuqs.revelationary.api.advancements.*;
import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.compat.claims.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;

import java.util.*;

public class StaffOfRemembranceItem extends Item implements InkPowered, PrioritizedEntityInteraction {
	
	public static final InkColor USED_COLOR = InkColors.LIGHT_GRAY;
	public static final InkAmount TURN_NEUTRAL_TO_MEMORY_COST = new InkAmount(USED_COLOR, 1000);
	public static final InkAmount TURN_HOSTILE_TO_MEMORY_COST = new InkAmount(USED_COLOR, 5000);
	public static final InkAmount CREATE_PLAYER_MEMORY_COST = new InkAmount(USED_COLOR, 5000);
	
	public StaffOfRemembranceItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		
		tooltip.add(Component.translatable("item.spectrum.staff_of_remembrance.tooltip").withStyle(ChatFormatting.GRAY));
		addInkPoweredTooltip(tooltip);
	}
	
	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
		Level world = user.level();
		Vec3 pos = entity.position();
		
		if (!GenericClaimModsCompat.canInteract(world, entity, user)) {
			return InteractionResult.FAIL;
		}
		
		if (!world.isClientSide()) {
			if (turnEntityToMemory(user, entity)) {
				PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) world, entity.position(), ColoredSparkleRisingParticleEffect.LIGHT_GRAY, 10, Vec3.ZERO, new Vec3(0.2, 0.2, 0.2));
				PlayParticleWithExactVelocityPayload.playParticleWithExactVelocity((ServerLevel) world, entity.position(), ColoredExplosionParticleEffect.LIGHT_GRAY, 1, Vec3.ZERO);
				world.playSound(null, pos.x(), pos.y(), pos.z(), SpectrumSoundEvents.RADIANCE_STAFF_PLACE, SoundSource.PLAYERS, 1.0F, 0.8F + world.getRandom().nextFloat() * 0.4F);
			} else {
				world.playSound(null, pos.x(), pos.y(), pos.z(), SpectrumSoundEvents.USE_FAIL, SoundSource.PLAYERS, 1.0F, 0.8F + world.getRandom().nextFloat() * 0.4F);
			}
		}
		
		return InteractionResult.sidedSuccess(world.isClientSide());
	}
	
	private boolean turnEntityToMemory(Player user, LivingEntity entity) {
		if (!entity.isAlive() || entity.isRemoved() || entity.isVehicle()) {
			return false;
		}
		
		if (entity instanceof ServerPlayer player) {
			if (!InkPowered.tryDrainEnergy(user, CREATE_PLAYER_MEMORY_COST)) {
				return false;
			}
			
			entity.hurt(SpectrumDamageTypes.remembrance(user.level(), user), 4);
			
			ItemStack memoryStack = MemoryItem.getForPlayer(player, 4);
			MemoryItem.setTicksToManifest(memoryStack, 1);
			
			Vec3 entityPos = entity.position();
			ItemEntity itemEntity = new ItemEntity(entity.level(), entityPos.x(), entityPos.y(), entityPos.z(), memoryStack);
			itemEntity.setDeltaMovement(new Vec3(0.0, 0.15, 0.0));
			entity.level().addFreshEntity(itemEntity);
			
			return true;
		}
		
		if (!(entity instanceof Mob mob)) {
			return false;
		}
		
		EntityType<?> entityType = mob.getType();
		if (entityType.is(SpectrumEntityTypeTags.STAFF_OF_REMEMBRANCE_BLACKLISTED)) {
			return false;
		}
		
		MobCategory spawnGroup = entityType.getCategory();
		if (spawnGroup == MobCategory.MONSTER) {
			if (!user.isCreative() && !AdvancementHelper.hasAdvancement(user, SpectrumAdvancements.HOSTILE_MEMORIZING)) {
				return false;
			}
			if (!InkPowered.tryDrainEnergy(user, TURN_HOSTILE_TO_MEMORY_COST)) {
				return false;
			}
		} else {
			if (!InkPowered.tryDrainEnergy(user, TURN_NEUTRAL_TO_MEMORY_COST)) {
				return false;
			}
		}
		
		mob.dropLeash(true, true);
		mob.playAmbientSound();
		mob.spawnAnim();
		
		ItemStack memoryStack = MemoryItem.getMemoryForEntity(entity);
		MemoryItem.setTicksToManifest(memoryStack, 1);
		MemoryItem.setSpawnAsAdult(memoryStack, true);
		
		Vec3 entityPos = mob.position();
		ItemEntity itemEntity = new ItemEntity(mob.level(), entityPos.x(), entityPos.y(), entityPos.z(), memoryStack);
		itemEntity.setDeltaMovement(new Vec3(0.0, 0.15, 0.0));
		mob.level().addFreshEntity(itemEntity);
		mob.remove(Entity.RemovalReason.DISCARDED);
		
		return true;
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.SPEAR;
	}
	
	@Override
	public List<InkColor> getUsedColors() {
		return List.of(USED_COLOR);
	}
	
}
