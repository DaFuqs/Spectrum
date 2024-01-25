package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.helpers.FluidInput;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.item.ItemStack.EMPTY;

public class FluidInputREI {
    // don't pass null, pass FluidInput.EMPTY.
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
        if (input.tag().isPresent()) {
            // NOTE: Using ObjectOpenHashSet, there may be a better type
            // or mechanism for sorting out distinct buckets.
            ObjectOpenHashSet<Item> unique = new ObjectOpenHashSet<>();
            return EntryIngredients.ofTag(input.tag().get(),
                    (entry) -> {
                        Item bucket = entry.value().getBucketItem();
                        if(!unique.add(bucket)) return EntryStacks.of(EMPTY);
                        return EntryStacks.of(bucket);
                    });
        }
        // UNREACHABLE under normal circumstances!
        throw new AssertionError("Invalid FluidInput object");
    }
}
