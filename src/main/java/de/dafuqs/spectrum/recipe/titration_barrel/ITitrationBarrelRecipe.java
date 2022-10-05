package de.dafuqs.spectrum.recipe.titration_barrel;

import de.dafuqs.revelationary.api.advancements.AdvancementHelper;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.recipe.GatedRecipe;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

/**
 * In contrast to most other Minecraft things, the titration barrel also counts the fermenting time
 * when the game is not running (comparing the time of sealing to the time of opening)
 * Making it a non-ticking block entity and also "fermenting" when the game is not running
 * This also means TitrationBarrelRecipes have to calculate their time using real life seconds, instead of game ticks
 */
public interface ITitrationBarrelRecipe extends Recipe<Inventory>, GatedRecipe {
	
	Identifier UNLOCK_ADVANCEMENT_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_titration_barrel");
	
	double BOTTLES_PER_BUCKET_OF_WATER = 3D;
	
	ItemStack tap(DefaultedList<ItemStack> content, int waterBuckets, long secondsFermented, float downfall, float temperature);
	
	// the amount of bottles able to get out of a single barrel
	static int getYieldBottles(int waterCount, long secondsFermented, float temperature) {
		return (int) Math.ceil(BOTTLES_PER_BUCKET_OF_WATER * waterCount * (1F - getAngelsSharePercent(secondsFermented, temperature) / 100F));
	}
	
	// the amount of fluid that evaporated while fermenting
	// the higher the temperature in the biome is, the more evaporates
	// making colder biomes more desirable
	static double getAngelsSharePercent(long secondsFermented, float temperature) {
		return Math.max(0.1, temperature) * minecraftDaysFromSeconds(secondsFermented) / 8;
	}
	
	static int getCountInInventory(List<ItemStack> inventory, Item item) {
		int count = 0;
		for (ItemStack stack : inventory) {
			if (stack.isOf(item)) {
				count += stack.getCount();
			}
		}
		return count;
	}
	
	static float minecraftDaysFromSeconds(long seconds) {
		return seconds / 1200F;
	}
	
	@Override
	default boolean canPlayerCraft(PlayerEntity playerEntity) {
		return AdvancementHelper.hasAdvancement(playerEntity, UNLOCK_ADVANCEMENT_IDENTIFIER);
	}
	
	@Override
	default TranslatableText getSingleUnlockToastString() {
		return new TranslatableText("spectrum.toast.titration_barrel_recipe_unlocked.title");
	}
	
	@Override
	default TranslatableText getMultipleUnlockToastString() {
		return new TranslatableText("spectrum.toast.titration_barrel_recipes_unlocked.title");
	}
	
	@Override
	default boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	default ItemStack createIcon() {
		return SpectrumBlocks.TITRATION_BARREL.asItem().getDefaultStack();
	}
	
	@Override
	default RecipeType<?> getType() {
		return SpectrumRecipeTypes.TITRATION_BARREL;
	}
	
	List<IngredientStack> getIngredientStacks();
	
	int getMinFermentationTimeHours();
	
	TitrationBarrelRecipe.FermentationData getFermentationData();
	
}
