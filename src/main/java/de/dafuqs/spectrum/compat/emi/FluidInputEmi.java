package de.dafuqs.spectrum.compat.emi;

import de.dafuqs.spectrum.helpers.FluidInput;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FluidInputEmi {
    // don't pass null, pass FluidInput.EMPTY.
    public static EmiIngredient into(@NotNull FluidInput input) {
        Objects.requireNonNull(input);
        // return empty stack if input is empty.
        // Redundant: the sole caller of this *checks if input is empty.*
        if (input == FluidInput.EMPTY) return EmiStack.EMPTY;
        if (input.fluid().isPresent())
            return EmiStack.of(input.fluid().get());
        // NOTE: EMI has a rather... involved and complicated way of dealing
        // with tags. Not even going to question any of that insanity. Anyway,
        // imitating behavior of EmiStack.of(fluid) by using amount 0 (was 1).
        if (input.tag().isPresent())
            return EmiIngredient.of(input.tag().get(), 0);

        // UNREACHABLE under normal circumstances!
        throw new AssertionError("Invalid FluidInput object");
    }
}
