package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.render.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.util.*;

public class FluidConvertingEmiRecipeGated extends GatedSpectrumEmiRecipe<FluidConvertingRecipe> {
	
	public FluidConvertingEmiRecipeGated(EmiRecipeCategory category, FluidConvertingRecipe recipe) {
		super(category, recipe, 78, 26);
		this.inputs = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
	}
	
	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 23, 4);
		widgets.addSlot(inputs.get(0), 0, 4);
		widgets.addSlot(outputs.get(0), 52, 0).large(true).recipeContext(this);
	}
}
