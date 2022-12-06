package de.dafuqs.spectrum.interfaces;

import de.dafuqs.spectrum.helpers.TooltipHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

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
	
	default boolean isAtLeastPartiallyFilled(ItemStack itemStack) {
		return PotionUtil.getCustomPotionEffects(itemStack).size() > 0;
	}
	
	default void removeEffects(ItemStack itemStack) {
		PotionUtil.setPotion(itemStack, Potions.EMPTY);
	}
	
	default void appendPotionFillableTooltip(ItemStack stack, List<Text> tooltip, MutableText attributeModifierText) {
		List<StatusEffectInstance> effects = PotionUtil.getCustomPotionEffects(stack);
		TooltipHelper.buildEffectTooltip(tooltip, effects, attributeModifierText);
		
		int maxEffectCount = maxEffectCount();
		if (effects.size() < maxEffectCount) {
			if (maxEffectCount == 1) {
				tooltip.add(Text.translatable("item.spectrum.potion_pendant.tooltip_not_full_one"));
			} else {
				tooltip.add(Text.translatable("item.spectrum.potion_pendant.tooltip_not_full_count", maxEffectCount));
			}
			tooltip.add(Text.translatable("item.spectrum.potion_pendant.tooltip_max_level").append(Text.translatable("enchantment.level." + (maxEffectAmplifier() + 1))));
		}
	}
	
}