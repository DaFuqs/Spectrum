package de.dafuqs.spectrum.items;

import net.minecraft.world.item.*;

public class ItemWithGlint extends Item {
	
	public ItemWithGlint(Properties settings) {
		super(settings);
	}
	
	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}
	
}
