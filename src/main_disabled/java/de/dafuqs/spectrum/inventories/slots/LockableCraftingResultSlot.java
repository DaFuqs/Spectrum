package de.dafuqs.spectrum.inventories.slots;

import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;

public class LockableCraftingResultSlot extends ResultSlot {
	
	boolean locked;
	
	public LockableCraftingResultSlot(Container craftingResultInventory, int index, int x, int y, Player player, TransientCraftingContainer input) {
		super(player, input, craftingResultInventory, index, x, y);
		this.locked = false;
	}
	
	@Override
	public boolean mayPickup(Player playerEntity) {
		return !locked;
	}
	
	public void lock() {
		this.locked = true;
	}
	
	public void unlock() {
		this.locked = false;
	}
	
}
