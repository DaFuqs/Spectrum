package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({LivingEntity.class, PlayerEntity.class})
public class FirstStrikeEnchantmentMixin {

	@ModifyVariable(method = "applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V", at = @At("HEAD"), ordinal = 0)
	public float applyAdditionalFirstStrikeEnchantmentDamage(float amount, DamageSource source) {
		LivingEntity user = (LivingEntity) source.getAttacker();
		LivingEntity target = (LivingEntity) (Object) this;

		if (amount != 0.0F && source.getSource() instanceof LivingEntity && user != null && target.getHealth() == target.getMaxHealth()) {
			ItemStack mainHandStack = user.getMainHandStack();
			int level = EnchantmentHelper.getLevel(SpectrumEnchantments.FIRST_STRIKE, mainHandStack);
			if(level > 0) {
				float additionalDamage = getAdditionalFirstStrikeEnchantmentDamage(user);
				amount += additionalDamage;
			}
		}
		return amount;
	}

	private float getAdditionalFirstStrikeEnchantmentDamage(@NotNull LivingEntity livingEntity) {
		int firstStrikeLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.FIRST_STRIKE, livingEntity.getMainHandStack());
		return SpectrumCommon.CONFIG.FirstStrikeDamagePerLevel * firstStrikeLevel;
	}

}