package de.dafuqs.spectrum.energy;

import de.dafuqs.spectrum.blocks.upgrade.*;

/**
 * Defines that an object holds a PigmentEnergyStorage
 * Objects are supposed to be block entities
 *
 * @param <PStorage>
 */
public interface InkStorageBlockEntity<PStorage extends InkStorage> {

	PStorage getEnergyStorage();

	default float drainInkForMod(Upgradeable upgradeable, Upgradeable.UpgradeType upgradeType, boolean useEfficiency) {
		Upgradeable.UpgradeHolder upgradeHolder = upgradeable.getUpgradeHolder();
		float upgradeTypeMod = upgradeHolder.getRawValue(upgradeType);
		if (upgradeTypeMod == 0) {
			return 1;
		}

		long inkToDrain = useEfficiency ? upgradeHolder.getEffectiveCostUsingEfficiency(upgradeType) : upgradeHolder.getEffectiveCost(upgradeType);
		if (getEnergyStorage().drainEnergy(upgradeType.getInkColor(), inkToDrain) == inkToDrain) {
			setInkDirty();
			return upgradeHolder.getEffectiveValue(upgradeType);
		}

		setInkDirty();
		return 1;
	}
	
	void setInkDirty();
	
	boolean getInkDirty();
	
}