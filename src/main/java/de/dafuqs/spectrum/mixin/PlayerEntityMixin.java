package de.dafuqs.spectrum.mixin;

import com.google.common.collect.*;
import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.attachment_types.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccessor {
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}
	
	@Shadow
	public abstract @NotNull Iterable<ItemStack> getHandSlots();
	
	@Shadow
	private int sleepCounter;
	
	@Shadow
	public abstract boolean hurt(@NotNull DamageSource source, float amount);
	
	@Shadow
	protected abstract boolean canPlayerFitWithinBlocksAndEntitiesWhen(Pose pose);
	
	@Unique
	public SpectrumFishingBobberEntity spectrum$fishingBobber;
	
	
	@ModifyVariable(method = "attack", name = "entityReachSq", at = @At(value = "STORE"))
	protected double spectrum$increaseSweepMaxDistance(double original) {
		var stack = this.getItemInHand(InteractionHand.MAIN_HAND);
		if (stack.getItem() == SpectrumItems.DRACONIC_TWINSWORD.get()) {
			int channeling = SpectrumEnchantmentHelper.getLevel(level().registryAccess(), Enchantments.CHANNELING, stack);
			return original * 3 * ((channeling + 1) * 1.5);
		}
		return original;
	}
	
	@WrapOperation(method = "getDigSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;)F"))
	private float spectrum$modifygetBlockBreakingSpeed(Inventory inventory, BlockState state, Operation<Float> original) {
		ItemStack stack = inventory.items.get(inventory.selected);
		RegistryAccess drm = registryAccess();
		Tool tool = stack.get(DataComponents.TOOL);
		float speed = original.call(inventory, state);
		
		// RAZING GAMING
		int razingLevel = SpectrumEnchantmentHelper.getLevel(drm, SpectrumEnchantmentKeys.RAZING, stack);
		if (razingLevel > 0 && tool != null && tool.getMiningSpeed(state) > tool.defaultMiningSpeed()) {
			float hardness = state.getBlock().defaultDestroyTime();
			speed = (float) Math.max(1 + hardness, Math.pow(2, 1 + razingLevel / 8F));
		}
		
		// INERTIA GAMING
		// inertia mining speed calculation logic is capped at 5 levels.
		// Higher and the formula would do weird stuff
		int inertiaLevel = SpectrumEnchantmentHelper.getLevel(drm, SpectrumEnchantmentKeys.INERTIA, stack);
		inertiaLevel = Math.min(4, inertiaLevel);
		if (inertiaLevel > 0) {
			var inertia = stack.getOrDefault(SpectrumDataComponentTypes.INERTIA, InertiaComponent.DEFAULT);
			if (state.is(inertia.lastMined())) {
				var additionalSpeedPercent = 2.0 * Math.log(inertia.count()) / Math.log((6 - inertiaLevel) * (6 - inertiaLevel) + 1);
				speed *= 0.5F + (float) additionalSpeedPercent;
			} else {
				speed /= 4;
			}
		}
		
		return speed;
	}
	
	@Inject(method = "updateSwimming()V", at = @At("HEAD"), cancellable = true)
	public void spectrum$updateSwimming(CallbackInfo ci) {
		if (SpectrumCurioItem.hasEquipped(this, SpectrumItems.RING_OF_DENSER_STEPS.get())) {
			this.setSwimming(false);
			ci.cancel();
		}
	}
	//TODO: move to event
	@Inject(method = "attack", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/player/Player.getAttributeValue (Lnet/minecraft/core/Holder;)D"))
	protected void spectrum$calculateModifiers(Entity target, CallbackInfo ci) {
		Player player = (Player) (Object) this;
		
		Multimap<Holder<Attribute>, AttributeModifier> map = Multimaps.newMultimap(Maps.newLinkedHashMap(), ArrayList::new);
		
		AttributeModifier jeopardantModifier;
		if (SpectrumCurioItem.hasEquipped(player, SpectrumItems.JEOPARDANT.get())) {
			jeopardantModifier = new AttributeModifier(AttackRingItem.ATTACK_RING_DAMAGE_ID, AttackRingItem.getAttackModifierForEntity(player), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		} else {
			jeopardantModifier = new AttributeModifier(AttackRingItem.ATTACK_RING_DAMAGE_ID, 0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		}
		map.put(Attributes.ATTACK_DAMAGE, jeopardantModifier);
		
		player.getAttributes().addTransientAttributeModifiers(map);
	}
	
	@ModifyExpressionValue(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"))
	protected List<LivingEntity> spectrum$increaseSweepRadius(List<LivingEntity> original, Entity target) {
		var stack = this.getItemInHand(InteractionHand.MAIN_HAND);
		if (stack.getItem() == SpectrumItems.DRACONIC_TWINSWORD.get()) {
			var channeling = spectrum$getChanneling(stack) + 1;
			var size = channeling * 2 + 0.5;
			var entities = this.level().getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(size, 0.4 * channeling, size));
			if (!level().isClientSide() && (channeling - 1) > 0) {
				for (LivingEntity living : entities) {
					if (living.canBeSeenAsEnemy()) {
						for (int i = 0; i < 5; i++) {
							((ServerLevel) level()).sendParticles(ParticleTypes.ENCHANTED_HIT,
									living.getRandomX(1.25),
									living.getY() + living.getBbHeight() * random.nextFloat(),
									living.getRandomZ(1.25),
									random.nextInt(2), 0, random.nextFloat() / 6F, 0, 0);
						}
					}
				}
			}
			
			return entities;
		}
		return original;
	}
	
	@WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "net/minecraft/world/level/Level.playSound (Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V", ordinal = 2))
	protected void spectrum$switchCritSound(Level instance, Player except, double x, double y, double z, SoundEvent sound, SoundSource category, float volume, float pitch, Operation<Void> original) {
		var player = (Player) (Object) this;
		var stack = this.getItemInHand(InteractionHand.MAIN_HAND);
		var component = MiscPlayerDataAttachmentType.get(player);
		if (stack.getItem() instanceof LightGreatswordItem && component.isLunging()) {
			original.call(instance, except, x, y, z, SpectrumSoundEvents.LUNGE_CRIT, category, 1F, 1F + random.nextFloat() * 0.2F);
			return;
		}
		original.call(instance, except, x, y, z, sound, category, volume, pitch);
	}
	
	@WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "net/minecraft/world/level/Level.playSound (Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V", ordinal = 1))
	protected void spectrum$switchSweepSound(Level instance, Player except, double x, double y, double z, SoundEvent sound, SoundSource category, float volume, float pitch, Operation<Void> original) {
		var stack = this.getItemInHand(InteractionHand.MAIN_HAND);
		if (stack.getItem() == SpectrumItems.DRACONIC_TWINSWORD.get() && spectrum$getChanneling(stack) > 0) {
			this.level().playSound(except, x, y, z, SpectrumSoundEvents.ELECTRIC_DISCHARGE, category, 0.75F, 0.9F + random.nextFloat() * 0.2F);
			return;
		}
		original.call(instance, except, x, y, z, sound, category, volume, pitch);
	}
	
	@Unique
	protected int spectrum$getChanneling(ItemStack stack) {
		return SpectrumEnchantmentHelper.getLevel(level().registryAccess(), Enchantments.CHANNELING, stack);
	}
	
	@Override
	public void spectrum$setSpectrumBobber(SpectrumFishingBobberEntity bobber) {
		this.spectrum$fishingBobber = bobber;
	}
	
	@Override
	public SpectrumFishingBobberEntity spectrum$getSpectrumBobber() {
		return this.spectrum$fishingBobber;
	}
	
	@Override
	public void spectrum$setSleepTimer(int ticks) {
		this.sleepCounter = ticks;
	}
	
	// If the player holds an ExperienceStorageItem in their hands
	// experience is tried to get put in there first
	@ModifyVariable(at = @At("HEAD"), method = "giveExperiencePoints", argsOnly = true)
	public int addExperience(int experience) {
		if (experience < 0) { // draining XP, like Botanias Rosa Arcana
			return experience;
		}
		
		// if the player has a ExperienceStorageItem in hand add the XP to that
		Player player = (Player) (Object) this;
		for (ItemStack stack : getHandSlots()) {
			if (!player.isUsingItem() && stack.getItem() instanceof ExperienceStorageItem) {
				experience = ExperienceStorageItem.addStoredExperience(level().registryAccess(), stack, experience);
				player.takeXpDelay = 0;
				if (experience == 0) {
					break;
				}
			}
		}
		return experience;
	}
	
	@WrapOperation(method = "updatePlayerPose", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setPose(Lnet/minecraft/world/entity/Pose;)V"))
	public void spectrum$forceSwimmingState(Player instance, Pose entityPose, Operation<Void> original) {
		var component = MiscPlayerDataAttachmentType.get(instance);
		if ((component.shouldLieDown() || instance.hasEffect(SpectrumMobEffects.FATAL_SLUMBER)) && canPlayerFitWithinBlocksAndEntitiesWhen(Pose.SWIMMING)) {
			instance.setPose(Pose.SWIMMING);
			return;
		}
		original.call(instance, entityPose);
	}
	
}
