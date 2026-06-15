package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.*;
import com.llamalad7.mixinextras.sugar.ref.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.damage_type.*;
import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.cca.azure_dike.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.helpers.enchantments.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import dev.emi.trinkets.api.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.food.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	
	@Shadow
	@Nullable
	protected Player lastHurtByPlayer;
	
	@Shadow
	public abstract boolean hasEffect(Holder<MobEffect> effect);
	
	@Shadow
	public abstract ItemStack getMainHandItem();

	@Shadow
	public abstract @Nullable MobEffectInstance getEffect(Holder<MobEffect> effect);
	
	@Shadow
	public abstract void readAdditionalSaveData(CompoundTag nbt);
	
	@Shadow
	public abstract boolean hurt(DamageSource source, float amount);
	
	@Shadow
	public abstract ItemStack getOffhandItem();
	
	@Shadow
	public abstract int getArmorValue();
	
	@Shadow
	public abstract void remove(Entity.RemovalReason reason);
	
	@Shadow
	public abstract void travel(Vec3 movementInput);
	
	@Shadow
	protected ItemStack useItem;
	
	@Shadow
	public abstract double getAttributeValue(Holder<Attribute> attribute);
	
	@Shadow
	protected abstract @Nullable SoundEvent getDeathSound();
	
	@Shadow
	protected abstract float getSoundVolume();
	
	@Shadow
	protected boolean dead;
	
	// FabricDefaultAttributeRegistry seems to only allow adding full containers and only single entity types?
	@Inject(method = "createLivingAttributes", require = 1, allow = 1, at = @At("RETURN"))
	private static void spectrum$addAttributes(final CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
		cir.getReturnValue().add(SpectrumEntityAttributes.MENTAL_PRESENCE);
	}
	
	@ModifyArg(method = "dropExperience", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"), index = 2)
	protected int spectrum$applyExuberance(int originalXP) {
		return (int) (originalXP * spectrum$getExuberanceMod(this.lastHurtByPlayer));
	}
	
	@Unique
	private float spectrum$getExuberanceMod(Player attackingPlayer) {
		if (attackingPlayer != null) {
			int exuberanceLevel = SpectrumEnchantmentHelper.getEquipmentLevel(attackingPlayer.level().registryAccess(), SpectrumEnchantments.EXUBERANCE, attackingPlayer);
			return 1.0F + exuberanceLevel * SpectrumCommon.CONFIG.ExuberanceBonusExperiencePercentPerLevel;
		} else {
			return 1.0F;
		}
	}
	
	@ModifyVariable(method = "travel", at = @At(value = "STORE"), ordinal = 0)
	private boolean spectrum$noSlowFallingSlowdown(boolean b) {
		if (!b) {
			return false;
		}
		return !InexorableHelper.isArmorActive((LivingEntity) (Object) this);
	}
	
	@Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;shouldDiscardFriction()Z"))
	private void spectrum$travel(CallbackInfo ci, @Local(ordinal = 1) LocalFloatRef f) {
		var entity = (LivingEntity) (Object) this;
		var override = false;
		var friction = -1F;
		
		if (SlotReservingItem.isReservingSlot(this.getMainHandItem()) || SlotReservingItem.isReservingSlot(this.getOffhandItem())) {
			if (!(entity).onGround()) {
				friction = 0.945F;
				override = true;
			}
		}
		
		if (!entity.onGround()) {
			var optionalTrinket = SpectrumTrinketItem.getFirstEquipped(entity, SpectrumItems.RING_OF_AERIAL_GRACE);
			if (optionalTrinket.isPresent()) {
				var inkStorage = SpectrumItems.RING_OF_AERIAL_GRACE.getEnergyStorage(optionalTrinket.get());
				var storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
				friction = (float) Math.max(friction, 0.91 + (((RingOfAerialGraceItem) SpectrumItems.RING_OF_AERIAL_GRACE).getBonus(storedInk) / 150F));
				override = true;
			}
		}
		
		if (entity instanceof Player player) {
			if (override) {
				friction += MiscPlayerDataComponent.get(player).getFrictionModifiers();
			} else {
				f.set(Math.min(f.get() + MiscPlayerDataComponent.get(player).getFrictionModifiers(), 0.99F));
			}
		}
		
		if (friction >= 0)
			f.set(Math.min(friction, 0.99F));
	}
	
	@ModifyExpressionValue(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getFriction()F"))
	private float spectrum$increaseSlipperiness(float original) {
		var entity = (LivingEntity) (Object) this;
		var random = entity.getRandom();
		var potency = SleepStatusEffect.getSleepScaling(entity);
		if (potency != -1) {
			potency *= 2;
			
			if (entity instanceof Player && random.nextFloat() < potency * 0.05) {
				return 0.35F + random.nextFloat() * 0.45F;
			}
			
			original = (float) Math.min(original + 0.3 + (potency / 25F), 0.9975F);
		}
		return original;
	}
	
	@ModifyReturnValue(method = "canStandOnFluid", at = @At("RETURN"))
	private boolean spectrum$modifyFluidWalking(boolean original) {
		var entity = (LivingEntity) (Object) this;
		
		if (SpectrumTrinketItem.hasEquipped(entity, SpectrumItems.RING_OF_AERIAL_GRACE))
			return !entity.isUnderWater();
		
		return original;
	}
	
	@ModifyExpressionValue(method = "isBlocking", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;getUseDuration(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)I"))
	private int spectrum$allowInstantBlockForParryingSwords(int original) {
		if (useItem.getItem() instanceof ParryingSwordItem)
			return Integer.MAX_VALUE;
		
		return original;
	}
	
	@WrapOperation(method = "handleEntityEvent", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.playSound (Lnet/minecraft/sounds/SoundEvent;FF)V", ordinal = 2))
	private void spectrum$swapBlockSound(LivingEntity instance, SoundEvent soundEvent, float v, float p, Operation<Void> original) {
		if (!(instance.getUseItem().getItem() instanceof ParryingSwordItem parryingSword)) {
			original.call(instance, soundEvent, v, p);
			return;
		}
		
		if (instance.getTicksUsingItem() <= parryingSword.getPerfectParryWindow(instance, instance.getUseItem())) {
			original.call(instance, SpectrumSoundEvents.PERFECT_PARRY, 1.75F, 0.9F + instance.level().getRandom().nextFloat() * 0.3F);
			original.call(instance, SpectrumSoundEvents.SWORD_BLOCK, 0.667F, 0.5F + instance.level().getRandom().nextFloat() * 0.3F);
		} else {
			original.call(instance, SpectrumSoundEvents.SWORD_BLOCK, 1.0F, 0.8F + instance.level().getRandom().nextFloat() * 0.4F);
		}
	}
	
	
	@Inject(method = "eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEatEffect(Lnet/minecraft/world/food/FoodProperties;)V"))
	private void spectrum$applyConcealedEffects(Level world, ItemStack stack, FoodProperties foodComponent, CallbackInfoReturnable<ItemStack> cir) {
		var oilEffect = stack.get(SpectrumDataComponentTypes.CONCEALED_EFFECT);
		if (!world.isClientSide() && oilEffect != null)
			((LivingEntity) (Object) this).addEffect(new MobEffectInstance(oilEffect));
	}
	
	@ModifyReturnValue(method = "canBeAffected", at = @At("RETURN"))
	private boolean spectrum$canHaveStatusEffect(boolean original, @Local(argsOnly = true) MobEffectInstance statusEffectInstance) {
		var instance = (LivingEntity) (Object) this;
		
		if (original && this.hasEffect(SpectrumStatusEffects.IMMUNITY) && statusEffectInstance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
			if (StatusEffectHelper.isSevere(statusEffectInstance)) {
				var immunity = getEffect(SpectrumStatusEffects.IMMUNITY);
				var cost = 600 * (statusEffectInstance.getAmplifier() + 1);
				
				if (immunity.getDuration() >= cost) {
					immunity.spectrum$setDuration(Math.max(5, immunity.getDuration() - cost));
					if (!instance.level().isClientSide()) {
						((ServerLevel) instance.level()).getChunkSource().broadcastAndSend(instance, new ClientboundUpdateMobEffectPacket(instance.getId(), immunity, false));
					}
					return false;
				} else {
					return true;
				}
			}
			
			return false;
		}
		return original;
	}
	
	@ModifyReturnValue(method = "canDisableShield", at = @At("RETURN"))
	private boolean spectrum$lungeBreaksShields(boolean original) {
		if ((LivingEntity) (Object) this instanceof Player player
				&& MiscPlayerDataComponent.get(player).isLunging()) {
			return player.getMainHandItem().getItem() instanceof LightGreatswordItem;
		}
		return original;
	}
	
	@ModifyExpressionValue(
			method = {"hurt"},
			at = {@At(
					value = "CONSTANT",
					args = {"floatValue=0F"},
					ordinal = 2
			)}
	)
	private float spectrum$parryingSwordShielding(float original, @Local(argsOnly = true) DamageSource source, @Local(ordinal = 2) float shieldedDamage) {
		var entity = (LivingEntity) (Object) this;
		var activeStack = entity.getUseItem();
		var useTime = entity.getTicksUsingItem();
		
		if (!(activeStack.getItem() instanceof ParryingSwordItem parryingSword))
			return original;
		
		if (entity instanceof Player player && parryingSword.canBluffParry(activeStack, entity, useTime)) {
			var comp = MiscPlayerDataComponent.get(player);
			comp.setParryTicks(15);
			
			if (parryingSword.canPerfectParry(activeStack, entity, useTime))
				comp.markForPerfectCounter();
		}
		
		return shieldedDamage * parryingSword.getBlockingMultiplier(source, activeStack, entity, useTime);
	}
	
	@ModifyExpressionValue(method = "isDamageSourceBlocked", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;getPierceLevel()B"))
	private byte spectrum$parryPiercingProjectiles(byte original) {
		var entity = (LivingEntity) (Object) this;
		var activeStack = entity.getUseItem();
		
		if (activeStack.getItem() instanceof ParryingSwordItem parryingSword)
			return parryingSword.canBluffParry(activeStack, entity, entity.getTicksUsingItem()) ? 0 : original;
		
		return original;
	}
	
	@ModifyExpressionValue(method = "isDamageSourceBlocked", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z"))
	private boolean spectrum$parryShieldUnblockables(boolean original, DamageSource source) {
		var entity = (LivingEntity) (Object) this;
		var activeStack = entity.getUseItem();
		
		if (!(activeStack.getItem() instanceof ParryingSwordItem parryingSword))
			return original;
		
		return source.is(SpectrumDamageTypeTags.BYPASSES_PARRYING)
				|| !parryingSword.canDeflect(source, parryingSword.canPerfectParry(activeStack, entity, entity.getTicksUsingItem()));
	}
	
	@ModifyVariable(method = "hurtArmor", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float spectrum$damageArmor(float amount, DamageSource source) {
		if (source.is(SpectrumDamageTypeTags.DOES_NOT_DAMAGE_ARMOR)) {
			return 0;
		} else if (source.is(SpectrumDamageTypeTags.INCREASED_ARMOR_DAMAGE)) {
			return amount * 10;
		}
		return amount;
	}
	
	@ModifyArg(method = "getDamageAfterMagicAbsorb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterMagicAbsorb(FF)F"), index = 1)
	private float spectrum$modifyAppliedDamage(float protection, @Local(argsOnly = true) DamageSource source) {
		var pair = getArmorPiercing(source);
		
		if (pair.isPresent()) {
			var ap = pair.get().getA();
			var stack = pair.get().getB();
			
			var modProt = Math.max(protection, 20F) / 25F;
			protection = Math.max(modProt - ap.getProtReduction((LivingEntity) (Object) this, stack), 0) * 20F;
		}
		
		return protection;
	}
	
	@ModifyVariable(method = "getDamageAfterArmorAbsorb", at = @At("STORE"), ordinal = 0, argsOnly = true)
	private float spectrum$applyArmorToDamage(float amount, DamageSource source) {
		float defense = getArmorValue();
		float toughness = getToughness();
		var modified = false;
		var pair = getArmorPiercing(source);
		var entity = (LivingEntity) (Object) this;
		
		if (pair.isPresent()) {
			var ap = pair.get().getA();
			var stack = pair.get().getB();
			
			defense *= ap.getDefenseMultiplier(entity, stack);
			toughness *= ap.getToughnessMultiplier(entity, stack);
			modified = true;
		}
		
		if (source.is(SpectrumDamageTypeTags.CALCULATES_DAMAGE_BASED_ON_TOUGHNESS)) {
			amount = CombatRules.getDamageAfterAbsorb(entity, amount, source, toughness * 1.334F, Float.MAX_VALUE);
		} else if (source.is(SpectrumDamageTypeTags.PARTLY_IGNORES_PROTECTION)) {
			amount = CombatRules.getDamageAfterAbsorb(entity, amount, source, defense / 2, toughness);
		}
		
		if (modified) {
			amount = CombatRules.getDamageAfterAbsorb(entity, amount, source, defense, toughness);
		}
		
		return amount;
	}
	
	@Unique
	private Optional<Tuple<ArmorPiercingItem, ItemStack>> getArmorPiercing(DamageSource source) {
		if (!(source instanceof StackTracking stackTracking))
			return Optional.empty();
		
		var stackOptional = stackTracking.spectrum$getTrackedStack();
		
		if (stackOptional.isEmpty())
			return Optional.empty();
		
		var stack = stackOptional.get();
		
		if (!(stack.getItem() instanceof ArmorPiercingItem ap))
			return Optional.empty();
		
		return Optional.of(new Tuple<>(ap, stack));
	}
	
	@Unique
	private float getToughness() {
		return (float) this.getAttributeValue(Attributes.ARMOR_TOUGHNESS);
	}
	
	@ModifyExpressionValue(method = "causeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;calculateFallDamage(FF)I"))
	private int spectrum$puffCircletDamageNegation(int original) {
		LivingEntity thisEntity = (LivingEntity) (Object) this;
		float cost = Math.min(original, PuffCircletItem.FALL_DAMAGE_NEGATING_COST);
		// check if damage reduction is applicable to this entity
		if (original <= 0 || thisEntity.isInvulnerableTo(thisEntity.damageSources().fall()) || AzureDikeProvider.getAzureDikeCharges(thisEntity) <= cost) return original;
		
		// check if this entity is protected by puff circlet
		Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(thisEntity);
		if (component.isEmpty() || component.get().getEquipped(SpectrumItems.PUFF_CIRCLET).isEmpty()) return original;
		
		// do damage reduction
		AzureDikeProvider.absorbDamage(thisEntity, cost);
		
		// yoink
		Vec3 velocity = thisEntity.getDeltaMovement();
		thisEntity.setDeltaMovement(velocity.x(), 0.5, velocity.z());
		Level world = thisEntity.level();
		if (world.isClientSide()) { // it is split here so the particles spawn immediately, without network lag
			ParticleHelper.playParticleWithPatternAndVelocityClient(thisEntity.level(), thisEntity.position(), ColoredCraftingParticleEffect.WHITE, VectorPattern.EIGHT, 0.4);
			ParticleHelper.playParticleWithPatternAndVelocityClient(thisEntity.level(), thisEntity.position(), ColoredCraftingParticleEffect.BLUE, VectorPattern.EIGHT_OFFSET, 0.5);
		} else if (thisEntity instanceof ServerPlayer serverPlayerEntity) {
			PlayParticleWithPatternAndVelocityPayload.playParticleWithPatternAndVelocity(serverPlayerEntity, (ServerLevel) thisEntity.level(), thisEntity.position(), ColoredCraftingParticleEffect.WHITE, VectorPattern.EIGHT, 0.4);
			PlayParticleWithPatternAndVelocityPayload.playParticleWithPatternAndVelocity(serverPlayerEntity, (ServerLevel) thisEntity.level(), thisEntity.position(), ColoredCraftingParticleEffect.BLUE, VectorPattern.EIGHT_OFFSET, 0.5);
		}
		thisEntity.level().playSound(null, thisEntity.blockPosition(), SpectrumSoundEvents.PUFF_CIRCLET_PFFT, SoundSource.PLAYERS, 1.0F, 1.0F);
		
		return 0;
	}
	
	@ModifyVariable(at = @At("HEAD"), method = "hurt", argsOnly = true)
	private float spectrum$modifyDamage(float amount, DamageSource source) {
		@Nullable MobEffectInstance vulnerability = getEffect(SpectrumStatusEffects.VULNERABILITY);
		if (vulnerability != null) {
			amount *= 1 + (SpectrumStatusEffects.VULNERABILITY_ADDITIONAL_DAMAGE_PERCENT_PER_LEVEL * vulnerability.getAmplifier() + 1);
		}
		return amount;
	}
	
	@Inject(at = @At(value = "INVOKE", target = "net/minecraft/world/damagesource/DamageSource.is (Lnet/minecraft/tags/TagKey;)Z", ordinal = 1), method = "hurt")
	private void spectrum$allowPartialBlocks(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		var entity = (LivingEntity) (Object) this;
		var activeItem = entity.getUseItem();
		
		if (!(activeItem.getItem() instanceof ParryingSwordItem))
			return;
	}
	
	@WrapOperation(at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.actuallyHurt (Lnet/minecraft/world/damagesource/DamageSource;F)V", ordinal = 0), method = "hurt")
	private void spectrum$applyDike1(LivingEntity instance, DamageSource source, float amount, Operation<Void> original) {
		if (source.is(SpectrumDamageTypeTags.BYPASSES_DIKE)) {
			original.call(instance, source, amount);
			return;
		}
		instance.actuallyHurt(source, AzureDikeProvider.absorbDamage(instance, amount));
	}
	
	@WrapOperation(at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.actuallyHurt (Lnet/minecraft/world/damagesource/DamageSource;F)V", ordinal = 1), method = "hurt")
	private void spectrum$applyDike2(LivingEntity instance, DamageSource source, float amount, Operation<Void> original) {
		if (source.is(SpectrumDamageTypeTags.BYPASSES_DIKE)) {
			original.call(instance, source, amount);
			return;
		}
		instance.actuallyHurt(source, AzureDikeProvider.absorbDamage(instance, amount));
	}
	
	@Inject(method = "tickEffects", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;remove()V"))
	private void spectrum$fatalSlumberKill(CallbackInfo ci, @Local MobEffectInstance effectInstance) {
		if (effectInstance.getEffect() == SpectrumStatusEffects.FATAL_SLUMBER) {
			var entity = (LivingEntity) (Object) this;
			
			if (entity.level().isClientSide())
				return;
			
			if (entity.isSpectator() || entity instanceof Player player && player.getAbilities().instabuild)
				return;
			
			var damage = Float.MAX_VALUE;
			if (SleepStatusEffect.isImmuneish(entity)) {
				if (entity instanceof Player)
					damage = entity.getHealth() * 0.95F;
				else
					damage = entity.getMaxHealth() * 0.3F;
			}
			
			entity.hurt(SpectrumDamageTypes.sleep(entity.level(), null), damage);
			if (entity.isAlive() && entity instanceof ServerPlayer serverPlayerEntity && !serverPlayerEntity.isCreative()) {
				Support.grantAdvancementCriterion(serverPlayerEntity, "lategame/survive_fatal_slumber", "survived_fatal_slumber");
			}
		}
	}
	
	/**
	 * We do not force player sleeping because that would do funny things to the sleep cycle
	 */
	@ModifyReturnValue(method = "isSleeping", at = @At("RETURN"))
	private boolean spectrum$forceSleepingState(boolean original) {
		if (original)
			return true;
		
		if (hasEffect(SpectrumStatusEffects.ETERNAL_SLUMBER) || hasEffect(SpectrumStatusEffects.FATAL_SLUMBER))
			return !(((LivingEntity) (Object) this) instanceof Player);
		
		return false;
	}
	
	@Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
	private void spectrum$addEffect(MobEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
		var entity = (LivingEntity) (Object) this;
		var effectType = effect.getEffect();
		
		// if it is a stacking effect, stack it
		MobEffectInstance existingInstance = this.getEffect(effectType);
		if (existingInstance != null && effectType.is(SpectrumStatusEffectTags.STACKING)) {
			SpectrumStatusEffects.effectsAreGettingStacked = true;
			
			int newAmplifier = 1 + existingInstance.getAmplifier() + effect.getAmplifier();
			effect.spectrum$setAmplifier(newAmplifier);
			SpectrumStatusEffects.effectsAreGettingStacked = false;
		}
		
		if ((!entity.hasEffect(SpectrumStatusEffects.IMMUNITY)) && AetherGracedNectarGlovesItem.testEffectFor(entity, effectType)) {
			var cost = (effect.getAmplifier() + 1) * AetherGracedNectarGlovesItem.HARMFUL_EFFECT_COST;
			
			if (StatusEffectHelper.isSevere(effect))
				cost *= 3;
			
			if (AetherGracedNectarGlovesItem.tryBlockEffect(entity, cost)) {
				cir.setReturnValue(false);
				return;
			}
		}
		
		var resistanceModifier = Mth.clamp(SleepStatusEffect.getSleepResistance(effect, entity), 0.1F, 10F);
		if (effectType == SpectrumStatusEffects.ETERNAL_SLUMBER) {
			if (SleepStatusEffect.isImmuneish(entity)) {
				effect.spectrum$setDuration(Math.round(effect.getDuration() / resistanceModifier));
			} else if (!entity.getType().is(SpectrumEntityTypeTags.SLEEP_RESISTANT)) {
				effect.spectrum$setDuration(MobEffectInstance.INFINITE_DURATION);
			}
		} else if (effectType == SpectrumStatusEffects.FATAL_SLUMBER) {
			if (SleepStatusEffect.isImmuneish(entity)) {
				effect.spectrum$setDuration(20 * 60);
			} else {
				effect.spectrum$setDuration(Math.max(Math.round(effect.getDuration() * resistanceModifier * 3), 20 * 10));
			}
		}
	}
	
	@Inject(at = @At("RETURN"), method = "hurt")
	private void spectrum$applyDisarmingEnchantment(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		// true if the entity got hurt
		var entity = (LivingEntity) (Object) this;
		if (amount > 0 && cir.getReturnValue() != null && cir.getReturnValue()) {
			// Disarming does not trigger when dealing damage to enemies using thorns
			if (!source.is(DamageTypes.THORNS)) {
				if (source.getEntity() instanceof LivingEntity livingSource) {
					int disarmingLevel = SpectrumEnchantmentHelper.getLevel(entity.level().registryAccess(), SpectrumEnchantments.DISARMING, livingSource.getMainHandItem());
					if (disarmingLevel > 0 && Math.random() < disarmingLevel * SpectrumCommon.CONFIG.DisarmingChancePerLevelMobs) {
						DisarmingHelper.disarmEntity(entity);
					}
				}
			}
		}
	}
	
	@Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z"), cancellable = true)
	private void spectrum$applyBonusDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity target = (LivingEntity) (Object) this;
		
		// SetHealth damage does exactly that
		if (amount > 0 && source.is(SpectrumDamageTypeTags.USES_SET_HEALTH)) {
			float h = target.getHealth();
			target.setHealth(h - amount);
			target.getCombatTracker().recordDamage(source, amount);
			if (target.isDeadOrDying()) {
				if (!dead) {
					var deathSound = getDeathSound();
					if (deathSound != null)
						target.playSound(deathSound, getSoundVolume(), target.getVoicePitch());
				}
				target.die(source);
			}
			cir.setReturnValue(true);
			return;
		}
		
		// If this entity is hit with a SplitDamageItem, damage() gets called recursively for each type of damage dealt
		if (!SpectrumDamageTypes.recursiveDamageFlag && amount > 0 && source.getDirectEntity() instanceof LivingEntity livingSource) {
			ItemStack mainHandStack = livingSource.getMainHandItem();
			if (mainHandStack.getItem() instanceof SplitDamageItem splitDamageItem) {
				SpectrumDamageTypes.recursiveDamageFlag = true;
				SplitDamageItem.DamageComposition composition = splitDamageItem.getDamageComposition(livingSource, target, mainHandStack, amount);
				
				boolean damaged = false;
				for (Tuple<DamageSource, Float> entry : composition.get()) {
					int invulnerableTimeStore = target.invulnerableTime;
					target.invulnerableTime = 0;
					damaged |= hurt(entry.getA(), entry.getB());
					target.invulnerableTime = invulnerableTimeStore;
				}
				
				SpectrumDamageTypes.recursiveDamageFlag = false;
				cir.setReturnValue(damaged);
			}
		}
	}
	
	@Inject(method = "hurt", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.isDeadOrDying ()Z", ordinal = 1))
	private void spectrum$TriggerArmorWithHitEffect(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity thisEntity = (LivingEntity) (Object) this;
		Level world = thisEntity.level();
		if (!world.isClientSide()) {
			if (thisEntity instanceof Mob thisMobEntity) {
				for (ItemStack armorItemStack : thisMobEntity.getArmorSlots()) {
					if (armorItemStack.getItem() instanceof ArmorWithHitEffect armorWithHitEffect) {
						armorWithHitEffect.onHit(armorItemStack, source, thisMobEntity, amount);
					}
				}
			} else if (thisEntity instanceof ServerPlayer thisPlayerEntity) {
				for (ItemStack armorItemStack : thisPlayerEntity.getArmorSlots()) {
					if (armorItemStack.getItem() instanceof ArmorWithHitEffect armorWithHitEffect) {
						armorWithHitEffect.onHit(armorItemStack, source, thisPlayerEntity, amount);
					}
				}
			}
		}
	}
	
	@ModifyVariable(method = "setSprinting(Z)V", at = @At("HEAD"), argsOnly = true)
	private boolean spectrum$setSprinting(boolean sprinting) {
		var entity = (LivingEntity) (Object) this;
		if (sprinting && entity.hasEffect(SpectrumStatusEffects.SCARRED)) {
			return false;
		}
		return sprinting;
	}
	
	@Inject(method = "eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "HEAD"))
	private void spectrum$conditionalFood(Level level, ItemStack food, FoodProperties foodProperties, CallbackInfoReturnable<ItemStack> cir) {
		PairedFoodComponent component = food.get(SpectrumDataComponentTypes.PAIRED_FOOD_COMPONENT);
		if (component != null) {
			component.tryEatFood(level, (LivingEntity) (Object) this, food);
		}
	}
	
	@Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
	private void spectrum$addStatusEffect(MobEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
		if (EffectProlongingStatusEffect.canBeExtended(effect.getEffect())) {
			MobEffectInstance effectProlongingInstance = this.getEffect(SpectrumStatusEffects.EFFECT_PROLONGING);
			if (effectProlongingInstance != null) {
				effect.spectrum$setDuration(EffectProlongingStatusEffect.getExtendedDuration(effect.getDuration(), effectProlongingInstance.getAmplifier()));
			}
		}
	}
	
	@Inject(method = "dropAllDeathLoot", at = @At("HEAD"), cancellable = true)
	protected void drop(ServerLevel world, DamageSource damageSource, CallbackInfo ci) {
		LivingEntity thisEntity = (LivingEntity) (Object) this;
		
		if (EverpromiseRibbonComponent.hasRibbon(thisEntity)) {
			ItemStack memoryStack = MemoryItem.getMemoryForEntity(thisEntity);
			MemoryItem.setTicksToManifest(memoryStack, 20);
			MemoryItem.setSpawnAsAdult(memoryStack, true);
			MemoryItem.markAsBrokenPromise(memoryStack, true);
			
			Vec3 entityPos = thisEntity.position();
			ItemEntity itemEntity = new ItemEntity(thisEntity.level(), entityPos.x(), entityPos.y(), entityPos.z(), memoryStack);
			thisEntity.level().addFreshEntity(itemEntity);
			
			ci.cancel();
		}
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	protected void applyInexorableEffects(CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		if (entity.level() != null && entity.level().getGameTime() % 20 == 0) {
			InexorableHelper.checkAndRemoveSlowdownModifiers(entity);
		}
	}
	
	@Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWaterRainOrBubble()Z"))
	private boolean spectrum$isWet(LivingEntity livingEntity) {
		return livingEntity.isInWater() ? ((TouchingWaterAware) livingEntity).spectrum$isActuallyTouchingWater() : livingEntity.isInWaterRainOrBubble();
	}
	
}