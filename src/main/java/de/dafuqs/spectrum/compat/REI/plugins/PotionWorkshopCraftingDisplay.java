package de.dafuqs.spectrum.compat.REI.plugins;

import de.dafuqs.spectrum.compat.REI.SpectrumPlugins;
import de.dafuqs.spectrum.recipe.potion_workshop.PotionWorkshopCraftingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class PotionWorkshopCraftingDisplay extends PotionWorkshopRecipeDisplay {
	
	protected final Ingredient baseIngredient;
	protected final boolean consumeBaseIngredient;
	
	/**
	 * When using the REI recipe functionality
	 *
	 * @param recipe The recipe
	 */
	public PotionWorkshopCraftingDisplay(PotionWorkshopCraftingRecipe recipe) {
		super(recipe);
		this.baseIngredient = recipe.getBaseIngredient();
		this.consumeBaseIngredient = recipe.consumesBaseIngredient();
	}
	
	/**
	 * When using Shift click on the plus button in the REI gui to autofill crafting grids
	 */
	public PotionWorkshopCraftingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, PotionWorkshopCraftingRecipe recipe) {
		super(inputs, outputs, recipe);
		this.baseIngredient = recipe.getBaseIngredient();
		this.consumeBaseIngredient = recipe.consumesBaseIngredient();
	}
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return SpectrumPlugins.POTION_WORKSHOP_CRAFTING;
	}
	
	public static Serializer<PotionWorkshopRecipeDisplay> serializer() {
		return Serializer.ofSimple(PotionWorkshopCraftingDisplay::simple).inputProvider(PotionWorkshopRecipeDisplay::getOrganisedInputEntries);
	}
	
	private static @NotNull PotionWorkshopRecipeDisplay simple(List<EntryIngredient> inputs, List<EntryIngredient> outputs, @NotNull Optional<Identifier> identifier) {
		Recipe<?> optionalRecipe = identifier.flatMap(resourceLocation -> RecipeManagerContext.getInstance().getRecipeManager().get(resourceLocation)).orElse(null);
		return new PotionWorkshopCraftingDisplay(inputs, outputs, (PotionWorkshopCraftingRecipe) optionalRecipe);
	}
	
}