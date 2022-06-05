package de.dafuqs.spectrum.items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

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
