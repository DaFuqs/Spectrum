package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({LivingEntity.class, Player.class})
public abstract class FirstStrikeEnchantmentMixin {
	
	@ModifyVariable(method = "actuallyHurt", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	public float applyAdditionalFirstStrikeEnchantmentDamage(float amount, DamageSource source) {
		LivingEntity target = (LivingEntity) (Object) this;
		
		if (source.getEntity() instanceof LivingEntity livingAttacker) {
			if (amount != 0F && target.getHealth() == target.getMaxHealth()) {
				ItemStack mainHandStack = livingAttacker.getMainHandItem();
				int level = SpectrumEnchantmentHelper.getLevel(livingAttacker.level().registryAccess(), SpectrumEnchantments.FIRST_STRIKE, mainHandStack);
				if (level > 0) {
					float additionalDamage = getAdditionalFirstStrikeEnchantmentDamage(level);
					amount += additionalDamage;
				}
			}
		}
		return amount;
	}
	
	@Unique
	private float getAdditionalFirstStrikeEnchantmentDamage(int level) {
		return SpectrumCommon.CONFIG.FirstStrikeDamagePerLevel * level;
	}
	
}