package de.dafuqs.spectrum.compat.emi;

import de.dafuqs.spectrum.recipe.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.stack.*;
import net.minecraft.util.*;

import java.util.*;

public abstract class SpectrumEmiRecipe<T extends GatedRecipe> extends SpectrumBaseEmiRecipe {
	public final T recipe;
	
	public SpectrumEmiRecipe(EmiRecipeCategory category, T recipe, int width, int height) {
		this(category, null, recipe, width, height);
	}
	
	public SpectrumEmiRecipe(EmiRecipeCategory category, Identifier unlockIdentifier, T recipe, int width, int height) {
		super(category, unlockIdentifier, recipe.isSecret(), recipe.getId(), width, height);
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
