package de.dafuqs.spectrum.energy;

import de.dafuqs.spectrum.energy.color.InkColor;

/**
 * Defines that an object holds a PigmentEnergyStorage
 * Objects are supposed to be block entities
 *
 * @param <PStorage>
 */
public interface InkStorageBlockEntity<PStorage extends InkStorage> {
	
	PStorage getEnergyStorage();
	
	default float drainInkForMod(float mod, InkColor inkColor) {
		return drainInkForMod(mod, inkColor, 1);
	}
	
	default float drainInkForMod(float mod, InkColor inkColor, float efficiencyMod) {
		if (mod > 1) {
			int cost = (int) ((mod * mod - 1) / Math.pow(2, efficiencyMod));
			if (getEnergyStorage().drainEnergy(inkColor, cost) == cost) {
				return mod;
			}
			setInkDirty();
			return 1;
		}
		
		return 1;
	}
	
	void setInkDirty();
	
	boolean getInkDirty();
	
}