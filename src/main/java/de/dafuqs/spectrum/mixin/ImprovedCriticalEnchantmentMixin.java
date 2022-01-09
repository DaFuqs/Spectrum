package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PlayerEntity.class)
public class ImprovedCriticalEnchantmentMixin {

	/**
	 * By default, the additional crit damage is a flat *1.5
	 */
	@ModifyConstant(method = "attack(Lnet/minecraft/entity/Entity;)V", constant = @Constant(floatValue = 1.5F))
	public float applyImprovedCriticalEnchantmentDamage(float critModifier) {
		PlayerEntity thisEntity = (PlayerEntity)(Object) this;
		if(SpectrumEnchantments.IMPROVED_CRITICAL.canEntityUse(thisEntity)) {
			return getCritMultiplier(thisEntity);
		} else {
			return critModifier;
		}
	}

	private float getCritMultiplier(@NotNull LivingEntity livingEntity) {
		int critMultiplierLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.IMPROVED_CRITICAL, livingEntity.getMainHandStack());
		return 1.5F + SpectrumCommon.CONFIG.ImprovedCriticalExtraDamageMultiplierPerLevel * critMultiplierLevel;
	}

}