package de.dafuqs.spectrum.api.item;

import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;

public interface InventoryInsertionAcceptor {
	
	boolean acceptsItemStack(ItemStack inventoryInsertionAcceptorStack, ItemStack itemStackToAccept);
	
	/**
	 * @return The amount that could not be accepted
	 */
	int acceptItemStack(ItemStack inventoryInsertionAcceptorStack, ItemStack itemStackToAccept, Player playerEntity);
	
}
