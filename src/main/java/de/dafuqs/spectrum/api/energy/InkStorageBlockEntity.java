package de.dafuqs.spectrum.api.energy;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import org.jetbrains.annotations.*;

/**
 * Defines that an object holds a PigmentEnergyStorage
 * Objects are supposed to be block entities
 *
 * @param <PStorage>
 */
public interface InkStorageBlockEntity<PStorage extends InkStorage> {
	
	PStorage getEnergyStorage();
	
	/**
	 * Drains ink exponentially increased by the amount of upgrades used.
	 * No upgrades: no ink use
	 *
	 * @return the effective upgrade value, or 1 if no upgrades of that type used or ink could not be drained
	 */
	default float drainInkForUpgrades(@NotNull Upgradeable upgradeable, @NotNull Upgradeable.UpgradeType upgradeType, @NotNull InkColor color, boolean useEfficiency) {
		Upgradeable.UpgradeHolder upgradeHolder = upgradeable.getUpgradeHolder();
		if (upgradeHolder.getRawValue(upgradeType) == 0) {
			return 1;
		}
		
		long inkToDrain = useEfficiency ? upgradeHolder.getEffectiveCostUsingEfficiency(upgradeType) : upgradeHolder.getEffectiveCost(upgradeType);
		if (getEnergyStorage().drainEnergy(color, inkToDrain) == inkToDrain) {
			setInkDirty();
			return upgradeHolder.getEffectiveValue(upgradeType);
		}
		
		setInkDirty();
		return 1;
	}
	
	/**
	 * Drains ink exponentially increased by the amount of upgrades used
	 *
	 * @return true if ink could be drained, false if not.
	 */
	default boolean drainInkForUpdatesRequired(@NotNull Upgradeable upgradeable, @NotNull Upgradeable.UpgradeType upgradeType, @NotNull InkColor color, boolean useEfficiency) {
		Upgradeable.UpgradeHolder upgradeHolder = upgradeable.getUpgradeHolder();
		long inkToDrain = useEfficiency ? upgradeHolder.getEffectiveCostUsingEfficiency(upgradeType) : upgradeHolder.getEffectiveCost(upgradeType);
		if (getEnergyStorage().drainEnergy(color, inkToDrain) == inkToDrain) {
			setInkDirty();
			return true;
		}
		
		setInkDirty();
		return false;
	}
	
	void setInkDirty();
	
	boolean getInkDirty();
	
}