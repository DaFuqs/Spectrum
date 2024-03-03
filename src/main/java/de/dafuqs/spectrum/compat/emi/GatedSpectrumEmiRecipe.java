package de.dafuqs.spectrum.compat.emi;

import de.dafuqs.spectrum.api.recipe.*;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.stack.*;
import net.minecraft.util.*;

import java.util.*;

public abstract class GatedSpectrumEmiRecipe<T extends GatedRecipe> extends SpectrumEmiRecipe {
	public final T recipe;

	public GatedSpectrumEmiRecipe(EmiRecipeCategory category, T recipe, int width, int height) {
		super(category, recipe.getRecipeTypeUnlockIdentifier(), recipe.getId(), width, height);
		this.recipe = recipe;
		this.outputs = List.of(EmiStack.of(recipe.getOutput(getRegistryManager())));
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