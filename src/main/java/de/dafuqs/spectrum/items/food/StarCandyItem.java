package de.dafuqs.spectrum.items.food;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;

public class StarCandyItem extends Item {
	
	public static final FoodComponent FOOD_COMPONENT = new FoodComponent.Builder()
			.hunger(2).saturationModifier(0.25F)
			.alwaysEdible().snack().build();
	
	public StarCandyItem(Settings settings) {
		super(settings);
	}
	
}
