package de.dafuqs.spectrum.energy;

import net.minecraft.item.ItemStack;

/**
 * Defines that an object holds a PigmentEnergyStorage
 * Objects are supposed to be items (so they implement getDefaultStack())
 *
 * @param <PStorage>
 */
public interface InkStorageItem<PStorage extends InkStorage> {

	enum Drainability {
		ALWAYS,
		MACHINE_ONLY,
		PLAYER_ONLY,
		NEVER;
		
		boolean canDrain(boolean isPlayer) {
			switch (this) {
				case NEVER -> {
					return false;
				}
				case PLAYER_ONLY -> {
					return isPlayer;
				}
				case MACHINE_ONLY -> {
					return !isPlayer;
				}
				default -> {
					return true;
				}
			}
		}
	}
	
	Drainability getDrainability();
	
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
	
	default void clearEnergyStorage(ItemStack stack) {
		PStorage storage = getEnergyStorage(stack);
		storage.clear();
		setEnergyStorage(stack, storage);
	}
	
	
}