package de.dafuqs.spectrum.inventories.slots;

import de.dafuqs.spectrum.api.ink.capability.*;
import net.minecraft.world.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;

public class InkInputSlot extends Slot {
	
	public InkInputSlot(Container inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	@Override
	public boolean mayPlace(ItemStack stack) {
		InkCapability inkCapability = InkCapabilities.ITEM.getCapability(stack, null);
		if(inkCapability == null) return false;
		return super.mayPlace(stack);
	}
	
}
