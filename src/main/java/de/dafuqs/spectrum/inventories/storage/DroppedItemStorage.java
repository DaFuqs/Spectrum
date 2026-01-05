package de.dafuqs.spectrum.inventories.storage;

import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.item.base.*;
import net.minecraft.world.item.*;

public class DroppedItemStorage extends SingleItemStorage {

    public DroppedItemStorage(ItemStack itemStack) {
        this.variant = ItemVariant.of(itemStack);
        this.amount = itemStack.getCount();
    }

    @Override
    protected long getCapacity(ItemVariant variant) {
        return 1;
    }
}