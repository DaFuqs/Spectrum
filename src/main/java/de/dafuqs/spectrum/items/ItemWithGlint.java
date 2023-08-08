package de.dafuqs.spectrum.items;

import net.minecraft.item.*;

public class ItemWithGlint extends Item {
	
	public ItemWithGlint(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}
	
}
