package de.dafuqs.spectrum.api.energy.extra;

import de.dafuqs.spectrum.api.energy.InkStorage;
import de.dafuqs.spectrum.api.energy.InkStorageItem;
import net.minecraft.item.ItemStack;

public class ItemBindingInkStorage extends BindingInkStorage {
    private final InkStorageItem<?> itemStorage;
    private final ItemStack toUpdate;

    public ItemBindingInkStorage(InkStorageItem<?> bindTo, ItemStack toUpdate) {
        super(bindTo.getEnergyStorage(toUpdate));
        this.itemStorage = bindTo;
        this.toUpdate = toUpdate;
    }

    @Override
    public void markDirty() {
        itemStorage.setEnergyStorage(toUpdate, (InkStorage)this.wrapped);
    }
}
