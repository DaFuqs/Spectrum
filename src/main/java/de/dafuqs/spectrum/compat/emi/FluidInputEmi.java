package de.dafuqs.spectrum.compat.emi;

import de.dafuqs.spectrum.helpers.FluidInput;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FluidInputEmi {
    // ALWAYS pass FluidInput.EMPTY INSTEAD OF null
    // DO NOT pass(OR EVEN USE AT ALL) hacked-in weird Inputs.
    // Only use ones provided by FluidInput.of() or FluidInput.EMPTY.
    public static EmiIngredient into(@NotNull FluidInput input) {
        Objects.requireNonNull(input);
        // Return empty stack if input is empty.
        // Semi-redundant: the sole caller of this *checks if input is empty*.
        if (input == FluidInput.EMPTY) return EmiStack.EMPTY;
        if (input.fluid().isPresent())
            return EmiStack.of(input.fluid().get());
        // NOTE: imitating the behavior of EmiStack.of(fluid)
        // by changing the amount to 0, instead of the 1 for tags.
        if (input.tag().isPresent())
            return EmiIngredient.of(input.tag().get(), 0);

        // UNREACHABLE under normal circumstances!
        throw new AssertionError("Invalid FluidInput object");
    }
}
