package de.dafuqs.spectrum.inventories.slots;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.world.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;

public class ExperienceStorageItemSlot extends Slot {
	
	public ExperienceStorageItemSlot(Container inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return super.mayPlace(stack) && stack.getItem() instanceof ExperienceStorageItem;
	}
	
}
