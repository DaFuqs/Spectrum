package de.dafuqs.spectrum.api.block;

import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.minecraft.core.*;
import net.minecraft.world.item.*;

public interface FluidStackInventory extends ImplementedInventory {
	
	/**
	 * Retrieves the item list of this inventory.
	 * Must return the same instance every time it's called.
	 */
	NonNullList<ItemStack> getItems();
	
	SingleVariantStorage<FluidVariant> getFluidStorage();
	
	/**
	 * Creates an inventory from the item list.
	 */
	static FluidStackInventory of(NonNullList<ItemStack> items, SingleVariantStorage<FluidVariant> fluid) {
		return new FluidStackInventory() {
			@Override
			public NonNullList<ItemStack> getItems() {
				return items;
			}
			
			@Override
			public SingleVariantStorage<FluidVariant> getFluidStorage() {
				return fluid;
			}
		};
	}
	
}