package de.dafuqs.spectrum.interfaces;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface InventoryInsertionAcceptor {
	
	boolean acceptsItemStack(ItemStack inventoryInsertionAcceptorStack, ItemStack itemStackToAccept);
	
	/**
	 * @param inventoryInsertionAcceptorStack
	 * @param itemStackToAccept
	 * @return The amount that could not be accepted
	 */
	int acceptItemStack(ItemStack inventoryInsertionAcceptorStack, ItemStack itemStackToAccept, PlayerEntity playerEntity);
	
}
