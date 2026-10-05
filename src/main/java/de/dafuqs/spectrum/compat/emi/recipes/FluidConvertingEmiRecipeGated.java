package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.render.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.world.item.crafting.*;

public class FluidConvertingEmiRecipeGated<T extends FluidConvertingRecipe> extends GatedSpectrumEmiRecipe<T> {
	
	public FluidConvertingEmiRecipeGated(EmiRecipeCategory category, RecipeHolder<T> entry) {
		super(category, entry, 120, 26);
		this.inputs = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
	}
	
	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 23 + 21, 4);
		widgets.addSlot(inputs.getFirst(), 0 + 21, 4);
		widgets.addSlot(outputs.getFirst(), 52 + 21, 0).large(true).recipeContext(this);
	}
}
