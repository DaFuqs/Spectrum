package de.dafuqs.spectrum.compat.emi;

import de.dafuqs.spectrum.api.recipe.*;
import dev.emi.emi.api.stack.*;
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
        if (ingredient == FluidIngredient.EMPTY) return EmiStack.EMPTY;
        if (ingredient.fluid().isPresent())
            return EmiStack.of(ingredient.fluid().get());
        // NOTE: imitating the behavior of EmiStack.of(fluid)
        // by changing the amount to 0, instead of the 1 for tags.
        if (ingredient.tag().isPresent())
            return EmiIngredient.of(ingredient.tag().get(), 0);

        // UNREACHABLE under normal circumstances!
        throw new AssertionError("Invalid FluidIngredient object");
    }
}
