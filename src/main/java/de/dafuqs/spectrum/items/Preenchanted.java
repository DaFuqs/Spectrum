package de.dafuqs.spectrum.items;

import net.minecraft.enchantment.*;
import net.minecraft.item.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface Preenchanted {
	
	Map<Enchantment, Integer> getDefaultEnchantments();
	
	default @NotNull ItemStack getDefaultEnchantedStack(Item item) {
		ItemStack itemStack = new ItemStack(item);
		for (Map.Entry<Enchantment, Integer> defaultEnchantment : getDefaultEnchantments().entrySet()) {
			itemStack.addEnchantment(defaultEnchantment.getKey(), defaultEnchantment.getValue());
		}
		return itemStack;
	}
	
	/**
	 * Checks a stack if it only has enchantments that are lower or equal its DefaultEnchantments,
	 * meaning enchantments had been added on top of the original ones.
	 */
	default boolean onlyHasPreEnchantments(ItemStack stack) {
		Map<Enchantment, Integer> innateEnchantments = getDefaultEnchantments();
		Map<Enchantment, Integer> stackEnchantments = EnchantmentHelper.get(stack);
		
		for (Map.Entry<Enchantment, Integer> stackEnchantment : stackEnchantments.entrySet()) {
			int innateLevel = innateEnchantments.getOrDefault(stackEnchantment.getKey(), 0);
			if (stackEnchantment.getValue() > innateLevel) {
				return false;
			}
		}
		
		return true;
	}
	
}
