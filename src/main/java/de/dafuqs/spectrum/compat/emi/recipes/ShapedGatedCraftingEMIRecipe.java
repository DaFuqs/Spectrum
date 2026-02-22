package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.recipe.crafting.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.render.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.item.*;

public class ShapedGatedCraftingEMIRecipe extends GatedSpectrumEmiRecipe<ShapedGatedCraftingRecipe> {

	public ShapedGatedCraftingEMIRecipe(ShapedGatedCraftingRecipe recipe) {
		super(VanillaEmiRecipeCategories.CRAFTING, recipe, 118, 54);
	}

	public boolean canFit(int width, int height) {
		if (inputs.size() > 9) {
			return false;
		}
		for (int i = 0; i < inputs.size(); i++) {
			int x = i % 3;
			int y = i / 3;
			if (!inputs.get(i).isEmpty() && (x >= width || y >= height)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 60, 18);

		int sOff = 0;
		if (canFit(1, 3)) {
			sOff -= 1;
		}
		if (canFit(3, 1)) {
			sOff -= 3;
		}
		for (int i = 0; i < 9; i++) {
			int s = i + sOff;
			if (s >= 0 && s < inputs.size()) {
				widgets.addSlot(inputs.get(s), i % 3 * 18, i / 3 * 18);
			} else {
				widgets.addSlot(EmiStack.of(ItemStack.EMPTY), i % 3 * 18, i / 3 * 18);
			}
		}

		widgets.addSlot(outputs.get(0), 92, 14).large(true).recipeContext(this);
	}

}
