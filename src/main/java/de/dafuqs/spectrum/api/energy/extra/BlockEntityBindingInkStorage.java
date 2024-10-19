package de.dafuqs.spectrum.api.energy.extra;

import de.dafuqs.spectrum.api.energy.InkStorageBlockEntity;

public class BlockEntityBindingInkStorage extends BindingInkStorage {
    private final InkStorageBlockEntity<?> blockEntity;

    public BlockEntityBindingInkStorage(InkStorageBlockEntity<?> bindTo) {
        super(bindTo.getEnergyStorage());
        this.blockEntity = bindTo;
    }

    @Override
    public void markDirty() {
        blockEntity.setInkDirty();
    }
}
