package de.dafuqs.spectrum.compat.emi.recipes;

import de.dafuqs.spectrum.compat.emi.*;
import de.dafuqs.spectrum.recipe.ink_converting.*;
import dev.emi.emi.api.render.*;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.api.widget.*;
import net.minecraft.text.*;

public class InkConvertingEmiRecipeGated extends GatedSpectrumEmiRecipe<InkConvertingRecipe> {
	
	public InkConvertingEmiRecipeGated(InkConvertingRecipe recipe) {
		super(SpectrumEmiRecipeCategories.INK_CONVERTING, recipe, 136, 20);
		this.inputs = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
	}
	
	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addSlot(inputs.get(0), 0, 1);
		
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 22, 1);

		// output amount & required time
		Text colorText = Text.translatable("container.spectrum.rei.ink_converting.color", recipe.getInkColor().getName());
		Text amountText = Text.translatable("container.spectrum.rei.ink_converting.amount", recipe.getInkAmount());
		widgets.addText(colorText, 50, 1, 0x3f3f3f, false);
		widgets.addText(amountText, 50, 11, 0x3f3f3f, false);
	}
}
