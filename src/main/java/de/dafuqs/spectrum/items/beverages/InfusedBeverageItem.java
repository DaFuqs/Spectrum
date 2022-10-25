package de.dafuqs.spectrum.items.beverages;

import de.dafuqs.spectrum.items.beverages.properties.BeverageProperties;
import de.dafuqs.spectrum.items.beverages.properties.VariantBeverageProperties;
import net.minecraft.item.ItemStack;

public class InfusedBeverageItem extends BeverageItem {
	
	public InfusedBeverageItem(Settings settings) {
		super(settings);
	}
	
	public BeverageProperties getBeverageProperties(ItemStack itemStack) {
		return VariantBeverageProperties.getFromStack(itemStack);
	}
	
	
}
