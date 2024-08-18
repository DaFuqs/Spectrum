package de.dafuqs.spectrum.api.block;

import net.fabricmc.fabric.api.transfer.v1.fluid.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.minecraft.item.*;
import net.minecraft.util.collection.*;

public interface FluidStackInventory extends ImplementedInventory {
	
	/**
	 * Retrieves the item list of this inventory.
	 * Must return the same instance every time it's called.
	 */
	DefaultedList<ItemStack> getItems();
	
	SingleVariantStorage<FluidVariant> getFluidStorage();
	
	/**
	 * Creates an inventory from the item list.
	 */
	static FluidStackInventory of(DefaultedList<ItemStack> items, SingleVariantStorage<FluidVariant> fluid) {
		return new FluidStackInventory() {
			@Override
			public DefaultedList<ItemStack> getItems() {
				return items;
			}
			
			@Override
			public SingleVariantStorage<FluidVariant> getFluidStorage() {
				return fluid;
			}
		};
	}
	
}