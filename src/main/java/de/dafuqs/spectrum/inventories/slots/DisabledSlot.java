package de.dafuqs.spectrum.inventories.slots;

import net.minecraft.world.*;
import net.minecraft.world.inventory.*;

public class DisabledSlot extends NonInteractiveResultSlot {
	
	public DisabledSlot(Container inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	@Override
	public boolean isActive() {
		return false;
	}
	
}
