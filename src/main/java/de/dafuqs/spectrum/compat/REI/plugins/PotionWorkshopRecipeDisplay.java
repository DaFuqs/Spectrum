package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.GatedSpectrumDisplay;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopRecipe;
import me.shedaniel.rei.api.common.util.EntryIngredients;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public abstract class PotionWorkshopRecipeDisplay extends GatedSpectrumDisplay {
	
	protected final int craftingTime;
	
	/**
	 * When using the REI recipe functionality
	 *
	 * @param recipe The recipe
	 */
	public PotionWorkshopRecipeDisplay(PotionWorkshopRecipe recipe) {
		super(recipe, recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).collect(Collectors.toCollection(ArrayList::new)), Collections.singletonList(EntryIngredients.of(recipe.getOutput(null))));
		this.craftingTime = recipe.getCraftingTime();
	}
	
}