package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.TextWidget.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.fluid.*;
import net.minecraft.text.*;

import java.util.*;

public class TitrationBarrelEmiRecipeGated extends GatedSpectrumEmiRecipe<ITitrationBarrelRecipe> {
	
	public TitrationBarrelEmiRecipeGated(ITitrationBarrelRecipe recipe) {
		super(SpectrumEmiRecipeCategories.TITRATION_BARREL, TitrationBarrelRecipe.UNLOCK_ADVANCEMENT_IDENTIFIER, recipe, 136, 50);
		if (recipe.getFluidInput() != Fluids.EMPTY) {
			input = new ArrayList<>();
			input.add(EmiIngredient.of(List.of(FluidEmiStack.of(recipe.getFluidInput()))));
			input.addAll(recipe.getIngredientStacks().stream().map(s -> EmiIngredient.of(s.getStacks().stream().map(EmiStack::of).toList())).toList());
		}
		output = buildOutputs(recipe); // TODO: only the first ever will be shown `output.get(0)`
	}
	
	private static List<EmiStack> buildOutputs(ITitrationBarrelRecipe recipe) {
		if (recipe instanceof TitrationBarrelRecipe titrationBarrelRecipe && titrationBarrelRecipe.getFermentationData() != null) {
			return titrationBarrelRecipe.getOutputVariations(TitrationBarrelRecipe.FERMENTATION_DURATION_DISPLAY_TIME_MULTIPLIERS).stream().map(EmiStack::of).toList();
		}
		return List.of(EmiStack.of(recipe.getOutput()));
	}
	
	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		// input slots
		int startX = Math.max(10, 40 - input.size() * 10);
		int startY = (input.size() > 3 ? 0 : 10);
		for (int i = 0; i < input.size(); i++) {
			int x = startX + (i % 3) * 20;
			int y = startY + (i / 3) * 20;
			widgets.addSlot(input.get(i), x, y);
		}

		EmiIngredient tapping = EmiStack.of(recipe.getTappingItem());
		if (tapping.isEmpty()) {
			widgets.addFillingArrow(70, 10, recipe.getMinFermentationTimeHours() * 20 * 50);
		} else {
			widgets.addFillingArrow(70, 2, recipe.getMinFermentationTimeHours() * 20 * 50);
			widgets.addSlot(tapping, 74, 20);
		}

		widgets.addSlot(output.get(0), 100, 5).output(true).recipeContext(this);

		Text text = TitrationBarrelRecipe.getDurationText(recipe.getMinFermentationTimeHours(), recipe.getFermentationData());
		widgets.addText(text, width / 2, 40, 0x3f3f3f, false).horizontalAlign(Alignment.CENTER);
	}
}
