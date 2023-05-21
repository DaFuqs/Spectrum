package de.dafuqs.spectrum.inventories.slots;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.screen.slot.*;

public class StackFilterSlot extends Slot {
	
	private final Item acceptedItem;
	
	public StackFilterSlot(Inventory inventory, int index, int x, int y, Item acceptedItem) {
		super(inventory, index, x, y);
		this.acceptedItem = acceptedItem;
	}
	
	@Override
	public boolean canInsert(ItemStack stack) {
		return stack.getItem().equals(acceptedItem);
	}
	
}
