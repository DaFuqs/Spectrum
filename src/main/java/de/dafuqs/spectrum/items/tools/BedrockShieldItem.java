package de.dafuqs.spectrum.items.tools;

import net.minecraft.item.ShieldItem;

public class BedrockShieldItem extends ShieldItem {

	public BedrockShieldItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

}