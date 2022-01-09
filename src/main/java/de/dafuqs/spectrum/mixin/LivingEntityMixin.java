package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Shadow @Nullable protected PlayerEntity attackingPlayer;
	
	@Shadow public abstract void swingHand(Hand hand);
	
	@Shadow protected abstract ItemStack getSyncedArmorStack(EquipmentSlot slot);
	
	@Shadow public abstract Iterable<ItemStack> getArmorItems();
	
	@Shadow @Final private DefaultedList<ItemStack> syncedArmorStacks;
	
	@ModifyArg(method = "dropXp()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ExperienceOrbEntity;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;I)V"), index = 2)
	protected int applyExuberance(int originalXP) {
		return (int) (originalXP * getExuberanceMod(this.attackingPlayer));
	}

	private float getExuberanceMod(PlayerEntity attackingPlayer) {
		if(attackingPlayer != null && SpectrumEnchantments.EXUBERANCE.canEntityUse(attackingPlayer)) {
			int exuberanceLevel = EnchantmentHelper.getEquipmentLevel(SpectrumEnchantments.EXUBERANCE, attackingPlayer);
			return 1.0F + exuberanceLevel * SpectrumCommon.CONFIG.ExuberanceBonusExperiencePercentPerLevel;
		} else {
			return 1.0F;
		}
	}

	@Inject(at = @At("RETURN"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	public void applyDisarmingEnchantment(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
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

}