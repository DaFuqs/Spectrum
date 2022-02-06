package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import de.dafuqs.spectrum.registries.SpectrumItems;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Shadow @Nullable protected PlayerEntity attackingPlayer;
	
	@Shadow @Final private DefaultedList<ItemStack> syncedArmorStacks;
	
	@Shadow public abstract void pushAwayFrom(Entity entity);
	
	@ModifyArg(method = "dropXp()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ExperienceOrbEntity;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;I)V"), index = 2)
	protected int spectrum$applyExuberance(int originalXP) {
		return (int) (originalXP * spectrum$getExuberanceMod(this.attackingPlayer));
	}

	private float spectrum$getExuberanceMod(PlayerEntity attackingPlayer) {
		if(attackingPlayer != null && SpectrumEnchantments.EXUBERANCE.canEntityUse(attackingPlayer)) {
			int exuberanceLevel = EnchantmentHelper.getEquipmentLevel(SpectrumEnchantments.EXUBERANCE, attackingPlayer);
			return 1.0F + exuberanceLevel * SpectrumCommon.CONFIG.ExuberanceBonusExperiencePercentPerLevel;
		} else {
			return 1.0F;
		}
	}

	@Inject(at = @At("RETURN"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	public void spectrum$applyDisarmingEnchantment(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		// true if the entity got hurt
		if(cir.getReturnValue() != null && cir.getReturnValue()) {
			if(source.getAttacker() instanceof LivingEntity livingSource && SpectrumEnchantments.DISARMING.canEntityUse(livingSource)) {
				int disarmingLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.DISARMING, livingSource.getMainHandStack());
				if(disarmingLevel > 0 &&  Math.random() < disarmingLevel * SpectrumCommon.CONFIG.DisarmingChancePerLevelMobs) {
					LivingEntity thisEntity = (LivingEntity)(Object) this;

					int randomSlot = (int) (Math.random() * 6);
					int slotsChecked = 0;
					while (slotsChecked < 6) {
						if(randomSlot == 5) {
							if(thisEntity.getMainHandStack() != null) {
								thisEntity.dropStack(thisEntity.getMainHandStack());
								thisEntity.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
								break;
							}
						} else if(randomSlot == 4) {
							if(thisEntity.getOffHandStack() != null) {
								thisEntity.dropStack(thisEntity.getOffHandStack());
								thisEntity.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
								break;
							}
						} else {
							if(!this.syncedArmorStacks.get(randomSlot).isEmpty()) {
								thisEntity.dropStack(this.syncedArmorStacks.get(randomSlot));
								this.syncedArmorStacks.set(randomSlot, ItemStack.EMPTY);
								break;
							}
						}

						randomSlot = (randomSlot + 1) % 6;
						slotsChecked++;
					}
				}
			}
		}
	}
	
	@Inject(at = @At("RETURN"), method = "tryUseTotem(Lnet/minecraft/entity/damage/DamageSource;)Z", cancellable = true)
	public void spectrum$checkForTotemPendant(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if(!cir.getReturnValue()) {
			// if no other totem triggered: check for a totem pendant
			LivingEntity thisEntity = (LivingEntity) (Object) this;
			Optional<TrinketComponent> optionalTrinketComponent = TrinketsApi.getTrinketComponent(thisEntity);
			if(optionalTrinketComponent.isPresent()) {
				List<Pair<SlotReference, ItemStack>> totems = optionalTrinketComponent.get().getEquipped(SpectrumItems.TOTEM_PENDANT);
				for(Pair<SlotReference, ItemStack> pair : totems) {
					if(pair.getRight().getCount() > 0) {
						// consume pendant
						pair.getRight().decrement(1);
						
						// Heal and add effects
						thisEntity.setHealth(1.0F);
						thisEntity.clearStatusEffects();
						thisEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
						thisEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
						thisEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
						thisEntity.world.sendEntityStatus(thisEntity, (byte)35);
						
						// override the previous return value
						cir.setReturnValue(true);
					}
				}
			}
		}
	}

}