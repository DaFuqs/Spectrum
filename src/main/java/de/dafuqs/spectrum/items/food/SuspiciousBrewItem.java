package de.dafuqs.spectrum.items.food;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;

public class SuspiciousBrewItem extends BeverageItem {
	
	public static final FoodComponent FOOD_COMPONENT = new FoodComponent.Builder().hunger(2).saturationModifier(0.4F).snack().build();
	
	public SuspiciousBrewItem(Settings settings) {
		super(settings);
	}
	
	public BeverageProperties getBeverageProperties(ItemStack itemStack) {
		return BeverageItem.BeverageProperties.getFromStack(itemStack);
	}
	
}
