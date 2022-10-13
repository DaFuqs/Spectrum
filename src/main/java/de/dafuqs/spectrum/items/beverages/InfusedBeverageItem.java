package de.dafuqs.spectrum.items.beverages;

import de.dafuqs.spectrum.items.beverages.properties.BeverageProperties;
import de.dafuqs.spectrum.items.beverages.properties.VariantBeverageProperties;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;

public class InfusedBeverageItem extends BeverageItem {
	
	public static final FoodComponent FOOD_COMPONENT = new FoodComponent.Builder().hunger(2).saturationModifier(0.2F).snack().build();
	
	public InfusedBeverageItem(Settings settings) {
		super(settings);
	}
	
	public BeverageProperties getBeverageProperties(ItemStack itemStack) {
		return VariantBeverageProperties.getFromStack(itemStack);
	}
	
	
}
