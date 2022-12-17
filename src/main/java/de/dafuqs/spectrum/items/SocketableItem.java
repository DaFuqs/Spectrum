package de.dafuqs.spectrum.items;

import net.minecraft.item.ItemStack;

public interface SocketableItem {
	
	/**
	 *
	 * @param socketableStack
	 * @param stackToSocket
	 * @return ItemStack.EMPTY if stackToSocket can not be used on this
	 */
	ItemStack socket(ItemStack socketableStack, ItemStack stackToSocket);
	
}
