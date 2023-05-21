package de.dafuqs.spectrum.compat.REI;

import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.id.incubus_core.recipe.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

public class REIHelper {
	
	public static List<EntryIngredient> toEntryIngredients(List<IngredientStack> ingredientStacks) {
		return ingredientStacks.stream().map(REIHelper::ofIngredientStack).collect(Collectors.toCollection(ArrayList::new));
	}
	
	public static EntryIngredient ofIngredientStack(@NotNull IngredientStack ingredientStack) {
		return EntryIngredients.ofItemStacks(ingredientStack.getStacks());
	}
	
}
