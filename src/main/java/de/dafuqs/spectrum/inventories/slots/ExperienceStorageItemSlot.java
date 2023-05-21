package de.dafuqs.spectrum.inventories.slots;

import de.dafuqs.spectrum.items.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.screen.slot.*;

public class ExperienceStorageItemSlot extends Slot {
	
	public ExperienceStorageItemSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	@Override
	public boolean canInsert(ItemStack stack) {
		return super.canInsert(stack) && stack.getItem() instanceof ExperienceStorageItem;
	}
	
}
