package de.dafuqs.spectrum.mixin;

import com.google.common.collect.*;
import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.*;
import com.llamalad7.mixinextras.sugar.ref.*;
import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.enchantments.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccessor {
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	public abstract Iterable<ItemStack> getHandItems();

	@Shadow private int sleepTimer;

	@Shadow public abstract boolean damage(DamageSource source, float amount);

	public SpectrumFishingBobberEntity spectrum$fishingBobber;
	
	@Inject(method = "updateSwimming()V", at = @At("HEAD"), cancellable = true)
	public void spectrum$updateSwimming(CallbackInfo ci) {
		if (SpectrumTrinketItem.hasEquipped(this, SpectrumItems.RING_OF_DENSER_STEPS)) {
			this.setSwimming(false);
			ci.cancel();
		}
	}
	
	@Inject(method = "onKilledOther", at = @At("HEAD"))
	private void spectrum$rememberKillOther(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir) {
		PlayerEntity entity = (PlayerEntity) (Object) this;
		LastKillComponent.rememberKillTick(entity, entity.getWorld().getTime());
		
		StatusEffectInstance frenzy = entity.getStatusEffect(SpectrumStatusEffects.FRENZY);
		if (frenzy != null) {
			((FrenzyStatusEffect) frenzy.getEffectType()).onKill(entity, frenzy.getAmplifier());
		}
	}

	@Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
	private void spectrum$stopSleep(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (amount > 0) {
			PlayerEntity entity = (PlayerEntity) (Object) this;
			MiscPlayerDataComponent.get(entity).notifyHit();
		}
	}

	@Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
	protected void spectrum$calculateModifiers(Entity target, CallbackInfo ci) {
		PlayerEntity player = (PlayerEntity) (Object) this;
		
		Multimap<EntityAttribute, EntityAttributeModifier> map = Multimaps.newMultimap(Maps.newLinkedHashMap(), ArrayList::new);
		
		EntityAttributeModifier jeopardantModifier;
		if (SpectrumTrinketItem.hasEquipped(player, SpectrumItems.JEOPARDANT)) {
			jeopardantModifier = new EntityAttributeModifier(AttackRingItem.ATTACK_RING_DAMAGE_UUID, AttackRingItem.ATTACK_RING_DAMAGE_NAME, AttackRingItem.getAttackModifierForEntity(player), EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
		} else {
			jeopardantModifier = new EntityAttributeModifier(AttackRingItem.ATTACK_RING_DAMAGE_UUID, AttackRingItem.ATTACK_RING_DAMAGE_NAME, 0, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
		}
		map.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, jeopardantModifier);
		
		if (SpectrumEnchantments.IMPROVED_CRITICAL.canEntityUse(player)) {
			int improvedCriticalLevel = SpectrumEnchantmentHelper.getUsableLevel(SpectrumEnchantments.IMPROVED_CRITICAL, player.getMainHandStack(), player);
			EntityAttributeModifier improvedCriticalModifier = new EntityAttributeModifier(ImprovedCriticalEnchantment.EXTRA_CRIT_DAMAGE_MULTIPLIER_ATTRIBUTE_UUID, ImprovedCriticalEnchantment.EXTRA_CRIT_DAMAGE_MULTIPLIER_ATTRIBUTE_NAME, ImprovedCriticalEnchantment.getAddtionalCritDamageMultiplier(improvedCriticalLevel), EntityAttributeModifier.Operation.ADDITION);
			map.put(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, improvedCriticalModifier);
		}
		
		player.getAttributes().addTemporaryModifiers(map);
	}
	
	@ModifyExpressionValue(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getNonSpectatingEntities(Ljava/lang/Class;Lnet/minecraft/util/math/Box;)Ljava/util/List;"))
	protected List<LivingEntity> spectrum$increaseSweepRadius(List<LivingEntity> original, Entity target) {
		var stack = this.getStackInHand(Hand.MAIN_HAND);
		if (stack.getItem() == SpectrumItems.DRACONIC_TWINSWORD) {
			var channeling = getChanneling(stack) + 1;
			var size = channeling * 2 + 0.5;
			var entities = this.getWorld().getNonSpectatingEntities(LivingEntity.class, target.getBoundingBox().expand(size, 0.4 * channeling, size));
			if (!getWorld().isClient() && (channeling - 1) > 0) {
				for (LivingEntity living : entities) {
					if (living.canTakeDamage()) {
						for (int i = 0; i < 5; i++) {
							((ServerWorld) getWorld()).spawnParticles(ParticleTypes.ENCHANTED_HIT,
									living.getParticleX(1.25),
									living.getY() + living.getHeight() * random.nextFloat(),
									living.getParticleZ(1.25),
									random.nextInt(2), 0, random.nextFloat() / 6F, 0, 0);
						}
					}
				}
			}
			
			return entities;
		}
		return original;
	}

	@ModifyVariable(method = "attack", at = @At(value = "STORE:LAST"), index = 8, require = 1, allow = 1)
	private boolean spectrum$binglebongle(boolean value, Entity target) {
		if (hasForcedCrits(target)) {
			return true;
		}

		return value;
	}

	@Inject(
			method = {"attack(Lnet/minecraft/entity/Entity;)V"},
			at = {@At(
					value = "INVOKE",
					target = "Lnet/minecraft/enchantment/EnchantmentHelper;getFireAspect(Lnet/minecraft/entity/LivingEntity;)I")}
	)
	private void spectrum$perfectCounter(Entity target, CallbackInfo ci, @Local(ordinal = 0) LocalFloatRef damage) {
		var player = (PlayerEntity) (Object) this;
		if (MiscPlayerDataComponent.get(player).consumePerfectCounter()) {
			damage.set(damage.get() * 1.5F);
		}
	}

	@Unique
	protected boolean hasForcedCrits(Entity target) {
		var player = (PlayerEntity) (Object) this;
		var component = MiscPlayerDataComponent.get(player);

		if (NectarLanceItem.sleepCrits(player, target)) {
			return true;
		}
		else if (component.isParrying()) {
			component.setParryTicks(0);
			return true;
		}
		else return component.isLunging();
	}

	@WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V", ordinal = 2))
	protected void spectrum$switchCritSound(World instance, PlayerEntity except, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, Operation<Void> original) {
		var player = (PlayerEntity) (Object) this;
		var stack = this.getStackInHand(Hand.MAIN_HAND);
		var component = MiscPlayerDataComponent.get(player);
		if (stack.getItem() instanceof LightGreatswordItem && component.isLunging()) {
			original.call(instance, except, x, y, z, SpectrumSoundEvents.LUNGE_CRIT, category, 1F, 1F + random.nextFloat() * 0.2F);
			return;
		}
		original.call(instance, except, x, y, z, sound, category, volume, pitch);
	}
	
	@WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V", ordinal = 1))
	protected void spectrum$switchSweepSound(World instance, PlayerEntity except, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, Operation<Void> original) {
		var stack = this.getStackInHand(Hand.MAIN_HAND);
		if (stack.getItem() == SpectrumItems.DRACONIC_TWINSWORD && getChanneling(stack) > 0) {
			this.getWorld().playSound(except, x, y, z, SpectrumSoundEvents.ELECTRIC_DISCHARGE, category, 0.75F, 0.9F + random.nextFloat() * 0.2F);
			return;
		}
		original.call(instance, except, x, y, z, sound, category, volume, pitch);
	}
	
	@Unique
	protected int getChanneling(ItemStack stack) {
		return EnchantmentHelper.getLevel(Enchantments.CHANNELING, stack);
	}

	@Inject(at = @At("TAIL"), method = "jump()V")
	protected void spectrum$jumpAdvancementCriterion(CallbackInfo ci) {

		if ((Object) this instanceof ServerPlayerEntity serverPlayerEntity) {
			SpectrumAdvancementCriteria.TAKE_OFF_BELT_JUMP.trigger(serverPlayerEntity);
		}
	}
	
	@ModifyVariable(method = "damageArmor(Lnet/minecraft/entity/damage/DamageSource;F)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float spectrum$damageArmor(float amount, DamageSource source) {
		if (source.isIn(SpectrumDamageTypeTags.DOES_NOT_DAMAGE_ARMOR)) {
			return 0;
		}
		else if (source.isIn(SpectrumDamageTypeTags.INCREASED_ARMOR_DAMAGE)) {
			return amount * 10;
		}
		return amount;
	}
	
	@Override
	public void setSpectrumBobber(SpectrumFishingBobberEntity bobber) {
		this.spectrum$fishingBobber = bobber;
	}

	@Override
	public void setSleepTimer(int ticks) {
		this.sleepTimer = ticks;
	}

	@Override
	public SpectrumFishingBobberEntity getSpectrumBobber() {
		return this.spectrum$fishingBobber;
	}
	
	@Inject(at = @At("HEAD"), method = "canFoodHeal()Z", cancellable = true)
	public void canFoodHeal(CallbackInfoReturnable<Boolean> cir) {
		PlayerEntity player = (PlayerEntity) (Object) this;
		if (player.hasStatusEffect(SpectrumStatusEffects.SCARRED)) {
			cir.setReturnValue(false);
		}
	}
	
	// If the player holds an ExperienceStorageItem in their hands
	// experience is tried to get put in there first
	@ModifyVariable(at = @At("HEAD"), method = "addExperience(I)V", argsOnly = true)
	public int addExperience(int experience) {
		if (experience < 0) { // draining XP, like Botanias Rosa Arcana
			return experience;
		}
		
		// if the player has a ExperienceStorageItem in hand add the XP to that
		PlayerEntity player = (PlayerEntity) (Object) this;
		for (ItemStack stack : getHandItems()) {
			if (!player.isUsingItem() && stack.getItem() instanceof ExperienceStorageItem) {
				experience = ExperienceStorageItem.addStoredExperience(stack, experience);
				player.experiencePickUpDelay = 0;
				if (experience == 0) {
					break;
				}
			}
		}
		return experience;
	}
	
	@ModifyVariable(method = "getBlockBreakingSpeed",
			slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z"),
					to = @At("TAIL")
			),
			at = @At(value = "LOAD"),
			ordinal = 1
	)
	public float applyInexorableEffects(float value) {
		if (isInexorableActive())
			return 1F;
		
		return value;
	}

	@ModifyReturnValue(method = "getBlockBreakingSpeed", at = @At("RETURN"))
	public float applyInexorableAntiSlowdowns(float original) {
		if (isInexorableActive()) {
			var player = (PlayerEntity) (Object) this;
			var f = original;

			if (player.isSubmergedIn(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(player))
				f *= 5;

			if (!player.isOnGround())
				f *= 5;

			return f;
		}

		return original;

	}

	@Inject(method = "wakeUp(ZZ)V", at = @At(value = "HEAD"))
	public void spectrum$applyWakeUpEffects(boolean skipSleepTimer, boolean updateSleepingPlayers, CallbackInfo ci) {
		var player = (PlayerEntity) (Object) this;
		if (!player.getWorld().isClient())
			MiscPlayerDataComponent.get(player).resetSleepingState(true);
	}

	@WrapOperation(method = "updatePose", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setPose(Lnet/minecraft/entity/EntityPose;)V"))
	public void spectrum$forceSwimmingState(PlayerEntity instance, EntityPose entityPose, Operation<Void> original) {
		var component = MiscPlayerDataComponent.get(instance);
		if ((component.shouldLieDown() || instance.hasStatusEffect(SpectrumStatusEffects.FATAL_SLUMBER)) && wouldPoseNotCollide(EntityPose.SWIMMING)) {
			instance.setPose(EntityPose.SWIMMING);
			return;
		}
		original.call(instance, entityPose);
	}
	
	@Unique
	private boolean isInexorableActive() {
		PlayerEntity player = (PlayerEntity) (Object) this;
		return SpectrumEnchantments.INEXORABLE.canEntityUse(player) && EnchantmentHelper.getLevel(SpectrumEnchantments.INEXORABLE, player.getStackInHand(player.getActiveHand())) > 0;
	}
	
}
