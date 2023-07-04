package de.dafuqs.spectrum.blocks.enchanter;

import net.minecraft.enchantment.*;

/**
 * Items with that Interface will be able to be enchanted with the given enchants
 * Most notably for items that do not match an existing net.minecraft.enchantment.EnchantmentTarget
 * (since that one is an enum, it cannot be easily extended)
 * <p>
 * Using this interface there is no need to mixin into every individual enchantment to override `isAcceptableItem`
 * Note that for an item to be enchanted, it still needs to have `isEnchantable() == true` and `getEnchantability() > 0`
 */
public interface ExtendedEnchantable {
	
	boolean canAcceptEnchantment(Enchantment enchantment);
	
}
