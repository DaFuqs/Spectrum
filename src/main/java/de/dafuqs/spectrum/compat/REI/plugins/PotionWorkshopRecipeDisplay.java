package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.potion_workshop.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.registry.*;

import java.util.*;

public abstract class PotionWorkshopRecipeDisplay extends GatedSpectrumDisplay {
	
	protected final int craftingTime;
	
	/**
	 * When using the REI recipe functionality
	 *
	 * @param recipe The recipe
	 */
	public PotionWorkshopRecipeDisplay(PotionWorkshopRecipe recipe) {
		super(recipe, REIHelper.toEntryIngredients(recipe.getIngredientStacks()), Collections.singletonList(EntryIngredients.of(recipe.getOutput(DynamicRegistryManager.EMPTY))));
		this.craftingTime = recipe.getCraftingTime();
	}
	
}