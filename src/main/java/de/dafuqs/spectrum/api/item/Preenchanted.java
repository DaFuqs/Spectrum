package de.dafuqs.spectrum.api.item;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public interface Preenchanted {
	
	Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments();
	static ItemEnchantments buildDefaultEnchantments(HolderLookup.Provider lookup, Preenchanted item) {
		ItemEnchantments.Mutable builder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
		for (Map.Entry<ResourceKey<Enchantment>, Integer> entry : item.getDefaultEnchantments().entrySet()) {
			builder.set(lookup.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(entry.getKey()), entry.getValue());
		}
		return builder.toImmutable();
	}
	
	static <T extends Item & Preenchanted> ItemStack getDefaultEnchantedStack(HolderLookup.Provider lookup, T item) {
		ItemStack stack = new ItemStack(item);
		stack.set(DataComponents.ENCHANTMENTS, buildDefaultEnchantments(lookup, item));
		return stack;
	}
	
	/**
	 * Checks a stack if it only has enchantments that are lower or equal its DefaultEnchantments,
	 * meaning enchantments had been added on top of the original ones.
	 */
	default boolean onlyHasPreEnchantments(ItemStack stack) {
		ItemEnchantments stackEnchants = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
		
		Map<ResourceKey<Enchantment>, Integer> defaultEnchants = new Object2IntArrayMap<>(getDefaultEnchantments());
		for(Object2IntMap.Entry<Holder<Enchantment>> stackEnchantEntry : stackEnchants.entrySet()) {
			@Nullable ResourceKey<Enchantment> stackEnchantKey = stackEnchantEntry.getKey().getKey();
			int stackEnchantLevel = defaultEnchants.getOrDefault(stackEnchantKey, 0);
			if(stackEnchantLevel == stackEnchantEntry.getIntValue()) {
				defaultEnchants.remove(stackEnchantKey);
			} else {
				return false;
			}
		}
		
		return defaultEnchants.isEmpty();
	}
}
