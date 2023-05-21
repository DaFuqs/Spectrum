package de.dafuqs.spectrum.inventories.slots;

import de.dafuqs.spectrum.energy.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.screen.slot.*;

public class InkStorageSlot extends Slot {
	
	public InkStorageSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	@Override
	public boolean canInsert(ItemStack stack) {
		return super.canInsert(stack) && stack.getItem() instanceof InkStorageItem;
	}
	
}
