package de.dafuqs.spectrum.items.magic_items;

import net.minecraft.item.BookItem;
import net.minecraft.item.Items;

public class GildedBookItem extends BookItem {
	
	public GildedBookItem(Settings settings) {
		super(settings);
	}
	
	public int getEnchantability() {
		return Items.GOLDEN_PICKAXE.getEnchantability();
	}
	
}
