package de.dafuqs.spectrum.items.beverages;

import de.dafuqs.spectrum.items.beverages.properties.BeverageProperties;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

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
	
	@Override
	public ItemStack getDefaultStack() {
		BeverageProperties properties = new BeverageProperties(100, 100, 1);
		return properties.getStack(super.getDefaultStack());
	}
	
	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
		if (this.isIn(group)) {
			stacks.add(getDefaultStack());
		}
	}
	
}
