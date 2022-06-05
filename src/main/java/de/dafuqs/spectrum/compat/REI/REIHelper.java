package de.dafuqs.spectrum.compat.REI;

import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class REIHelper {
	
	public static EntryIngredient ofIngredientStack(@NotNull IngredientStack ingredientStack) {
		if (ingredientStack.isEmpty()) return EntryIngredient.empty();
		List<ItemStack> matchingStacks = ingredientStack.getStacks();
		if (matchingStacks.isEmpty()) return EntryIngredient.empty();
		
		for (ItemStack stack : matchingStacks) {
			stack.setCount(ingredientStack.getCount());
		}
		
		if (matchingStacks.size() == 1) return EntryIngredient.of(EntryStacks.of(matchingStacks.get(0)));
		EntryIngredient.Builder result = EntryIngredient.builder(matchingStacks.size());
		for (ItemStack matchingStack : matchingStacks) {
			if (!matchingStack.isEmpty()) {
				result.add(EntryStacks.of(matchingStack));
			}
		}
		return result.build();
	}
	
}
