package de.dafuqs.spectrum.compat.emi;

import de.dafuqs.spectrum.recipe.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.stack.*;
import net.minecraft.util.*;

import java.util.*;

public abstract class GatedSpectrumEmiRecipe<T extends GatedRecipe> extends SpectrumEmiRecipe {
	public final T recipe;
	
	public GatedSpectrumEmiRecipe(EmiRecipeCategory category, T recipe, int width, int height) {
		this(category, null, recipe, width, height);
	}
	
	public GatedSpectrumEmiRecipe(EmiRecipeCategory category, Identifier unlockIdentifier, T recipe, int width, int height) {
		super(category, unlockIdentifier, recipe.getId(), width, height);
		this.recipe = recipe;
		input = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
		if (!recipe.getOutput(null).isEmpty()) {
			output = List.of(EmiStack.of(recipe.getOutput(null)));
		}
	}
	
	@Override
	public boolean isUnlocked() {
		return hasAdvancement(recipe.getRequiredAdvancementIdentifier()) && super.isUnlocked();
	}
	
	@Override
	public boolean hideCraftable() {
		return recipe.isSecret() || super.hideCraftable();
	}
}
