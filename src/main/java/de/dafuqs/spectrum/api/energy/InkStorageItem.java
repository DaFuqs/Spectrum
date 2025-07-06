package de.dafuqs.spectrum.api.energy;

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

	enum Drainability {
		ALWAYS, // ink storage should be able to be used via both machines and players
		MACHINE_ONLY, // ink storage should only be drained via machines (block entities)
		PLAYER_ONLY, // ink storage should only be drained via a player (ink storage trinkets)
		NEVER; // ink storage should never be drained, only filled (upgradeable trinkets)

		public boolean canDrain(boolean isPlayer) {
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