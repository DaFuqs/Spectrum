package de.dafuqs.spectrum.items.food.beverages;

import de.dafuqs.spectrum.items.food.beverages.properties.*;
import net.minecraft.item.*;

public class VariantBeverageItem extends BeverageItem {
	
	public VariantBeverageItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public BeverageProperties getBeverageProperties(ItemStack itemStack) {
		return VariantBeverageProperties.getFromStack(itemStack);
	}

}
