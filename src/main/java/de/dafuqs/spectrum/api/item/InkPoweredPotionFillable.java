package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.api.energy.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface InkPoweredPotionFillable {
	
	int maxEffectCount();
	int maxEffectAmplifier();
	
	// used for calculating the items cost to apply a certain effect
	// calculated once and then stored in the items nbt for quick lookup and nicer modifiability
	// via commands or special loot (so ones found in dungeon chests can be cheaper!)
	default long adjustFinalCostFor(@NotNull InkPoweredStatusEffectInstance instance) {
		return (long) Math.pow(instance.getInkCost().cost(), 1 + instance.getStatusEffectInstance().getAmplifier());
	}
	
	// saving
	default void addOrUpgradeEffects(ItemStack potionFillableStack, List<InkPoweredStatusEffectInstance> newEffects) {
		if (!isFull(potionFillableStack)) {
			// by default, values are immutable, so we need to copy the values to an arraylist to be able to add stuff to it
			List<InkPoweredStatusEffectInstance> existingEffects = new ArrayList<>(InkPoweredStatusEffectInstance.getEffects(potionFillableStack));
			int maxCount = maxEffectCount();
			int maxAmplifier = maxEffectAmplifier();
			for (InkPoweredStatusEffectInstance newEffect : newEffects) {
				MobEffectInstance statusEffectInstance = newEffect.getStatusEffectInstance();
				if (statusEffectInstance.getAmplifier() > maxAmplifier) {
					statusEffectInstance = new MobEffectInstance(statusEffectInstance.getEffect(), statusEffectInstance.getDuration(), maxAmplifier, statusEffectInstance.isAmbient(), statusEffectInstance.isVisible());
				}
				if (existingEffects.size() == maxCount) {
					break;
				}
				
				// calculate the final cost of this effect and add it
				InkCost adjustedCost = new InkCost(newEffect.getInkCost().color(), adjustFinalCostFor(newEffect));
				InkPoweredStatusEffectInstance modifiedInstance = new InkPoweredStatusEffectInstance(statusEffectInstance, adjustedCost, newEffect.getColor(), newEffect.isUnidentifiable(), newEffect.isIncurable());
				existingEffects.add(modifiedInstance);
			}
			
			InkPoweredStatusEffectInstance.setEffects(potionFillableStack, existingEffects);
		}
	}
	
	static List<InkPoweredStatusEffectInstance> getEffects(ItemStack stack) {
		return InkPoweredStatusEffectInstance.getEffects(stack);
	}
	
	@Deprecated
	default List<MobEffectInstance> getVanillaEffects(ItemStack stack) {
		return InkPoweredStatusEffectInstance.getEffects(stack).stream().map(InkPoweredStatusEffectInstance::getStatusEffectInstance).toList();
	}
	
	default boolean isFull(ItemStack itemStack) {
		return InkPoweredStatusEffectInstance.getEffects(itemStack).size() >= maxEffectCount();
	}
	
	default boolean isAtLeastPartiallyFilled(ItemStack itemStack) {
		return !InkPoweredStatusEffectInstance.getEffects(itemStack).isEmpty();
	}
	
	default void clearEffects(ItemStack itemStack) {
		InkPoweredStatusEffectInstance.setEffects(itemStack, List.of());
	}
	
	default void appendPotionFillableTooltip(ItemStack stack, List<Component> tooltip, MutableComponent attributeModifierText, boolean showDuration, float tickRate) {
		List<InkPoweredStatusEffectInstance> effects = InkPoweredStatusEffectInstance.getEffects(stack);
		InkPoweredStatusEffectInstance.buildTooltip(tooltip, effects, attributeModifierText, showDuration, tickRate);
		
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
