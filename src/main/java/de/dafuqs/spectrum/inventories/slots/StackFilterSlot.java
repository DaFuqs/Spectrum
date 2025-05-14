package de.dafuqs.spectrum.inventories.slots;

import net.minecraft.world.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;

public class StackFilterSlot extends Slot {
	
	private final Item acceptedItem;
	
	public StackFilterSlot(Container inventory, int index, int x, int y, Item acceptedItem) {
		super(inventory, index, x, y);
		this.acceptedItem = acceptedItem;
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.is(acceptedItem);
	}
	
}
