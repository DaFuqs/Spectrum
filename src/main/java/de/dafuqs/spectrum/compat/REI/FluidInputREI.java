package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.helpers.FluidInput;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.fluid.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.minecraft.fluid.Fluids.EMPTY;

public class FluidInputREI {
    // ALWAYS pass FluidInput.EMPTY INSTEAD OF null
    // DO NOT pass(OR EVEN USE AT ALL) hacked-in weird Inputs.
    // Only use ones provided by FluidInput.of() or FluidInput.EMPTY.
    public static EntryIngredient into(@NotNull FluidInput input) {
        Objects.requireNonNull(input);
        // Return empty stack if input is empty.
        // Semi-redundant: the sole caller of this *checks if input is empty*.
        if (input == FluidInput.EMPTY)
            return EntryIngredients.of(EMPTY);

        if (input.fluid().isPresent())
            return EntryIngredients.of(input.fluid().get());
        // NOTE: Using EMIs fluid filter for parity.
        if (input.tag().isPresent())
            return EntryIngredients.ofTag(input.tag().get(),
                    (entry) -> {
                        Fluid fluid = entry.value();
                        if (!fluid.getDefaultState().isStill())
                            return EntryStacks.of(EMPTY);
                        return EntryStacks.of(fluid);
                    });

        // UNREACHABLE under normal circumstances!
        throw new AssertionError("Invalid FluidInput object");
    }
}
