package de.dafuqs.spectrum.compat.REI;

import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.util.*;
import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.fluids.crafting.*;
import org.jspecify.annotations.*;

import static net.minecraft.world.level.material.Fluids.*;

public class FluidIngredientREI {
	
	public static EntryIngredient into(@NonNull FluidIngredient ingredient) {
        // Return empty stack if ingredient is empty.
        // Semi-redundant: the sole caller of this *checks if input is empty*.
		if (ingredient == FluidIngredient.empty())
            return EntryIngredients.of(EMPTY);
		
		FluidStack[] stacks = ingredient.getStacks();
		EntryIngredient.Builder builder = EntryIngredient.builder(stacks.length);
		
		for (var stack : stacks) {
			builder.add(EntryStacks.of(stack.getFluid(), stack.getAmount()));
		}
		
		return builder.build();
    }
}
