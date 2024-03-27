package de.dafuqs.spectrum.api.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;

public interface MergeableItem {

    ItemStack getResult(ServerPlayerEntity player, ItemStack firstHalf, ItemStack secondHalf);

    boolean canMerge(ServerPlayerEntity player, ItemStack parent, ItemStack other);

    default boolean verify(ItemStack parent, ItemStack other) {
        if (!EnchantmentHelper.get(parent).equals(EnchantmentHelper.get(other))) {
            return false;
        }

        var parNbt = parent.getOrCreateNbt();
        var otherNbt = other.getOrCreateNbt();
        if (parNbt.contains("pairSignature") && otherNbt.contains("pairSignature"))
            return parNbt.getLong("pairSignature") == otherNbt.getLong("pairSignature");
        return false;
    }

    SoundProvider getMergeSound();
}
