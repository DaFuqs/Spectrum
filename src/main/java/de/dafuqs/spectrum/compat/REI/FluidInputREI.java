package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.helpers.FluidInput;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.fluid.Fluids;
import org.jetbrains.annotations.NotNull;

public class FluidInputREI {

    public static EntryIngredient into(@NotNull FluidInput input) {
        // TEMP: Imitating old behavior. Not sure why *specifically* buckets,
        // but will do anyway.

        // return empty stack if input is empty.
        // Redundant: the sole caller of this *checks if input is empty.*
        if (input == FluidInput.EMPTY)
            return EntryIngredients.of(Fluids.EMPTY.getBucketItem());

        if (input.fluid().isPresent())
            return EntryIngredients.of(input.fluid().get().getBucketItem());

        // NOTE: would use ofFluidTag if not for the buckets.
        if (input.tag().isPresent())
            return EntryIngredients.ofTag(input.tag().get(),
                    (entry) -> EntryStacks.of(entry.value().getBucketItem()));

        // UNREACHABLE under normal circumstances!
        throw new AssertionError("Invalid FluidInput object");
    }
}
