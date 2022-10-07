package de.dafuqs.spectrum.items.food;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EnchantedStarCandyItem extends Item {
	
	public static final FoodComponent FOOD_COMPONENT = new FoodComponent.Builder()
			.hunger(20).saturationModifier(0.25F)
			.statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 20), 1.0F)
			.alwaysEdible().snack().build();
	
	public EnchantedStarCandyItem(Settings settings) {
		super(settings);
	}
	
	public boolean hasGlint(ItemStack stack) {
		return true;
	}
	
}
