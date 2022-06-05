package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({LivingEntity.class, PlayerEntity.class})
public class FirstStrikeEnchantmentMixin {
	
	@ModifyVariable(method = "applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	public float applyAdditionalFirstStrikeEnchantmentDamage(float amount, DamageSource source) {
		LivingEntity target = (LivingEntity) (Object) this;
		
		if (source.getAttacker() instanceof LivingEntity livingAttacker) {
			if (amount != 0F && target.getHealth() == target.getMaxHealth()) {
				ItemStack mainHandStack = livingAttacker.getMainHandStack();
				int level = EnchantmentHelper.getLevel(SpectrumEnchantments.FIRST_STRIKE, mainHandStack);
				if (level > 0 && SpectrumEnchantments.FIRST_STRIKE.canEntityUse(livingAttacker)) {
					float additionalDamage = getAdditionalFirstStrikeEnchantmentDamage(level);
					amount += additionalDamage;
				}
			}
		}
		return amount;
	}
	
	private float getAdditionalFirstStrikeEnchantmentDamage(int level) {
		return SpectrumCommon.CONFIG.FirstStrikeDamagePerLevel * level;
	}
	
}