package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.recipe.crafting.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.render.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.item.*;

public class ShapelessGatedCraftingEMIRecipe extends GatedSpectrumEmiRecipe<ShapelessGatedCraftingRecipe> {

	public ShapelessGatedCraftingEMIRecipe(ShapelessGatedCraftingRecipe recipe) {
		super(VanillaEmiRecipeCategories.CRAFTING, recipe, 118, 54);
	}

	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 60, 18);
		widgets.addTexture(EmiTexture.SHAPELESS, 97, 0);

		for (int i = 0; i < 9; i++) {
			if (i < inputs.size()) {
				widgets.addSlot(inputs.get(i), i % 3 * 18, i / 3 * 18);
			} else {
				widgets.addSlot(EmiStack.of(ItemStack.EMPTY), i % 3 * 18, i / 3 * 18);
			}
		}

		widgets.addSlot(outputs.get(0), 92, 14).large(true).recipeContext(this);
	}

}
