package de.dafuqs.spectrum.api.damage_type;

import net.minecraft.item.ItemStack;

import java.util.Optional;

public interface StackTracking {

    Optional<ItemStack> spectrum$getTrackedStack();

    void spectrum$setTrackedStack(ItemStack stack);
}
