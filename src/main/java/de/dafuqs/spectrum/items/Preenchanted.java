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
	
}
