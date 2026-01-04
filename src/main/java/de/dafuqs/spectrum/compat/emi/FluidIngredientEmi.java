package de.dafuqs.spectrum.compat.emi;

import dev.emi.emi.api.stack.*;
import net.neoforged.neoforge.fluids.crafting.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FluidIngredientEmi {
    // ALWAYS pass FluidIngredient.EMPTY INSTEAD OF null
    // DO NOT pass(OR EVEN USE AT ALL) hacked-in weird Ingredients.
    // Only use ones provided by FluidIngredient.of() or FluidIngredient.EMPTY.
    public static EmiIngredient into(@NotNull FluidIngredient ingredient) {
        Objects.requireNonNull(ingredient);
        // Return empty stack if ingredient is empty.
        // Semi-redundant: the sole caller of this *checks if input is empty*.
		if (ingredient.isEmpty()) return EmiStack.EMPTY;
		
		return EmiIngredient.of(Arrays.stream(ingredient.getStacks()).map(stack -> EmiStack.of(stack.getFluid(), stack.getAmount())).toList());
    }
}
