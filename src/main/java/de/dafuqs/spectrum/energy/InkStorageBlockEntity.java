package de.dafuqs.spectrum.energy;

import de.dafuqs.spectrum.energy.storage.InkStorage;

/**
 * Defines that an object holds a PigmentEnergyStorage
 * Objects are supposed to be block entities
 * @param <PStorage>
 */
public interface InkStorageBlockEntity<PStorage extends InkStorage> {
	
	PStorage getEnergyStorage();
	
}