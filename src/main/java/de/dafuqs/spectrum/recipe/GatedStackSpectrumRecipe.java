package de.dafuqs.spectrum.recipe;

import de.dafuqs.spectrum.api.recipe.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;

public abstract class GatedStackSpectrumRecipe<C extends RecipeInput> extends GatedSpectrumRecipe<C> {
	
	protected GatedStackSpectrumRecipe(String group, Optional<ResourceLocation> requiredAdvancement, Optional<ResourceLocation> revealSecretAdvancement, List<ItemStack> additionalResults) {
		super(group, requiredAdvancement, revealSecretAdvancement, additionalResults);
	}
	
	public abstract List<IngredientStack> getIngredientStacks();
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		List<IngredientStack> ingredientStacks = getIngredientStacks();
		NonNullList<Ingredient> ingredients = NonNullList.createWithCapacity(ingredientStacks.size());
		for (IngredientStack stack : ingredientStacks) {
			ingredients.add(stack.getIngredient());
		}
		return ingredients;
	}
	
	protected boolean matchIngredientStacksExclusively(RecipeInput recipeInput, List<IngredientStack> ingredientStacks) {
		// does the recipe fit into that inventory in the first place?
		if (recipeInput.size() < ingredientStacks.size()) {
			return false;
		}
		
		// collect all non-empty stacks
		List<ItemStack> nonEmptyStacks = new ArrayList<>();
		for (int i = 0; i < recipeInput.size(); i++) {
			ItemStack stack = recipeInput.getItem(i);
			if (!stack.isEmpty()) {
				nonEmptyStacks.add(stack);
			}
		}
		// do we have an exact count match?
		if (nonEmptyStacks.size() != ingredientStacks.size()) {
			return false;
		}
		
		// match each IngredientStack exclusively
		ObjectArraySet<IngredientStack> ingredients = ObjectArraySet.of(ingredientStacks.toArray(new IngredientStack[0]));
		for (ItemStack stack : nonEmptyStacks) {
			IngredientStack foundStack = null;
			for (IngredientStack ingredientStack : ingredients) {
				if (ingredientStack.test(stack)) {
					foundStack = ingredientStack;
					break;
				}
			}
			if (foundStack == null) {
				return false;
			}
			ingredients.remove(foundStack);
			if (ingredients.isEmpty()) {
				break;
			}
		}
		
		return true;
	}
	
	protected boolean matchIngredientStacksExclusively(RecipeInput recipeInput, List<IngredientStack> ingredients, int[] slots) {
		int inputStackCount = 0;
		for (int slot : slots) {
			if (!recipeInput.getItem(slot).isEmpty()) {
				inputStackCount++;
			}
		}
		if (inputStackCount != ingredients.size()) {
			return false;
		}
		
		for (IngredientStack ingredient : ingredients) {
			boolean found = false;
			for (int slot : slots) {
				if (ingredient.test(recipeInput.getItem(slot))) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		
		return true;
	}
	
}
