package de.dafuqs.spectrum.items.beverages;

import de.dafuqs.spectrum.items.beverages.properties.BeverageProperties;
import de.dafuqs.spectrum.items.beverages.properties.StatusEffectBeverageProperties;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;

public class SuspiciousBrewItem extends BeverageItem {
	
	public SuspiciousBrewItem(Settings settings) {
		super(settings);
	}
	
	public BeverageProperties getBeverageProperties(ItemStack itemStack) {
		return StatusEffectBeverageProperties.getFromStack(itemStack);
	}
	
}
