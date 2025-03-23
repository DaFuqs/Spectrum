package de.dafuqs.spectrum.recipe;

import de.dafuqs.matchbooks.recipe.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;

import java.util.*;

public abstract class GatedStackSpectrumRecipe<C extends Inventory> extends GatedSpectrumRecipe<C> {
	
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
	
	protected boolean matchIngredientStacksExclusively(Inventory inv, List<IngredientStack> ingredientStacks) {
		// does the recipe fit into that inventory in the first place?
		if (inv.size() < ingredientStacks.size()) {
			return false;
		}
		
		// collect all non-empty stacks
		List<ItemStack> nonEmptyStacks = new ArrayList<>();
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
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
	
	protected boolean matchIngredientStacksExclusively(Inventory inv, List<IngredientStack> ingredients, int[] slots) {
		int inputStackCount = 0;
		for (int slot : slots) {
			if (!inv.getStack(slot).isEmpty()) {
				inputStackCount++;
			}
		}
		if (inputStackCount != ingredients.size()) {
			return false;
		}
		
		for (IngredientStack ingredient : ingredients) {
			boolean found = false;
			for (int slot : slots) {
				if (ingredient.test(inv.getStack(slot))) {
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
