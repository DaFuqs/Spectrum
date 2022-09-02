package de.dafuqs.spectrum.items.tools;

import net.minecraft.item.BowItem;

public class SpectrumBowItem extends BowItem {
	
	public SpectrumBowItem(Settings settings) {
		super(settings);
	}
	
	public float getZoom() {
		return 30F;
	}
	
}
