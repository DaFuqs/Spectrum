package de.dafuqs.spectrum.items.food;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;

public class KimchiItem extends Item {
	
	public static final FoodComponent FOOD_COMPONENT = new FoodComponent.Builder().hunger(6).saturationModifier(0.6F).build();
	
	public KimchiItem(Settings settings) {
		super(settings);
	}
	
}
