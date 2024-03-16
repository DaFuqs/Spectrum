package de.dafuqs.spectrum.api.item;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface SlotReservingItem {

    public static final String NBT_STRING = "reserved";

    boolean isReservingSlot(ItemStack stack);
}
