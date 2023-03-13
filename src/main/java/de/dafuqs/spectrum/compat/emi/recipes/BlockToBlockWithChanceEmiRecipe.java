package de.dafuqs.spectrum.compat.emi.recipes;

import java.util.List;

import de.dafuqs.spectrum.compat.emi.SpectrumBaseEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;

public class BlockToBlockWithChanceEmiRecipe extends SpectrumBaseEmiRecipe {

	public BlockToBlockWithChanceEmiRecipe(EmiRecipeCategory category, Identifier id, EmiIngredient in, EmiStack out, Identifier unlock) {
		super(category, unlock, id, 78, 26);
		this.input = List.of(in);
		this.output = List.of(out);
	}

	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 23, 4);
		widgets.addSlot(input.get(0), 0, 4);
		widgets.addSlot(output.get(0), 52, 0).output(true).recipeContext(this);
	}
}
