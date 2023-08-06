package de.dafuqs.spectrum.recipe;

import net.id.incubus_core.recipe.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;

import java.util.*;

public abstract class GatedStackSpectrumRecipe extends GatedSpectrumRecipe {
	
	protected GatedStackSpectrumRecipe(Identifier id, String group, boolean secret, Identifier requiredAdvancementIdentifier) {
		super(id, group, secret, requiredAdvancementIdentifier);
	}
	
	public abstract List<IngredientStack> getIngredientStacks();
	
	
	/**
	 * Gets the recipes required ingredients
	 *
	 * @deprecated should not be used. Instead, use getIngredientStacks(), which includes item counts
	 */
	@Override
	@Deprecated
	public DefaultedList<Ingredient> getIngredients() {
		return IngredientStack.listIngredients(getIngredientStacks());
	}
	
}
