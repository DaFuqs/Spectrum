package de.dafuqs.spectrum.blocks.titration_barrel.recipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

/**
 * In contrast to most other Minecraft things, the titration barrel also counts the fermenting time
 * when the game is not running (comparing the time of sealing to the time of opening)
 * Making it a non-ticking block entity and also "fermenting" when the game is not running
 * This also means TitrationBarrelRecipes have to calculate their time using real life seconds, instead of game ticks
 */
public abstract class TitrationBarrelRecipe {
	
	public static final double BOTTLES_PER_BUCKET_OF_WATER = 3D;
	
	public abstract boolean isValidIngredient(DefaultedList<ItemStack> content, ItemStack itemStack);
	
	public abstract ItemStack tap(DefaultedList<ItemStack> content, int waterBuckets, long secondsFermented, float downfall, float temperature);
	
	// the amount of bottles able to get out of a single barrel
	public static int getYieldBottles(int waterCount, long secondsFermented, float temperature) {
		return (int) Math.ceil(BOTTLES_PER_BUCKET_OF_WATER * waterCount * (1F - getAngelsSharePercent(secondsFermented, temperature) / 100F));
	}
	
	// the amount of fluid that evaporated while fermenting
	// the higher the temperature in the biome is, the more evaporates
	// making colder biomes more desirable
	protected static double getAngelsSharePercent(long secondsFermented, float temperature) {
		return Math.max(0.1, temperature) * minecraftDaysFromSeconds(secondsFermented) / 8;
	}
	
	public static int getCountInInventory(List<ItemStack> inventory, Item item) {
		int count = 0;
		for (ItemStack stack : inventory) {
			if (stack.isOf(item)) {
				count += stack.getCount();
			}
		}
		return count;
	}
	
	public static float minecraftDaysFromSeconds(long seconds) {
		return seconds / 1200F;
	}
	
	protected static double logBase(double base, double logNumber) {
		return Math.log(logNumber) / Math.log(base);
	}
	
}
