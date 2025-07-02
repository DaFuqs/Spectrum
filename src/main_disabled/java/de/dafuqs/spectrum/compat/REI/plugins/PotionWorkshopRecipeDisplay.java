package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.*;
import de.dafuqs.spectrum.recipe.potion_workshop.*;
import me.shedaniel.rei.api.common.display.basic.*;
import me.shedaniel.rei.api.common.util.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;

public abstract class PotionWorkshopRecipeDisplay extends GatedSpectrumDisplay {
	
	protected final int craftingTime;
	
	/**
	 * When using the REI recipe functionality
	 *
	 * @param recipe The recipe
	 */
	public PotionWorkshopRecipeDisplay(RecipeHolder<? extends PotionWorkshopRecipe> recipe) {
		super(recipe, REIHelper.toEntryIngredients(recipe.value().getIngredientStacks()), Collections.singletonList(EntryIngredients.of(recipe.value().getResultItem(BasicDisplay.registryAccess()))));
		this.craftingTime = recipe.value().getCraftingTime();
	}
	
}