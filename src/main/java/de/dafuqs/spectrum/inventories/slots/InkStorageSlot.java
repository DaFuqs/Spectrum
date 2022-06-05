package de.dafuqs.spectrum.inventories.slots;

import de.dafuqs.spectrum.energy.InkStorageItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class InkStorageSlot extends Slot {
	
	public InkStorageSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	@Override
	public boolean canInsert(ItemStack stack) {
		return super.canInsert(stack) && stack.getItem() instanceof InkStorageItem;
	}
	
}
