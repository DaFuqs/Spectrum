package de.dafuqs.spectrum.api.ink.storage;

import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.item.*;

/**
 * Defines that an object holds a PigmentEnergyStorage
 * Objects are supposed to be items (so they implement getDefaultStack())
 *
 * @param <PStorage>
 */
public interface InkStorageItem<PStorage extends InkStorage> {
	
	default boolean canFill() {
		return true;
	}
	
	default boolean canDrain(boolean player) {
		return true;
	}
	
	PStorage getEnergyStorage(ItemStack itemStack);
	
	default void setEnergyStorage(ItemStack itemStack, InkStorage storage) {
		itemStack.set(SpectrumDataComponentTypes.INK_STORAGE, new InkStorageComponent(storage));
	}
	
	ItemStack getDefaultInstance();
	
	default ItemStack getFullStack() {
		ItemStack stack = this.getDefaultInstance();
		PStorage storage = getEnergyStorage(stack);
		storage.fillCompletely();
		setEnergyStorage(stack, storage);
		return stack;
	}
	
	default void clearEnergyStorage(ItemStack stack) {
		PStorage storage = getEnergyStorage(stack);
		storage.clearContent();
		setEnergyStorage(stack, storage);
	}
	
}