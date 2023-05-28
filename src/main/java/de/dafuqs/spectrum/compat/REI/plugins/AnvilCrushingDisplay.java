package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.GatedSpectrumDisplay;
import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.recipe.anvil_crushing.AnvilCrushingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;

import java.util.Collections;

public class AnvilCrushingDisplay extends GatedSpectrumDisplay {
	public final float experience;
	public final float crushedItemsPerPointOfDamage;
	
	public AnvilCrushingDisplay(AnvilCrushingRecipe recipe) {
		super(recipe, recipe.getIngredients().stream().map(EntryIngredients::ofIngredient).toList(), Collections.singletonList(EntryIngredients.of(recipe.getOutput(null))));
		this.experience = recipe.getExperience();
		this.crushedItemsPerPointOfDamage = recipe.getCrushedItemsPerPointOfDamage();
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.ANVIL_CRUSHING;
	}
	
}