package de.dafuqs.spectrum.items.food.beverages;

import de.dafuqs.spectrum.items.food.beverages.properties.*;
import net.minecraft.item.*;

public class PureAlcoholItem extends BeverageItem {
	
	public PureAlcoholItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public BeverageProperties getBeverageProperties(ItemStack itemStack) {
		BeverageProperties beverageProperties = BeverageProperties.getFromStack(itemStack);
		beverageProperties.alcPercent = 100;
		return beverageProperties;
	}
	
}
