package de.dafuqs.spectrum.blocks.titration_barrel.recipes;

import de.dafuqs.spectrum.items.food.JadeWineItem;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class JadeWineRecipe extends TitrationBarrelRecipe {
	
	@Override
	public boolean isValidIngredient(DefaultedList<ItemStack> content, ItemStack itemStack) {
		if(itemStack.isOf(SpectrumItems.GERMINATED_JADE_VINE_SEEDS) || itemStack.isOf(SpectrumItems.JADE_VINE_PETALS)) {
			return true;
		}
		if(itemStack.isOf(SpectrumItems.MOONSTRUCK_NECTAR)) {
			boolean sweetened = getCountInInventory(content, SpectrumItems.MOONSTRUCK_NECTAR) > 0;
			return !sweetened;
		}
		return false;
	}
	
	@Override
	public ItemStack tap(DefaultedList<ItemStack> content, int waterBuckets, long secondsFermented, float downfall, float temperature) {
		int bulbCount = getCountInInventory(content, SpectrumItems.GERMINATED_JADE_VINE_SEEDS);
		int petalCount = getCountInInventory(content, SpectrumItems.JADE_VINE_PETALS);
		boolean sweetened = getCountInInventory(content, SpectrumItems.MOONSTRUCK_NECTAR) > 0;
		
		int yield = getYieldBottles(waterBuckets, secondsFermented, temperature);
		
		double bloominess = getBloominess(bulbCount, petalCount);
		double thickness = getThickness(bulbCount, petalCount, waterBuckets);
		double alcPercent = getAlcPercent(secondsFermented, downfall, bloominess, thickness);
		
		JadeWineItem.JadeWineProperties properties = new JadeWineItem.JadeWineProperties(secondsFermented, (float) alcPercent, (float) bloominess, sweetened);
		ItemStack stack = JadeWineItem.getWineStack(properties);
		stack.setCount(yield);
		
		return stack;
	}
	
	// bloominess reduces the possibility of negative effects to trigger (better on the tounge)
	// but also reduces the potency of positive effects a bit
	protected static double getBloominess(int bulbCount, int petalCount) {
		if(bulbCount == 0) {
			return 0;
		}
		return (double) petalCount / (double) bulbCount / 2F;
	}
	
	// the amount of solid to liquid
	// adding more water will increase the amount of bottles the player can harvest from the barrel
	// but too much water will - who would have thought - water it down
	protected static double getThickness(int bulbCount, int petalCount, int waterCount) {
		if(waterCount == 0) {
			return 0;
		}
		return (double) waterCount / (bulbCount + petalCount / 8F);
	}
	
	// the alc % determines the power of effects when drunk
	// it generally increases the longer the wine has fermented
	//
	// another detail: the more rainy the weather (downfall) the more water evaporates
	// in contrast to alcohol, making the drink stronger / weaker in return
	protected static double getAlcPercent(long secondsFermented, float downfall, double bloominess, double thickness) {
		float minecraftDaysFermented = minecraftDaysFromSeconds(secondsFermented);
		return logBase(1.08, thickness * minecraftDaysFermented * (0.5D + downfall / 2)) - bloominess;
	}
	
}
