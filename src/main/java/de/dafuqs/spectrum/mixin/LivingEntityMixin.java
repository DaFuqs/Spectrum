package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.damage_type.StackTracking;
import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.status_effect.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.cca.azure_dike.*;
import de.dafuqs.spectrum.enchantments.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.tools.DragonTalonItem;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import dev.emi.trinkets.api.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Shadow
	@Nullable
	protected PlayerEntity attackingPlayer;

	@Shadow
	@Final
	private DefaultedList<ItemStack> syncedArmorStacks;

	@Shadow
	public abstract boolean hasStatusEffect(StatusEffect effect);

	@Shadow
	public abstract boolean blockedByShield(DamageSource source);

	@Shadow
	public abstract ItemStack getMainHandStack();

	@Shadow
	@Nullable
	public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

	@Shadow
	public abstract boolean canHaveStatusEffect(StatusEffectInstance effect);

	@Shadow
	protected ItemStack activeItemStack;

	@Shadow
	public abstract void readCustomDataFromNbt(NbtCompound nbt);
	
	@Shadow
	public abstract boolean damage(DamageSource source, float amount);
	
	@Shadow
	public abstract boolean removeStatusEffect(StatusEffect type);
	
	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);

	@Shadow public abstract ItemStack getOffHandStack();

	@Shadow public abstract void damageArmor(DamageSource source, float amount);

	@Shadow public abstract int getArmor();

	@Shadow public abstract double getAttributeValue(EntityAttribute attribute);

	@ModifyArg(method = "dropXp()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ExperienceOrbEntity;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;I)V"), index = 2)
	protected int spectrum$applyExuberance(int originalXP) {
		return (int) (originalXP * spectrum$getExuberanceMod(this.attackingPlayer));
	}
	
	@Unique
	private float spectrum$getExuberanceMod(PlayerEntity attackingPlayer) {
		if (attackingPlayer != null && SpectrumEnchantments.EXUBERANCE.canEntityUse(attackingPlayer)) {
			int exuberanceLevel = EnchantmentHelper.getEquipmentLevel(SpectrumEnchantments.EXUBERANCE, attackingPlayer);
			return 1.0F + exuberanceLevel * SpectrumCommon.CONFIG.ExuberanceBonusExperiencePercentPerLevel;
		} else {
			return 1.0F;
		}
	}

	@Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasNoDrag()Z"))
	public void spectrum$modifyDragPhysics(CallbackInfo ci, @Local(ordinal = 1) LocalFloatRef f) {
		var needle = (DragonTalonItem) SpectrumItems.DRAGON_TALON;
		if (needle.isReservingSlot(this.getMainHandStack()) || needle.isReservingSlot(this.getOffHandStack())) {
			if (!((LivingEntity) (Object) this).isOnGround()) {
				f.set(0.945F);
			}
		}
	}

	@ModifyArg(method = "modifyAppliedDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getInflictedDamage(FF)F"), index = 1)
	public float modifyProtectionValues(float protection, @Local(argsOnly = true) DamageSource source) {
		var pair = getArmorPiercing(source);

		if (pair.isPresent()) {
			var ap = pair.get().getLeft();
			var stack = pair.get().getRight();

			var modProt = Math.max(protection, 20F) / 25F;
			protection = Math.max(modProt - ap.getProtReduction((LivingEntity) (Object) this, stack), 0) * 20F;
		}

		return protection;
	}

	@Inject(method = "applyArmorToDamage", at = @At("HEAD"), cancellable = true)
	public void spectrum$applySpecialArmorEffects(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
		float defense = getArmor();
		float toughness = getToughness();
		var modified = false;
		var pair = getArmorPiercing(source);

		if (pair.isPresent()) {
			var ap = pair.get().getLeft();
			var stack = pair.get().getRight();

			defense *= ap.getDefenseMultiplier((LivingEntity) (Object) this, stack);
			toughness *= ap.getToughnessMultiplier((LivingEntity) (Object) this, stack);
			modified = true;
		}

		if (source.isOf(SpectrumDamageTypes.IMPALING)) {
			this.damageArmor(source, amount * 10);
			amount = DamageUtil.getDamageLeft(amount, toughness * 1.334F, Float.MAX_VALUE);
			cir.setReturnValue(amount);
			cir.cancel();
		}
		else if(source.isOf(SpectrumDamageTypes.EVISCERATION)) {
			this.damageArmor(source, amount);
			amount = DamageUtil.getDamageLeft(amount, defense / 2, toughness);
			cir.setReturnValue(amount);
			cir.cancel();
		}

		if (modified && !source.isIn(DamageTypeTags.BYPASSES_ARMOR)) {
			this.damageArmor(source, amount);
			amount = DamageUtil.getDamageLeft(amount, defense, toughness);
			cir.setReturnValue(amount);
			cir.cancel();
		}
	}

	@Unique
	private Optional<Pair<ArmorPiercingItem, ItemStack>> getArmorPiercing(DamageSource source) {
		if (!(source instanceof StackTracking stackTracking))
			return Optional.empty();

		var stackOptional = stackTracking.spectrum$getTrackedStack();

		if (stackOptional.isEmpty())
			return Optional.empty();

		var stack = stackOptional.get();

		if (!(stack.getItem() instanceof  ArmorPiercingItem ap))
			return Optional.empty();

		return Optional.of(new Pair<>(ap, stack));
	}

	@Unique
	private float getToughness() {
		return (float) this.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
	}

	@Inject(at = @At("HEAD"), method = "fall(DZLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V")
	public void spectrum$mitigateFallDamageWithPuffCirclet(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition, CallbackInfo ci) {
		if (onGround) {
			LivingEntity thisEntity = (LivingEntity) (Object) this;
			if (!thisEntity.isInvulnerableTo(thisEntity.getDamageSources().fall()) && thisEntity.fallDistance > thisEntity.getSafeFallDistance()) {
				Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(thisEntity);
				if (component.isPresent()) {
					if (!component.get().getEquipped(SpectrumItems.PUFF_CIRCLET).isEmpty()) {
						int charges = AzureDikeProvider.getAzureDikeCharges(thisEntity);
						if (charges > 0) {
							AzureDikeProvider.absorbDamage(thisEntity, PuffCircletItem.FALL_DAMAGE_NEGATING_COST);
							
							thisEntity.fallDistance = 0;
							thisEntity.setVelocity(thisEntity.getVelocity().x, 0.5, thisEntity.getVelocity().z);
							World world = thisEntity.getWorld();
							if (world.isClient) { // it is split here so the particles spawn immediately, without network lag
								ParticleHelper.playParticleWithPatternAndVelocityClient(thisEntity.getWorld(), thisEntity.getPos(), SpectrumParticleTypes.WHITE_CRAFTING, VectorPattern.EIGHT, 0.4);
								ParticleHelper.playParticleWithPatternAndVelocityClient(thisEntity.getWorld(), thisEntity.getPos(), SpectrumParticleTypes.BLUE_CRAFTING, VectorPattern.EIGHT_OFFSET, 0.5);
							} else if (thisEntity instanceof ServerPlayerEntity serverPlayerEntity) {
								SpectrumS2CPacketSender.playParticleWithPatternAndVelocity(serverPlayerEntity, (ServerWorld) thisEntity.getWorld(), thisEntity.getPos(), SpectrumParticleTypes.WHITE_CRAFTING, VectorPattern.EIGHT, 0.4);
								SpectrumS2CPacketSender.playParticleWithPatternAndVelocity(serverPlayerEntity, (ServerWorld) thisEntity.getWorld(), thisEntity.getPos(), SpectrumParticleTypes.BLUE_CRAFTING, VectorPattern.EIGHT_OFFSET, 0.5);
							}
							thisEntity.getWorld().playSound(null, thisEntity.getBlockPos(), SpectrumSoundEvents.PUFF_CIRCLET_PFFT, SoundCategory.PLAYERS, 1.0F, 1.0F);
						}
					}
				}
			}
		}
	}

	@ModifyVariable(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", argsOnly = true)
	public float spectrum$modifyDamage(float amount, DamageSource source) {
		@Nullable StatusEffectInstance vulnerability = getStatusEffect(SpectrumStatusEffects.VULNERABILITY);
		if (vulnerability != null) {
			amount *= 1 + (SpectrumStatusEffects.VULNERABILITY_ADDITIONAL_DAMAGE_PERCENT_PER_LEVEL * vulnerability.getAmplifier());
		}
		
		if (amount <= 0
				|| source.isIn(SpectrumDamageTypeTags.BYPASSES_DIKE)
				|| this.blockedByShield(source)
				|| ((Entity) (Object) this).isInvulnerableTo(source)
				|| source.isIn(DamageTypeTags.IS_FIRE) && hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
			
			return amount;
		}
		
		return AzureDikeProvider.absorbDamage((LivingEntity) (Object) this, amount);
	}

	@Inject(at = @At("RETURN"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	public void spectrum$applyDisarmingEnchantment(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		// true if the entity got hurt
		if (cir.getReturnValue() != null && cir.getReturnValue()) {
			// Disarming does not trigger when dealing damage to enemies using thorns
			if (!source.isOf(DamageTypes.THORNS)) {
				if (source.getAttacker() instanceof LivingEntity livingSource && SpectrumEnchantments.DISARMING.canEntityUse(livingSource)) {
					int disarmingLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.DISARMING, livingSource.getMainHandStack());
					if (disarmingLevel > 0 && Math.random() < disarmingLevel * SpectrumCommon.CONFIG.DisarmingChancePerLevelMobs) {
						DisarmingEnchantment.disarmEntity((LivingEntity) (Object) this, this.syncedArmorStacks);
					}
				}
			}
		}
	}

	@Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
	public void spectrum$applyBonusDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity target = (LivingEntity) (Object) this;

		// SetHealth damage does exactly that
		if (amount > 0 && source.isIn(SpectrumDamageTypeTags.USES_SET_HEALTH)) {
			float h = target.getHealth();
			target.setHealth(h - amount);
			target.getDamageTracker().onDamage(source, amount);
			if (target.isDead()) {
				target.onDeath(source);
			}
			return;
		}

		// If this entity is hit with a SplitDamageItem, damage() gets called recursively for each type of damage dealt
		if (!SpectrumDamageTypes.recursiveDamageFlag && amount > 0 && source.getSource() instanceof LivingEntity livingSource) {
			ItemStack mainHandStack = livingSource.getMainHandStack();
			if (mainHandStack.getItem() instanceof SplitDamageItem splitDamageItem) {
				SpectrumDamageTypes.recursiveDamageFlag = true;
				SplitDamageItem.DamageComposition composition = splitDamageItem.getDamageComposition(livingSource, target, mainHandStack, amount);
				
				boolean damaged = false;
				for (Pair<DamageSource, Float> entry : composition.get()) {
					damaged |= damage(entry.getLeft(), entry.getRight());
				}
				
				SpectrumDamageTypes.recursiveDamageFlag = false;
				cir.setReturnValue(damaged);
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "tryUseTotem(Lnet/minecraft/entity/damage/DamageSource;)Z", cancellable = true)
	public void spectrum$checkForTotemPendant(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue()) {
			// if no other totem triggered: check for a totem pendant
			LivingEntity thisEntity = (LivingEntity) (Object) this;
			Optional<TrinketComponent> optionalTrinketComponent = TrinketsApi.getTrinketComponent(thisEntity);
			if (optionalTrinketComponent.isPresent()) {
				List<Pair<SlotReference, ItemStack>> totems = optionalTrinketComponent.get().getEquipped(SpectrumItems.TOTEM_PENDANT);
				for (Pair<SlotReference, ItemStack> pair : totems) {
					if (pair.getRight().getCount() > 0) {
						// consume pendant
						pair.getRight().decrement(1);

						// Heal and add effects
						thisEntity.setHealth(1.0F);
						thisEntity.clearStatusEffects();
						thisEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
						thisEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
						thisEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
						thisEntity.getWorld().sendEntityStatus(thisEntity, (byte) 35);

						// override the previous return value
						cir.setReturnValue(true);
					}
				}
			}
		}
	}

	@Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isDead()Z", ordinal = 1))
	public void spectrum$TriggerArmorWithHitEffect(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity thisEntity = (LivingEntity) (Object) this;
		World world = thisEntity.getWorld();
		if (!world.isClient) {
			if (thisEntity instanceof MobEntity thisMobEntity) {
				for (ItemStack armorItemStack : thisMobEntity.getArmorItems()) {
					if (armorItemStack.getItem() instanceof ArmorWithHitEffect armorWithHitEffect) {
						armorWithHitEffect.onHit(armorItemStack, source, thisMobEntity, amount);
					}
				}
			} else if (thisEntity instanceof ServerPlayerEntity thisPlayerEntity) {
				for (ItemStack armorItemStack : thisPlayerEntity.getArmorItems()) {
					if (armorItemStack.getItem() instanceof ArmorWithHitEffect armorWithHitEffect) {
						armorWithHitEffect.onHit(armorItemStack, source, thisPlayerEntity, amount);
					}
				}
			}
		}
	}

	@Inject(method = "canHaveStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z", at = @At("RETURN"), cancellable = true)
	public void spectrum$canHaveStatusEffect(StatusEffectInstance statusEffectInstance, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue() && this.hasStatusEffect(SpectrumStatusEffects.IMMUNITY) && statusEffectInstance.getEffectType().getCategory() == StatusEffectCategory.HARMFUL && !SpectrumStatusEffectTags.isUncurable(statusEffectInstance.getEffectType())) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "setSprinting(Z)V", at = @At("HEAD"), cancellable = true)
	public void spectrum$setSprinting(boolean sprinting, CallbackInfo ci) {
		if (sprinting && ((LivingEntity) (Object) this).hasStatusEffect(SpectrumStatusEffects.SCARRED)) {
			ci.cancel();
		}
	}

	@Inject(at = @At("TAIL"), method = "applyFoodEffects(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)V")
	public void spectrum$eat(ItemStack stack, World world, LivingEntity targetEntity, CallbackInfo ci) {
		Item item = stack.getItem();
		if (item instanceof ApplyFoodEffectsCallback foodWithCallback) {
			foodWithCallback.afterConsumption(world, stack, (LivingEntity) (Object) this);
		}
	}

	@Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"), cancellable = true)
	public void spectrum$addStatusEffect(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
		StatusEffect effectType = effect.getEffectType();
		if (effectType instanceof StackableStatusEffect) {
			if (!SpectrumStatusEffects.effectsAreGettingStacked) {
				if (this.canHaveStatusEffect(effect)) {
					StatusEffectInstance existingInstance = getStatusEffect(effect.getEffectType());
					if (existingInstance != null) {
						SpectrumStatusEffects.effectsAreGettingStacked = true;

						int newAmplifier = 1 + existingInstance.getAmplifier() + effect.getAmplifier();
						StatusEffectInstance newInstance = new StatusEffectInstance(existingInstance.getEffectType(), existingInstance.getDuration(), newAmplifier, existingInstance.isAmbient(), existingInstance.shouldShowParticles(), existingInstance.shouldShowIcon());
						removeStatusEffect(existingInstance.getEffectType());
						addStatusEffect(newInstance);
						cir.cancel();
					}
				} else {
					SpectrumStatusEffects.effectsAreGettingStacked = false;
				}
			} else {
				SpectrumStatusEffects.effectsAreGettingStacked = false;
			}
		} else if (EffectProlongingStatusEffect.canBeExtended(effectType)) {
			StatusEffectInstance effectProlongingInstance = this.getStatusEffect(SpectrumStatusEffects.EFFECT_PROLONGING);
			if (effectProlongingInstance != null) {
				((StatusEffectInstanceAccessor) effect).setDuration(EffectProlongingStatusEffect.getExtendedDuration(effect.getDuration(), effectProlongingInstance.getAmplifier()));
			}
		}
	}

	@Inject(method = "drop(Lnet/minecraft/entity/damage/DamageSource;)V", at = @At("HEAD"), cancellable = true)
	protected void drop(DamageSource source, CallbackInfo ci) {
		LivingEntity thisEntity = (LivingEntity) (Object) this;

		if (EverpromiseRibbonComponent.hasRibbon(thisEntity)) {
			ItemStack memoryStack = MemoryItem.getMemoryForEntity(thisEntity);
			MemoryItem.setTicksToManifest(memoryStack, 20);
			MemoryItem.setSpawnAsAdult(memoryStack, true);
			MemoryItem.markAsBrokenPromise(memoryStack, true);

			Vec3d entityPos = thisEntity.getPos();
			ItemEntity itemEntity = new ItemEntity(thisEntity.getWorld(), entityPos.getX(), entityPos.getY(), entityPos.getZ(), memoryStack);
			thisEntity.getWorld().spawnEntity(itemEntity);

			ci.cancel();
		}
	}

	@Inject(method = "tick", at = @At("TAIL"))
	protected void applyInexorableEffects(CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		if (entity.getWorld() != null && entity.getWorld().getTime() % 20 == 0) {
			InexorableEnchantment.checkAndRemoveSlowdownModifiers(entity);
		}
	}
	
	@Redirect(method = "tickMovement()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isWet()Z"))
	public boolean spectrum$isWet(LivingEntity livingEntity) {
		return livingEntity.isTouchingWater() ? ((TouchingWaterAware) livingEntity).spectrum$isActuallyTouchingWater() : livingEntity.isWet();
	}
}