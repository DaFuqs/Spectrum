package de.dafuqs.spectrum.energy;

import de.dafuqs.spectrum.energy.storage.InkStorage;
import net.minecraft.item.ItemStack;

/**
 * Defines that an object holds a PigmentEnergyStorage
 * Objects are supposed to be items (so they implement getDefaultStack())
 * @param <PStorage>
 */
public interface InkStorageItem<PStorage extends InkStorage> {
	
	PStorage getEnergyStorage(ItemStack itemStack);
	void setEnergyStorage(ItemStack itemStack, PStorage storage);
	ItemStack getDefaultStack();
		
	default ItemStack getFullStack() {
		ItemStack stack = this.getDefaultStack();
		PStorage storage = getEnergyStorage(stack);
		storage.fillCompletely();
		setEnergyStorage(stack, storage);
		return stack;
	}
	
}