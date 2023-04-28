package de.dafuqs.spectrum.recipe.titration_barrel;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.TimeHelper;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.registries.*;
import net.id.incubus_core.recipe.*;
import net.minecraft.fluid.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;

import java.util.*;

/**
 * In contrast to most other Minecraft things, the titration barrel also counts the fermenting time
 * when the game is not running (comparing the time of sealing to the time of opening)
 * Making it a non-ticking block entity and also "fermenting" when the game is not running
 * This also means TitrationBarrelRecipes have to calculate their time using real life seconds, instead of game ticks
 */
public interface ITitrationBarrelRecipe extends Recipe<Inventory>, GatedRecipe {
	
	Identifier UNLOCK_ADVANCEMENT_IDENTIFIER = SpectrumCommon.locate("unlocks/blocks/titration_barrel");
	
	ItemStack tap(Inventory inventory, long secondsFermented, float downfall);
	
	Item getTappingItem();
	
	Fluid getFluidInput();
	
	float getAngelsSharePerMcDay();
	
	// the amount of bottles able to get out of a single barrel
	default int getOutputCountAfterAngelsShare(float temperature, long secondsFermented) {
		if (getFermentationData() == null) {
			return getOutput().getCount();
		}
		
		float angelsSharePercent = getAngelsSharePercent(secondsFermented, temperature);
		if (angelsSharePercent > 0) {
			return (int) (getOutput().getCount() * Math.ceil(1F - angelsSharePercent / 100F));
		} else {
			return (int) (getOutput().getCount() * Math.floor(1F - angelsSharePercent / 100F));
		}
	}
	
	// the amount of fluid that evaporated while fermenting
	// the higher the temperature in the biome is, the more evaporates
	// making colder biomes more desirable
	default float getAngelsSharePercent(long secondsFermented, float temperature) {
		return Math.max(0.1F, temperature) * TimeHelper.minecraftDaysFromSeconds(secondsFermented) * getAngelsSharePerMcDay();
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
	
	FermentationData getFermentationData();

}
