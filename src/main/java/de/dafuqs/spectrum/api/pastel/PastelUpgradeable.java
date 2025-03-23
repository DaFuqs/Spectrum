package de.dafuqs.spectrum.api.pastel;

import java.util.*;

public interface PastelUpgradeable {

	default void apply(PastelUpgradeSignature upgrade, List<PastelUpgradeSignature> previousUpgrades) {
		if (upgrade.light) {
			markLit();
		}

		if (upgrade.triggerTransfer)
			markTriggerTransfer();

		if (upgrade.lamp)
			markLamp();

		if (upgrade.sensor)
			markSensor();

		if (upgrade.category.isRedstone())
			return;

		if (previousUpgrades.stream().anyMatch(u -> u.category.compoundsWith(upgrade.category))) {
			applyCompounding(upgrade);
		} else {
			applySimple(upgrade);
		}

		applySlotUpgrade(upgrade);

		if (upgrade.priority) {
			upgradePriority();
		}
	}

	void applyCompounding(PastelUpgradeSignature upgrade);

	void applySimple(PastelUpgradeSignature upgrade);

	void applySlotUpgrade(PastelUpgradeSignature upgrade);

	void upgradePriority();

	void markLit();

	void markLamp();

	void markTriggerTransfer();

	void markTriggered();

	void markSensor();

	boolean isTriggerTransfer();

	boolean isSensor();

	void notifySensor();
}
