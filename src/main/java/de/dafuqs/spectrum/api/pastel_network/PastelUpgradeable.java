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

		if (upgrade.goesToRedstoneRing())
			return;
		
		applySignature(upgrade);
	}
	
	void applySignature(PastelUpgradeSignature upgrade);

	void markLit();

	void markLamp();

	void markTriggerTransfer();

	void markTriggered();

	void markSensor();

	boolean isTriggerTransfer();

	boolean isSensor();

	void notifySensor();
}
