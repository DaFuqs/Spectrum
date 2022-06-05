package de.dafuqs.spectrum.inventories.slots;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class StackFilterSlot extends Slot {
	
	private final Item acceptedItem;
	
	public StackFilterSlot(Inventory inventory, int index, int x, int y, Item acceptedItem) {
		super(inventory, index, x, y);
		this.acceptedItem = acceptedItem;
	}
	
	public boolean canInsert(ItemStack stack) {
		return stack.getItem().equals(acceptedItem);
	}
	
}
