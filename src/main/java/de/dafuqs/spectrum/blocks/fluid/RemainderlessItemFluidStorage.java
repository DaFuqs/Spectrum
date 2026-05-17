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
		if (!resource.equals(containedFluid) || !context.getItemVariant().isOf(fullItem)) return 0;
		StoragePreconditions.notNegative(maxAmount);
		
		long extractedAmount = Math.min(maxAmount, getAmount());
		extractedAmount -= (extractedAmount % containedAmount);
		if (extractedAmount == 0) return 0;
		
		return containedAmount * context.extract(context.getItemVariant(), extractedAmount / containedAmount, transaction);
	};

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
            return containedAmount * context.getAmount();
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
