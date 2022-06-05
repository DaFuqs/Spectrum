package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.azure_dike.AzureDikeProvider;
import de.dafuqs.spectrum.enchantments.DisarmingEnchantment;
import de.dafuqs.spectrum.interfaces.ArmorWithHitEffect;
import de.dafuqs.spectrum.items.trinkets.PuffCircletItem;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketReceiver;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.ParticlePattern;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import de.dafuqs.spectrum.registries.SpectrumItems;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

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
								SpectrumS2CPacketReceiver.playParticleWithPatternAndVelocityClient(thisEntity.getWorld(), thisEntity.getPos(), SpectrumParticleTypes.WHITE_CRAFTING, ParticlePattern.EIGHT, 0.4);
								SpectrumS2CPacketReceiver.playParticleWithPatternAndVelocityClient(thisEntity.getWorld(), thisEntity.getPos(), SpectrumParticleTypes.BLUE_CRAFTING, ParticlePattern.EIGHT_OFFSET, 0.5);
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
		if (source.isOutOfWorld() || source.isUnblockable() || amount <= 0 || ((Entity) (Object) this).isInvulnerableTo(source) || source.isFire() && hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
			return amount;
		} else {
			return AzureDikeProvider.absorbDamage((LivingEntity) (Object) this, amount);
		}
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
			if (((Object) this) instanceof MobEntity) {
				MobEntity thisMobEntity = (MobEntity) (Object) this;
				for (ItemStack armorItemStack : thisMobEntity.getArmorItems()) {
					if (armorItemStack.getItem() instanceof ArmorWithHitEffect) {
						((ArmorWithHitEffect) armorItemStack.getItem()).onHit(armorItemStack, source, thisMobEntity, amount);
					}
				}
			} else if (((Object) this) instanceof ServerPlayerEntity) {
				ServerPlayerEntity thisPlayerEntity = (ServerPlayerEntity) (Object) this;
				for (ItemStack armorItemStack : thisPlayerEntity.getArmorItems()) {
					if (armorItemStack.getItem() instanceof ArmorWithHitEffect) {
						((ArmorWithHitEffect) armorItemStack.getItem()).onHit(armorItemStack, source, thisPlayerEntity, amount);
					}
				}
			}
		}
	}
	
	@Inject(method = "canHaveStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z", at = @At("RETURN"), cancellable = true)
	public void canHaveStatusEffect(StatusEffectInstance statusEffectInstance, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue() && this.hasStatusEffect(SpectrumStatusEffects.IMMUNITY) && !SpectrumStatusEffects.isUncurable(statusEffectInstance.getEffectType())) {
			cir.setReturnValue(false);
		}
	}
	
}