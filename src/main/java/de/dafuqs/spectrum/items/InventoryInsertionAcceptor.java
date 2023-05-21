package de.dafuqs.spectrum.items;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;

public interface InventoryInsertionAcceptor {
	
	boolean acceptsItemStack(ItemStack inventoryInsertionAcceptorStack, ItemStack itemStackToAccept);
	
	/**
	 * @param inventoryInsertionAcceptorStack
	 * @param itemStackToAccept
	 * @return The amount that could not be accepted
	 */
	int acceptItemStack(ItemStack inventoryInsertionAcceptorStack, ItemStack itemStackToAccept, PlayerEntity playerEntity);
	
}
