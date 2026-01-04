package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.api.recipe.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.stream.*;

public class REIHelper {
	
	public static List<EntryIngredient> toEntryIngredients(List<IngredientStack> ingredientStacks) {
		return ingredientStacks.stream().map(REIHelper::ofIngredientStack).collect(Collectors.toCollection(ArrayList::new));
	}
	
	public static EntryIngredient ofIngredientStack(@NotNull IngredientStack ingredientStack) {
		return EntryIngredients.ofItemStacks(ingredientStack.getMatchingStacks());
	}
	
	public static EntryIngredient ofFluidIngredient(FluidIngredient fluidIngredient) {
		if (fluidIngredient.isTag()) {
			return EntryIngredients.ofFluidTag(fluidIngredient.tag().get());
		}
		return EntryIngredients.of(fluidIngredient.fluid().get());
	}
	
}
