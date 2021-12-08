package de.dafuqs.spectrum.items;

import net.minecraft.item.ItemStack;

public interface ExperienceStorageItem {
	
	/**
	 * Returns the amount of experience this item stack has stored
	 * @param itemStack The item stack
	 * @return The amount of stored experience
	 */
	public int getStoredExperience(ItemStack itemStack);
	
	/**
	 * Adds amount experience to an ExperienceProviderItem stack.
	 * If the experience would be overflowing return the amount of experience that could not be stored
	 * @param itemStack The item stack
	 * @param amount The amount of experience to store
	 * @return The overflow amount that could not be stored
	 */
	public int addStoredExperience(ItemStack itemStack, int amount);
	
	/**
	 * Removes amount experience from an ExperienceProviderItem stack.
	 * If there is not enough experience that could be removed do nothing and return false
	 * @param itemStack The item stack
	 * @param amount The amount of experience to remove
	 * @return If there was enough experience that could be removed
	 */
	public boolean removeStoredExperience(ItemStack itemStack, int amount);
	
}
