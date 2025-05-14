package de.dafuqs.spectrum.blocks.fluid;

import net.fabricmc.fabric.api.transfer.v1.context.*;
import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.minecraft.world.item.*;

public class RemainderlessItemFluidStorage implements ExtractionOnlyStorage<FluidVariant>, SingleSlotStorage<FluidVariant> {

    private final ContainerItemContext context;
    private final Item fullItem;
    private final FluidVariant containedFluid;
    private final long containedAmount;


    public RemainderlessItemFluidStorage(ContainerItemContext context, FluidVariant containedFluid, long containedAmount) {
        StoragePreconditions.notBlankNotNegative(containedFluid, containedAmount);

        this.context = context;
        this.fullItem = context.getItemVariant().getItem();
        this.containedFluid = containedFluid;
        this.containedAmount = containedAmount;
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        // If the context's item is not fullItem anymore, can't extract!
        if (!context.getItemVariant().isOf(fullItem)) return 0;

        // Make sure that the fluid and the amount match.
        if (resource.equals(containedFluid) && maxAmount >= containedAmount) {
            // If that's ok, just convert one of the full item into the empty item, copying the nbt.


            if (context.extract(context.getItemVariant(), 1, transaction) == 1) {
                // Conversion ok!
                return containedAmount;
            }
        }
        return 0;
    }

    @Override
    public boolean isResourceBlank() {
        return getResource().isBlank();
    }

    @Override
    public FluidVariant getResource() {
        if (context.getItemVariant().isOf(fullItem)) {
            return containedFluid;
        } else {
            return FluidVariant.blank();
        }
    }

    @Override
    public long getAmount() {
        if (context.getItemVariant().isOf(fullItem)) {
            return containedAmount;
        } else {
            return 0;
        }
    }

    @Override
    public long getCapacity() {
        return getAmount();
    }

    @Override
    public String toString() {
        return "FullItemFluidStorage[context=%s, fluid=%s, amount=%d]"
                .formatted(context, containedFluid, containedAmount);
    }
}
