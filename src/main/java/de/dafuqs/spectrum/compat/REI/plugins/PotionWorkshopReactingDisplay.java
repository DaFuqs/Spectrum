package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopReactingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;

public class PotionWorkshopReactingDisplay extends GatedItemInformationDisplay {
	
	public PotionWorkshopReactingDisplay(PotionWorkshopReactingRecipe recipe) {
		super(recipe);
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.POTION_WORKSHOP_REACTING;
	}
	
}