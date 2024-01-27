package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.helpers.FluidIngredient;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.fluid.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.minecraft.fluid.Fluids.EMPTY;

public class FluidIngredientREI {
    // ALWAYS pass FluidIngredient.EMPTY INSTEAD OF null
    // DO NOT pass(OR EVEN USE AT ALL) hacked-in weird Ingredients.
    // Only use ones provided by FluidIngredient.of() or FluidIngredient.EMPTY.
    public static EntryIngredient into(@NotNull FluidIngredient ingredient) {
        Objects.requireNonNull(ingredient);
        // Return empty stack if ingredient is empty.
        // Semi-redundant: the sole caller of this *checks if input is empty*.
        if (ingredient == FluidIngredient.EMPTY)
            return EntryIngredients.of(EMPTY);

        if (ingredient.fluid().isPresent())
            return EntryIngredients.of(ingredient.fluid().get());
        // NOTE: Using EMIs fluid filter for parity.
        if (ingredient.tag().isPresent())
            return EntryIngredients.ofTag(ingredient.tag().get(),
                    (entry) -> {
                        Fluid fluid = entry.value();
                        if (!fluid.getDefaultState().isStill())
                            return EntryStacks.of(EMPTY);
                        return EntryStacks.of(fluid);
                    });

        // UNREACHABLE under normal circumstances!
        throw new AssertionError("Invalid FluidIngredient object");
    }
}
