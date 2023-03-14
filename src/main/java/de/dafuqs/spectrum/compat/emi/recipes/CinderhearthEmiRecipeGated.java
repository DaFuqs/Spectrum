package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.recipe.cinderhearth.*;
import dev.emi.emi.api.render.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.TextWidget.*;
import dev.emi.emi.api.widget.*;

public class CinderhearthEmiRecipeGated extends GatedSpectrumEmiRecipe<CinderhearthRecipe> {
	
	public CinderhearthEmiRecipeGated(CinderhearthRecipe recipe) {
		super(SpectrumEmiRecipeCategories.CINDERHEARTH, CinderhearthRecipe.UNLOCK_IDENTIFIER, recipe, 112, 48);
		output = recipe.getOutputsWithChance().stream().map(p -> EmiStack.of(p.getLeft()).setChance(p.getRight())).toList();
	}
	
	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		int xOff = 5;
		widgets.addSlot(input.get(0), xOff, 0);

		widgets.addFillingArrow(22 + xOff, 9, recipe.getCraftingTime() * 50);

		widgets.addTexture(EmiTexture.FULL_FLAME, 1 + xOff, 20);
		widgets.addAnimatedTexture(EmiTexture.FULL_FLAME, 1 + xOff, 20, 10000, false, true, false);

		for (int i = 0; i < 2; i++) {
			if (i >= output.size()) {
				widgets.addSlot(EmiStack.EMPTY, 50 + i * 26 + xOff, 5).output(true);
			} else {
				widgets.addSlot(output.get(i), 50 + i * 26 + xOff, 5).output(true).recipeContext(this);
			}
		}

		widgets.addText(getCraftingTimeText(recipe.getCraftingTime(), recipe.getExperience()), width / 2, 36, 0x3f3f3f, false).horizontalAlign(Alignment.CENTER);
	}
}
