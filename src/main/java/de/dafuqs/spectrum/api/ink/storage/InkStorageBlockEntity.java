package de.dafuqs.spectrum.api.ink.storage;

import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.blocks.upgrade.*;
import org.jetbrains.annotations.*;

/**
 * Defines that an object holds a PigmentEnergyStorage
 * Objects are supposed to be block entities
 *
 * @param <IS>
 */
public interface InkStorageBlockEntity<IS extends InkStorage> {
	
	IS getInkStorage();
	
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
		if (getInkStorage().drainEnergy(color, inkToDrain) == inkToDrain) {
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
	default boolean drainInkForUpgradesRequired(@NotNull Upgradeable upgradeable, @NotNull Upgradeable.UpgradeType upgradeType, @NotNull InkColor color, boolean useEfficiency) {
		Upgradeable.UpgradeHolder upgradeHolder = upgradeable.getUpgradeHolder();
		long inkToDrain = useEfficiency ? upgradeHolder.getEffectiveCostUsingEfficiency(upgradeType) : upgradeHolder.getEffectiveCost(upgradeType);
		if (getInkStorage().drainEnergy(color, inkToDrain) == inkToDrain) {
			setInkDirty();
			return true;
		}
		
		setInkDirty();
		return false;
	}
	
	/**
	 * Drains ink exponentially increased by the amount of upgrades used
	 *
	 * @return true if ink could be drained, false if not.
	 */
	default boolean drainInkForUpgradesRequired(@NotNull Upgradeable upgradeable, @NotNull InkColor color, long amount, boolean useEfficiency) {
		long inkToDrain = useEfficiency ? upgradeable.getUpgradeHolder().getEffectiveCostUsingEfficiency(amount) : amount;
		if (getInkStorage().drainEnergy(color, inkToDrain) == inkToDrain) {
			setInkDirty();
			return true;
		}
		
		setInkDirty();
		return false;
	}
	
	void setInkDirty();
	
	boolean getInkDirty();
	
}