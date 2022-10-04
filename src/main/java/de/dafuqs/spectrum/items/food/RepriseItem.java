package de.dafuqs.spectrum.items.food;

import net.minecraft.item.ItemStack;

public class RepriseItem extends BeverageItem {
	
	public RepriseItem(Settings settings) {
		super(settings);
	}
	
	public BeverageProperties getBeverageProperties(ItemStack itemStack) {
		return DefaultBeverageItem.DefaultBeverageProperties.getFromStack(itemStack);
	}
	
}
