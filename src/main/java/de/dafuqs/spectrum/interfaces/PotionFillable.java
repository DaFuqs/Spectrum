package de.dafuqs.spectrum.interfaces;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;

import java.util.List;

/**
 * Items with this interface should support Potion nbt like
 * set via PotionUtil.setCustomPotionEffects
 * Think Custom Potions or Trinkets
 */
public interface PotionFillable {
	
	int maxEffectCount();
	
	int maxEffectAmplifier();
	
	default void addEffects(ItemStack potionFillableStack, List<StatusEffectInstance> newEffects) {
		if (!isFull(potionFillableStack)) {
			List<StatusEffectInstance> existingEffects = PotionUtil.getCustomPotionEffects(potionFillableStack);
			int max = maxEffectCount();
			int maxAmplifier = maxEffectAmplifier();
			for (StatusEffectInstance newEffect : newEffects) {
				if (newEffect.getAmplifier() > maxAmplifier) {
					newEffect = new StatusEffectInstance(newEffect.getEffectType(), newEffect.getDuration(), maxAmplifier, newEffect.isAmbient(), newEffect.shouldShowParticles());
				}
				existingEffects.add(newEffect);
				if (existingEffects.size() == max) {
					break;
				}
			}
			
			PotionUtil.setCustomPotionEffects(potionFillableStack, existingEffects);
		}
	}
	
	default boolean isFull(ItemStack itemStack) {
		return PotionUtil.getCustomPotionEffects(itemStack).size() >= maxEffectCount();
	}
	
}