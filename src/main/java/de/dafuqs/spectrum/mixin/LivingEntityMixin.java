package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.*;
import com.llamalad7.mixinextras.sugar.ref.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.damage_type.*;
import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.status_effect.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.cca.azure_dike.*;
import de.dafuqs.spectrum.enchantments.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.items.tools.*;
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
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
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
	public abstract boolean hasStatusEffect(StatusEffect effect);

	@Shadow
	public abstract ItemStack getMainHandStack();

	@Shadow
	@Nullable
	public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

	@Shadow
	public abstract boolean canHaveStatusEffect(StatusEffectInstance effect);

	@Shadow
	public abstract void readCustomDataFromNbt(NbtCompound nbt);
	
	@Shadow
	public abstract boolean damage(DamageSource source, float amount);
	
	@Shadow
	public abstract boolean removeStatusEffect(StatusEffect type);
	
	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);
	
	@Shadow
	public abstract ItemStack getOffHandStack();
	
	@Shadow
	public abstract int getArmor();
	
	@Shadow
	public abstract double getAttributeValue(EntityAttribute attribute);

	@Shadow public abstract void remove(Entity.RemovalReason reason);
	
	@Shadow
	public abstract void travel(Vec3d movementInput);
	
	// FabricDefaultAttributeRegistry seems to only allow adding full containers and only single entity types?
	@Inject(method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;", require = 1, allow = 1, at = @At("RETURN"))
	private static void spectrum$addAttributes(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue().add(SpectrumEntityAttributes.INDUCED_SLEEP_RESISTANCE);
	}
	
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
	public void spectrum$travel(CallbackInfo ci, @Local(ordinal = 1) LocalFloatRef f) {
		var talon = (DragonTalonItem) SpectrumItems.DRAGON_TALON;
		var entity = (LivingEntity) (Object) this;
		var friction = -1F;

		if (talon.isReservingSlot(this.getMainHandStack()) || talon.isReservingSlot(this.getOffHandStack())) {
			if (!(entity).isOnGround()) {
				friction = 0.945F;
			}
		}
		
		if (!entity.isOnGround()) {
			var optionalTrinket = SpectrumTrinketItem.getFirstEquipped((LivingEntity) (Object) this, SpectrumItems.RING_OF_AERIAL_GRACE);
			if (optionalTrinket.isPresent()) {
				var inkStorage = SpectrumItems.RING_OF_AERIAL_GRACE.getEnergyStorage(optionalTrinket.get());
				var storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
				friction = (float) Math.max(friction, 0.91 + (((RingOfAerialGraceItem) SpectrumItems.RING_OF_AERIAL_GRACE).getBonus(storedInk) / 150F));
			}
		}

		if (friction >= 0)
			f.set(friction);
	}

	@ModifyExpressionValue(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getSlipperiness()F"))
	public float spectrum$increaseSlipperiness(float original) {
		var entity = (LivingEntity) (Object) this;
		var random = entity.getRandom();
		var potency = SleepStatusEffect.getGeneralSleepResistanceIfEntityHasSoporificEffect(entity);
		if (potency != -1) {

			if (entity instanceof PlayerEntity && random.nextFloat() < potency * 0.0334) {
				return 0.35F + random.nextFloat() * 0.45F;
			}

			original = (float) Math.min(original + 0.3 + (potency / 25F), 0.9975F);
		}
		return original;
	}

	@ModifyReturnValue(method = "canWalkOnFluid", at = @At("RETURN"))
	public boolean spectrum$modifyFluidWalking(boolean original) {
		var entity = (LivingEntity) (Object) this;
		
		if (SpectrumTrinketItem.hasEquipped((LivingEntity) (Object) this, SpectrumItems.RING_OF_AERIAL_GRACE))
			return !entity.isSubmergedInWater();

		return original;
	}

	@Inject(method = "applyFoodEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getFoodComponent()Lnet/minecraft/item/FoodComponent;"))
	private void spectrum$applyConcealedEffects(ItemStack stack, World world, LivingEntity targetEntity, CallbackInfo ci) {
		if (!world.isClient() && stack.hasNbt() && stack.getNbt().contains(ConcealingOilsItem.OIL_EFFECT_ID)) {
			var nbt = stack.getNbt().getCompound(ConcealingOilsItem.OIL_EFFECT_ID);
			var instance = StatusEffectInstance.fromNbt(nbt);
			targetEntity.addStatusEffect(instance);
		}
	}

	@ModifyReturnValue(method = "canHaveStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z", at = @At("RETURN"))
	public boolean spectrum$canHaveStatusEffect(boolean original, @Local(argsOnly = true) StatusEffectInstance statusEffectInstance) {
		var instance = (LivingEntity) (Object) this;

		if (original && this.hasStatusEffect(SpectrumStatusEffects.IMMUNITY) && statusEffectInstance.getEffectType().getCategory() == StatusEffectCategory.HARMFUL && !SpectrumStatusEffectTags.isIncurable(statusEffectInstance.getEffectType())) {
			if (Incurable.isIncurable(statusEffectInstance)) {
				var immunity = getStatusEffect(SpectrumStatusEffects.IMMUNITY);
				var cost = 600 * (statusEffectInstance.getAmplifier() + 1);

				if (immunity.getDuration() >= cost) {
					((StatusEffectInstanceAccessor) immunity).setDuration(Math.max(5, immunity.getDuration() - cost));
					if (!instance.getWorld().isClient()) {
						((ServerWorld) instance.getWorld()).getChunkManager().sendToNearbyPlayers(instance, new EntityStatusEffectS2CPacket(instance.getId(), immunity));
					}
					return false;
				}
				else {
					return true;
				}
			}

			return false;
		}
		return original;
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
	
	@ModifyArg(method = "modifyAppliedDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getInflictedDamage(FF)F"), index = 1)
	public float spectrum$modifyAppliedDamage(float protection, @Local(argsOnly = true) DamageSource source) {
		var pair = getArmorPiercing(source);
		
		if (pair.isPresent()) {
			var ap = pair.get().getLeft();
			var stack = pair.get().getRight();
			
			var modProt = Math.max(protection, 20F) / 25F;
			protection = Math.max(modProt - ap.getProtReduction((LivingEntity) (Object) this, stack), 0) * 20F;
		}
		
		return protection;
	}
	
	@ModifyVariable(method = "applyArmorToDamage", at = @At("STORE"))
	public float spectrum$applyArmorToDamage(float amount, DamageSource source) {
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
		
		if (source.isIn(SpectrumDamageTypeTags.CALCULATES_DAMAGE_BASED_ON_TOUGHNESS)) {
			amount = DamageUtil.getDamageLeft(amount, toughness * 1.334F, Float.MAX_VALUE);
		} else if (source.isIn(SpectrumDamageTypeTags.PARTLY_IGNORES_PROTECTION)) {
			amount = DamageUtil.getDamageLeft(amount, defense / 2, toughness);
		}
		
		if (modified) {
			amount = DamageUtil.getDamageLeft(amount, defense, toughness);
		}
		
		return amount;
	}
	
	@Unique
	private Optional<Pair<ArmorPiercingItem, ItemStack>> getArmorPiercing(DamageSource source) {
		if (!(source instanceof StackTracking stackTracking))
			return Optional.empty();
		
		var stackOptional = stackTracking.spectrum$getTrackedStack();
		
		if (stackOptional.isEmpty())
			return Optional.empty();
		
		var stack = stackOptional.get();
		
		if (!(stack.getItem() instanceof ArmorPiercingItem ap))
			return Optional.empty();
		
		return Optional.of(new Pair<>(ap, stack));
	}
	
	@Unique
	private float getToughness() {
		return (float) this.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
	}

	@Inject(at = @At("HEAD"), method = "fall(DZLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V")
	public void spectrum$fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition, CallbackInfo ci) {
		if (onGround) {
			LivingEntity thisEntity = (LivingEntity) (Object) this;
			if (!thisEntity.isInvulnerableTo(thisEntity.getDamageSources().fall()) && thisEntity.fallDistance > thisEntity.getSafeFallDistance()) {
				Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(thisEntity);
				if (component.isPresent()) {
					if (!component.get().getEquipped(SpectrumItems.PUFF_CIRCLET).isEmpty()) {
						var charges = AzureDikeProvider.getAzureDikeCharges(thisEntity);
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
		return amount;
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V", ordinal = 0), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	public void spectrum$applyDike1(LivingEntity instance, DamageSource source, float amount, Operation<Void> original) {
		if (source.isIn(SpectrumDamageTypeTags.BYPASSES_DIKE)) {
			original.call(instance, source, amount);
			return;
		}
		instance.applyDamage(source, AzureDikeProvider.absorbDamage(instance, amount));
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V", ordinal = 1), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	public void spectrum$applyDike2(LivingEntity instance, DamageSource source, float amount, Operation<Void> original) {
		if (source.isIn(SpectrumDamageTypeTags.BYPASSES_DIKE)) {
			original.call(instance, source, amount);
			return;
		}
		instance.applyDamage(source, AzureDikeProvider.absorbDamage(instance, amount));
	}

	@Inject(method = "tickStatusEffects", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;remove()V"))
	public void spectrum$fatalSlumberKill(CallbackInfo ci, @Local StatusEffectInstance effectInstance) {
		if (effectInstance.getEffectType() == SpectrumStatusEffects.FATAL_SLUMBER) {
			var entity = (LivingEntity) (Object) this;

			if (entity.getWorld().isClient())
				return;

			var damage = Float.MAX_VALUE;
			if (SleepStatusEffect.isImmuneish(entity)) {
				if (entity instanceof PlayerEntity player) {
					damage = entity.getHealth() * 0.95F;
					Support.grantAdvancementCriterion((ServerPlayerEntity) player, "lategame/survive_fatal_slumber", "get_slumbered_idiot");
				}
				else {
					damage = entity.getMaxHealth() * 0.3F;
				}
			}
			entity.damage(SpectrumDamageTypes.sleep(entity.getWorld(), null), damage);
		}
	}

	/**
	 * We do not force player sleeping because that would do funny things to the sleep cycle
	 */
	@ModifyReturnValue(method = "isSleeping", at = @At("RETURN"))
	public boolean spectrum$forceSleepingState(boolean original) {
		if (original)
			return true;

		if (hasStatusEffect(SpectrumStatusEffects.ETERNAL_SLUMBER) || hasStatusEffect(SpectrumStatusEffects.FATAL_SLUMBER))
			return !(((LivingEntity) (Object) this) instanceof PlayerEntity);

		return false;
	}

	@Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"))
	public void spectrum$modifySlumberEffectLengths(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
		var entity = (LivingEntity) (Object) this;
		var resistanceModifier = SleepStatusEffect.getSleepResistanceModifier(effect, entity);
		if (effect.getEffectType() == SpectrumStatusEffects.ETERNAL_SLUMBER) {
			if (SleepStatusEffect.isImmuneish(entity)) {
				((StatusEffectInstanceAccessor) effect).setDuration(Math.round(effect.getDuration() / resistanceModifier));
			} else if (!entity.getType().isIn(SpectrumEntityTypeTags.SLEEP_RESISTANT)) {
				((StatusEffectInstanceAccessor) effect).setDuration(-1); // StatusEffectInstance.INFINITE = -1
			}
		} else if (effect.getEffectType() == SpectrumStatusEffects.FATAL_SLUMBER) {
			((StatusEffectInstanceAccessor) effect).setDuration(Math.max(Math.round(effect.getDuration() * resistanceModifier * 2), 100));
		}
	}

	@Inject(at = @At("RETURN"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	public void spectrum$applyDisarmingEnchantment(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		// true if the entity got hurt
		if (amount > 0 && cir.getReturnValue() != null && cir.getReturnValue()) {
			// Disarming does not trigger when dealing damage to enemies using thorns
			if (!source.isOf(DamageTypes.THORNS)) {
				if (source.getAttacker() instanceof LivingEntity livingSource && SpectrumEnchantments.DISARMING.canEntityUse(livingSource)) {
					
					int disarmingLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.DISARMING, livingSource.getMainHandStack());
					if (disarmingLevel > 0 && Math.random() < disarmingLevel * SpectrumCommon.CONFIG.DisarmingChancePerLevelMobs) {
						DisarmingEnchantment.disarmEntity((LivingEntity) (Object) this);
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
			cir.setReturnValue(true);
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
	
	@ModifyVariable(method = "setSprinting(Z)V", at = @At("HEAD"), argsOnly = true)
	public boolean spectrum$setSprinting(boolean sprinting) {
		if (sprinting && ((LivingEntity) (Object) this).hasStatusEffect(SpectrumStatusEffects.SCARRED)) {
			return false;
		}
		return sprinting;
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