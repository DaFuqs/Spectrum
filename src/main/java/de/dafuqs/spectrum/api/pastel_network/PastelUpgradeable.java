package de.dafuqs.spectrum.api.pastel_network;

import org.jspecify.annotations.*;

public interface PastelUpgradeable {

	default void apply(PastelUpgradeSignature upgrade, @Nullable PastelUpgradeSignature previous) {
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

		if (previous != null && previous.category.compoundsWith(upgrade.category)) {
			applyCompounding(upgrade);
		} else {
			applySimple(upgrade);
		}

		applySlotUpgrade(upgrade);
	}

	void applyCompounding(PastelUpgradeSignature upgrade);

	void applySimple(PastelUpgradeSignature upgrade);

	void applySlotUpgrade(PastelUpgradeSignature upgrade);

	void markLit();

	void markLamp();

	void markTriggerTransfer();

	void markTriggered();

	void markSensor();

	boolean isTriggerTransfer();

	boolean isSensor();

	void notifySensor();
}
