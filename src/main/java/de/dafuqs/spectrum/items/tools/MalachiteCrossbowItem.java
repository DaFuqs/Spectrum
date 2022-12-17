package de.dafuqs.spectrum.items.tools;

import net.minecraft.item.CrossbowItem;

public class MalachiteCrossbowItem extends CrossbowItem {
	
	private final MalachiteWorkstaffItem.Variant variant;
	
	public MalachiteCrossbowItem(Settings settings, MalachiteWorkstaffItem.Variant variant) {
		super(settings);
		this.variant = variant;
	}
	
}
