package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.compat.emi.SpectrumRecipeCategories;
import de.dafuqs.spectrum.compat.emi.SpectrumEmiRecipe;
import de.dafuqs.spectrum.recipe.ink_converting.InkConvertingRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.text.Text;

public class InkConvertingEmiRecipe extends SpectrumEmiRecipe<InkConvertingRecipe> {

	public InkConvertingEmiRecipe(InkConvertingRecipe recipe) {
		super(SpectrumRecipeCategories.INK_CONVERTING, InkConvertingRecipe.UNLOCK_IDENTIFIER, recipe, 136, 20);
	}

	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addSlot(input.get(0), 0, 1);

		widgets.addTexture(EmiTexture.EMPTY_ARROW, 22, 1);

		// output amount & required time
		Text colorText = Text.translatable("container.spectrum.rei.ink_converting.color", recipe.getInkColor().getName());
		Text amountText = Text.translatable("container.spectrum.rei.ink_converting.amount", recipe.getInkAmount());
		widgets.addText(colorText, 50, 1, 0x3f3f3f, false);
		widgets.addText(amountText, 50, 11, 0x3f3f3f, false);
	}
}
