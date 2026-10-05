package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.api.recipe.*;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.neoforged.neoforge.fluids.crafting.*;

import java.util.*;
import java.util.stream.*;

public class REIHelper {
	
	public static List<EntryIngredient> toEntryIngredients(List<IngredientStack> ingredientStacks) {
		return ingredientStacks.stream().map(REIHelper::ofIngredientStack).collect(Collectors.toCollection(ArrayList::new));
	}
	
	public static EntryIngredient ofIngredientStack(IngredientStack ingredientStack) {
		return EntryIngredients.ofItemStacks(ingredientStack.getItems().toList());
	}
	
	public static EntryIngredient ofFluidIngredient(FluidIngredient fluidIngredient) {
		return EntryIngredient.of(Arrays.stream(fluidIngredient.getStacks()).map(stack -> EntryStacks.of(stack.getFluid(), stack.getAmount())).toList());
	}
	
}
