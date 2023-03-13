package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.compat.emi.SpectrumRecipeCategories;
import de.dafuqs.spectrum.compat.emi.SpectrumEmiRecipe;
import de.dafuqs.spectrum.recipe.spirit_instiller.SpiritInstillerRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

public class SpiritInstillingEmiRecipe extends SpectrumEmiRecipe<SpiritInstillerRecipe> {

	public SpiritInstillingEmiRecipe(SpiritInstillerRecipe recipe) {
		super(SpectrumRecipeCategories.SPIRIT_INSTILLER, SpiritInstillerRecipe.UNLOCK_IDENTIFIER, recipe, 116, 48);
		input = recipe.getIngredientStacks().stream().map(s -> EmiIngredient.of(s.getStacks().stream().map(EmiStack::of).toList())).toList();
	}

	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addSlot(input.get(0), 0, 0);
		widgets.addSlot(input.get(1), 20, 0);
		widgets.addSlot(input.get(2), 40, 0);

		widgets.addSlot(EmiStack.of(SpectrumBlocks.ITEM_BOWL_CALCITE), 0, 17).drawBack(false);
		widgets.addSlot(EmiStack.of(SpectrumBlocks.SPIRIT_INSTILLER), 20, 17).drawBack(false);
		widgets.addSlot(EmiStack.of(SpectrumBlocks.ITEM_BOWL_CALCITE), 40, 17).drawBack(false);

		if (!recipe.getOutput().isEmpty()) {
			widgets.addSlot(output.get(0), 90, 4).output(true).recipeContext(this);
		}

		widgets.addFillingArrow(60, 9, recipe.getCraftingTime() * 50);

		widgets.addText(getCraftingTimeText(recipe.getCraftingTime(), recipe.getExperience()), 0, 39, 0x3f3f3f, false);
	}
}
