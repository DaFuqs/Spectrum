package de.dafuqs.spectrum.api.item;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;

public interface MergeableItem {

    ItemStack getResult(ServerPlayerEntity player, ItemStack firstHalf, ItemStack secondHalf);

    boolean canMerge(ServerPlayerEntity player, ItemStack parent, ItemStack other);

    default boolean verify(ItemStack parent, ItemStack other) {
        var parNbt = parent.getOrCreateNbt();
        var otherNbt = other.getOrCreateNbt();
        if (parNbt.contains("pairSignature") && otherNbt.contains("pairSignature"))
            return parNbt.getLong("pairSignature") == otherNbt.getLong("pairSignature");
        return false;
    }

    SoundEvent getMergeSound();
}
