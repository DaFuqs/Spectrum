package de.dafuqs.spectrum.compat.emi;

import java.util.List;

import de.dafuqs.spectrum.recipe.GatedRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.util.Identifier;

public abstract class SpectrumEmiRecipe<T extends GatedRecipe> extends SpectrumBaseEmiRecipe {
	public final T recipe;

	public SpectrumEmiRecipe(EmiRecipeCategory category, T recipe, int width, int height) {
		this(category, null, recipe, width, height);
	}

	public SpectrumEmiRecipe(EmiRecipeCategory category, Identifier unlock, T recipe, int width, int height) {
		super(category, unlock, recipe.getId(), width, height);
		this.recipe = recipe;
		input = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
		if (!recipe.getOutput().isEmpty()) {
			output = List.of(EmiStack.of(recipe.getOutput()));
		}
	}

	@Override
	public boolean isUnlocked() {
		return hasAdvancement(recipe.getRequiredAdvancementIdentifier()) && super.isUnlocked();
	}
}
