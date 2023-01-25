package de.dafuqs.spectrum.energy;

import de.dafuqs.spectrum.energy.color.*;

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

	/**
	 * Drains ink from the BlockEntities Ink Storage
	 * For that it uses an exponential scale:
	 * mod 1 (default): 0 drain
	 * mod 2: 1.5 drain
	 * mod 3: 4 drain
	 * mod 4: 7.5 drain
	 * mod 5: 12 drain
	 *
	 * @param mod           the input ink amount to drain. Will be scaled via an exponential scale
	 * @param inkColor      the ink color to drain
	 * @param efficiencyMod modifier on
	 * @return how much ink was successfully drained
	 */
	default float drainInkForMod(float mod, InkColor inkColor, float efficiencyMod) {
		if (mod > 1) {
			int inkToDrain = (int) (1 / Math.pow(2, efficiencyMod - mod * mod));
			if (getEnergyStorage().drainEnergy(inkColor, inkToDrain) == inkToDrain) {
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