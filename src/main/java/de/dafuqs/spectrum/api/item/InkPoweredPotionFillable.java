package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.api.ink.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.*;

import java.util.*;

public interface InkPoweredPotionFillable {
	
	int maxEffectCount();
	int maxEffectAmplifier();
	
	// used for calculating the items amount to apply a certain effect
	// calculated once and then stored in the items nbt for quick lookup and nicer modifiability
	// via commands or special loot (so ones found in dungeon chests can be cheaper!)
	default long adjustFinalCostFor(InkPoweredMobEffectInstance instance) {
		return (long) Math.pow(instance.getInkCost().amount(), 1 + instance.getStatusEffectInstance().getAmplifier());
	}
	
	// saving
	default void addOrUpgradeEffects(ItemStack potionFillableStack, List<InkPoweredMobEffectInstance> newEffects, Optional<Integer> color, boolean unidentifiable) {
		if (!isFull(potionFillableStack)) {
			// by default, values are immutable, so we need to copy the values to an arraylist to be able to add stuff to it
			List<InkPoweredMobEffectInstance> existingEffects = new ArrayList<>(InkPoweredPotionContentsComponent.getEffects(potionFillableStack));
			int maxCount = maxEffectCount();
			int maxAmplifier = maxEffectAmplifier();
			for (InkPoweredMobEffectInstance newEffect : newEffects) {
				MobEffectInstance statusEffectInstance = newEffect.getStatusEffectInstance();
				if (statusEffectInstance.getAmplifier() > maxAmplifier) {
					statusEffectInstance = new MobEffectInstance(statusEffectInstance.getEffect(), statusEffectInstance.getDuration(), maxAmplifier, statusEffectInstance.isAmbient(), statusEffectInstance.isVisible());
				}
				if (existingEffects.size() == maxCount) {
					break;
				}
				
				// calculate the final amount of this effect and add it
				InkAmount adjustedCost = new InkAmount(newEffect.getInkCost().color(), adjustFinalCostFor(newEffect));
				InkPoweredMobEffectInstance modifiedInstance = new InkPoweredMobEffectInstance(statusEffectInstance, adjustedCost, color, unidentifiable);
				existingEffects.add(modifiedInstance);
			}
			
			InkPoweredPotionContentsComponent.setEffects(potionFillableStack, existingEffects);
		}
	}
	
	default boolean isFull(ItemStack itemStack) {
		return InkPoweredPotionContentsComponent.getEffects(itemStack).size() >= maxEffectCount();
	}
	
	default boolean isAtLeastPartiallyFilled(ItemStack itemStack) {
		return !InkPoweredPotionContentsComponent.getEffects(itemStack).isEmpty();
	}
	
	default void appendPotionFillableTooltip(ItemStack stack, List<Component> tooltip, MutableComponent attributeModifierText, boolean showDuration, float tickRate) {
		List<InkPoweredMobEffectInstance> effects = InkPoweredPotionContentsComponent.getEffects(stack);
		InkPoweredMobEffectInstance.buildTooltip(tooltip, effects, attributeModifierText, showDuration, tickRate);
		
		int maxEffectCount = maxEffectCount();
		if (effects.size() < maxEffectCount) {
			if (maxEffectCount == 1) {
				tooltip.add(Component.translatable("item.spectrum.potion_pendant.tooltip_not_full_one"));
			} else {
				tooltip.add(Component.translatable("item.spectrum.potion_pendant.tooltip_not_full_count", maxEffectCount));
			}
			tooltip.add(Component.translatable("item.spectrum.potion_pendant.tooltip_max_level").append(Component.translatable("enchantment.level." + (maxEffectAmplifier() + 1))));
		}
	}

	default boolean isWeapon() {
		return false;
	}
	
}
