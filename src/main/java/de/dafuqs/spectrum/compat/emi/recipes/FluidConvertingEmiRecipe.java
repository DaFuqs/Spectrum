package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.compat.emi.SpectrumEmiRecipe;
import de.dafuqs.spectrum.recipe.fluid_converting.FluidConvertingRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;

public class FluidConvertingEmiRecipe extends SpectrumEmiRecipe<FluidConvertingRecipe> {

	public FluidConvertingEmiRecipe(EmiRecipeCategory category, FluidConvertingRecipe recipe, Identifier unlock) {
		super(category, unlock, recipe, 78, 26);
	}

	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 23, 4);
		widgets.addSlot(input.get(0), 0, 4);
		widgets.addSlot(output.get(0), 52, 0).output(true).recipeContext(this);
	}
}
