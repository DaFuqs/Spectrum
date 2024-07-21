package de.dafuqs.spectrum.api.item;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public interface SleepStatusAffectingItem {

    float getSleepFlatModifier(ServerPlayerEntity player, ItemStack stack);

    float getSleepMultModifier(ServerPlayerEntity player, ItemStack stack);
}
