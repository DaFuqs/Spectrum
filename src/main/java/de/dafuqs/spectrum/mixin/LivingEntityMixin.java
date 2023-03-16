package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.cca.azure_dike.*;
import de.dafuqs.spectrum.enchantments.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.items.armor.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import dev.emi.trinkets.api.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
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
	public abstract boolean removeStatusEffect(StatusEffect type);

	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);

	@Shadow
	protected ItemStack activeItemStack;

	@Shadow
	public abstract boolean damage(DamageSource source, float amount);

	@ModifyArg(method = "dropXp()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ExperienceOrbEntity;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;I)V"), index = 2)
	protected int spectrum$applyExuberance(int originalXP) {
		return (int) (originalXP * spectrum$getExuberanceMod(this.attackingPlayer));
	}

	private float spectrum$getExuberanceMod(PlayerEntity attackingPlayer) {
		if (attackingPlayer != null && SpectrumEnchantments.EXUBERANCE.canEntityUse(attackingPlayer)) {
			int exuberanceLevel = EnchantmentHelper.getEquipmentLevel(SpectrumEnchantments.EXUBERANCE, attackingPlayer);
			return 1.0F + exuberanceLevel * SpectrumCommon.CONFIG.ExuberanceBonusExperiencePercentPerLevel;
		} else {
			return 1.0F;
		}
	}
	
	@Inject(at = @At("HEAD"), method = "fall(DZLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V")
	public void spectrum$mitigateFallDamageWithPuffCirclet(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition, CallbackInfo ci) {
		if (onGround) {
			LivingEntity thisEntity = (LivingEntity) (Object) this;
			if (thisEntity.fallDistance > thisEntity.getSafeFallDistance()) {
				Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(thisEntity);
				if (component.isPresent()) {
					if (!component.get().getEquipped(SpectrumItems.PUFF_CIRCLET).isEmpty()) {
						int charges = AzureDikeProvider.getAzureDikeCharges(thisEntity);
						if (charges > 0) {
							AzureDikeProvider.absorbDamage(thisEntity, PuffCircletItem.FALL_DAMAGE_NEGATING_COST);

							thisEntity.fallDistance = 0;
							thisEntity.setVelocity(thisEntity.getVelocity().x, 0.5, thisEntity.getVelocity().z);
							if (thisEntity.world.isClient) { // it is split here so the particles spawn immediately, without network lag
								ParticleHelper.playParticleWithPatternAndVelocityClient(thisEntity.getWorld(), thisEntity.getPos(), SpectrumParticleTypes.WHITE_CRAFTING, ParticlePattern.EIGHT, 0.4);
								ParticleHelper.playParticleWithPatternAndVelocityClient(thisEntity.getWorld(), thisEntity.getPos(), SpectrumParticleTypes.BLUE_CRAFTING, ParticlePattern.EIGHT_OFFSET, 0.5);
							} else if (thisEntity instanceof ServerPlayerEntity serverPlayerEntity) {
								SpectrumS2CPacketSender.playParticleWithPatternAndVelocity(serverPlayerEntity, (ServerWorld) thisEntity.getWorld(), thisEntity.getPos(), SpectrumParticleTypes.WHITE_CRAFTING, ParticlePattern.EIGHT, 0.4);
								SpectrumS2CPacketSender.playParticleWithPatternAndVelocity(serverPlayerEntity, (ServerWorld) thisEntity.getWorld(), thisEntity.getPos(), SpectrumParticleTypes.BLUE_CRAFTING, ParticlePattern.EIGHT_OFFSET, 0.5);
							}
							thisEntity.getWorld().playSound(null, thisEntity.getBlockPos(), SpectrumSoundEvents.PUFF_CIRCLET_PFFT, SoundCategory.PLAYERS, 1.0F, 1.0F);
						}
					}
				}
			}
		}
	}
	
	@ModifyVariable(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", argsOnly = true)
	public float spectrum$applyAzureDikeDamageProtection(float amount, DamageSource source) {
		@Nullable StatusEffectInstance vulnerability = getStatusEffect(SpectrumStatusEffects.VULNERABILITY);
		if (vulnerability != null) {
			amount *= 1 + (SpectrumStatusEffects.VULNERABILITY_ADDITIONAL_DAMAGE_PERCENT_PER_LEVEL * vulnerability.getAmplifier());
		}
		
		if (source.isOutOfWorld() || source.isUnblockable() || this.blockedByShield(source) || amount <= 0 || ((Entity) (Object) this).isInvulnerableTo(source) || source.isFire() && hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
			return amount;
		}
		
		return AzureDikeProvider.absorbDamage((LivingEntity) (Object) this, amount);
	}
	
	@Inject(at = @At("RETURN"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	public void spectrum$applyDisarmingEnchantment(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		// true if the entity got hurt
		if (cir.getReturnValue() != null && cir.getReturnValue()) {
			if (source.getAttacker() instanceof LivingEntity livingSource && SpectrumEnchantments.DISARMING.canEntityUse(livingSource)) {
				int disarmingLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.DISARMING, livingSource.getMainHandStack());
				if (disarmingLevel > 0 && Math.random() < disarmingLevel * SpectrumCommon.CONFIG.DisarmingChancePerLevelMobs) {
					DisarmingEnchantment.disarmEntity((LivingEntity) (Object) this, this.syncedArmorStacks);
				}
			}
		}
	}

	@Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"))
	public void spectrum$applyBonusDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity target = (LivingEntity) (Object) this;

		// SetHealth damage does exactly that
		if (amount > 0 && source instanceof SpectrumDamageSources.SetHealthDamageSource) {
			float h = target.getHealth();
			target.setHealth(h - amount);
			target.getDamageTracker().onDamage(source, h, amount);
			if (target.isDead()) {
				target.onDeath(source);
			}
			return;
		}

		// If this entity is hit with a SplitDamageItem, damage() gets called recursively for each type of damage dealt
		if (!SpectrumDamageSources.recursiveDamage && amount > 0 && source instanceof EntityDamageSource && source.getSource() instanceof LivingEntity livingSource) {
			ItemStack mainHandStack = livingSource.getMainHandStack();
			if (mainHandStack.getItem() instanceof SplitDamageItem splitDamageItem) {
				SpectrumDamageSources.recursiveDamage = true;
				SplitDamageItem.DamageComposition composition = splitDamageItem.getDamageComposition(livingSource, target, activeItemStack, amount);

				for (Pair<DamageSource, Float> entry : composition.get()) {
					damage(entry.getLeft(), entry.getRight());
				}

				SpectrumDamageSources.recursiveDamage = false;
				return;
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
						thisEntity.world.sendEntityStatus(thisEntity, (byte) 35);
						
						// override the previous return value
						cir.setReturnValue(true);
					}
				}
			}
		}
	}
	
	@Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isDead()Z", ordinal = 1))
	public void spectrum$TriggerArmorWithHitEffect(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (!((LivingEntity) (Object) this).world.isClient) {
			if (((Object) this) instanceof MobEntity thisMobEntity) {
				for (ItemStack armorItemStack : thisMobEntity.getArmorItems()) {
					if (armorItemStack.getItem() instanceof ArmorWithHitEffect) {
						((ArmorWithHitEffect) armorItemStack.getItem()).onHit(armorItemStack, source, thisMobEntity, amount);
					}
				}
			} else if (((Object) this) instanceof ServerPlayerEntity thisPlayerEntity) {
				for (ItemStack armorItemStack : thisPlayerEntity.getArmorItems()) {
					if (armorItemStack.getItem() instanceof ArmorWithHitEffect) {
						((ArmorWithHitEffect) armorItemStack.getItem()).onHit(armorItemStack, source, thisPlayerEntity, amount);
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
	public void spectrum$addStackableStatusEffect(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
		if (effect.getEffectType() instanceof StackableStatusEffect) {
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
		}
	}
	
}