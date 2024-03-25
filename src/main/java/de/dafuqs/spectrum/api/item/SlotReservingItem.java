package de.dafuqs.spectrum.api.item;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface SlotReservingItem {

    public static String NBT_STRING = "reserved";

    boolean isReservingSlot(ItemStack stack);

    void markReserved(ItemStack stack, boolean reserved);
}
