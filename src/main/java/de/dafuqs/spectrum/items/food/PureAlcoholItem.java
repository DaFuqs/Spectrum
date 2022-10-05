package de.dafuqs.spectrum.items.food;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class PureAlcoholItem extends BeverageItem {
	
	public static final FoodComponent FOOD_COMPONENT = new FoodComponent.Builder().alwaysEdible().snack()
			.statusEffect(new StatusEffectInstance(StatusEffects.POISON, 20 * 15, 4), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20 * 30, 2), 1.0F)
			.statusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 20 * 60, 2), 1.0F)
			.build();
	
	public PureAlcoholItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public BeverageProperties getBeverageProperties(ItemStack itemStack) {
		return BeverageItem.BeverageProperties.getFromStack(itemStack);
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
