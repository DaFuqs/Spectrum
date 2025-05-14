package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;

public interface ExperienceStorageItem {
	
	/**
	 * Returns the amount of experience this item stack has stored
	 *
	 * @param itemStack The item stack
	 * @return The amount of stored experience
	 */
	static int getStoredExperience(ItemStack itemStack) {
		return itemStack.getOrDefault(SpectrumDataComponentTypes.STORED_EXPERIENCE, 0);
	}
	
	/**
	 * Adds amount experience to an ExperienceProviderItem stack.
	 * If the experience would be overflowing return the amount of experience that could not be stored
	 *
	 * @param itemStack The item stack
	 * @param amount    The amount of experience to store
	 * @param random    A random
	 * @return The overflow amount that could not be stored
	 */
	static int addStoredExperience(HolderLookup.Provider lookup, ItemStack itemStack, float amount, RandomSource random) {
		if (amount > 0) {
			int intAmount = Support.getIntFromDecimalWithChance(amount, random);
			return addStoredExperience(lookup, itemStack, intAmount);
		}
		return 0;
	}
	
	/**
	 * Adds amount experience to an ExperienceProviderItem stack.
	 * If the experience would be overflowing return the amount of experience that could not be stored
	 *
	 * @param itemStack The item stack
	 * @param amount    The amount of experience to store
	 * @return The overflow amount that could not be stored
	 */
	static int addStoredExperience(HolderLookup.Provider lookup, ItemStack itemStack, int amount) {
		if (amount <= 0 || itemStack.isEmpty())
			return 0;
		
		if (itemStack.getItem() instanceof ExperienceStorageItem experienceStorageItem) {
			int maxStorage = experienceStorageItem.getMaxStoredExperience(lookup, itemStack);
			int existing = itemStack.getOrDefault(SpectrumDataComponentTypes.STORED_EXPERIENCE, 0);
			int toAdd = amount + existing;
			int overflow = Math.max(toAdd - maxStorage, 0);
			toAdd = Math.min(toAdd, maxStorage);
			itemStack.set(SpectrumDataComponentTypes.STORED_EXPERIENCE, toAdd);
			return overflow;
		} else {
			itemStack.update(SpectrumDataComponentTypes.STORED_EXPERIENCE, 0, existing -> existing + amount);
			return 0;
		}
		
	}
	
	/**
	 * Removes amount experience from an ExperienceProviderItem stack.
	 * If there is not enough experience that could be removed do nothing and return false
	 *
	 * @param itemStack The item stack
	 * @param amount    The amount of experience to remove
	 * @return If there was enough experience that could be removed
	 */
	static boolean removeStoredExperience(ItemStack itemStack, int amount) {
		if (itemStack.has(SpectrumDataComponentTypes.STORED_EXPERIENCE)) {
			var existing = itemStack.getOrDefault(SpectrumDataComponentTypes.STORED_EXPERIENCE, 0);
			var newAmount = existing - amount;
			if (newAmount >= 0) {
				itemStack.set(SpectrumDataComponentTypes.STORED_EXPERIENCE, newAmount);
				return true;
			}
		}
		return false;
	}
	
	int getMaxStoredExperience(HolderLookup.Provider lookup, ItemStack itemStack);
	
}
