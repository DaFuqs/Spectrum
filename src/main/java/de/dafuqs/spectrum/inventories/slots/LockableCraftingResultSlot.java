package de.dafuqs.spectrum.inventories.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.CraftingResultSlot;

public class LockableCraftingResultSlot extends CraftingResultSlot {
	
	boolean locked;
	
	public LockableCraftingResultSlot(PlayerEntity player, CraftingInventory input, Inventory inventory, int index, int x, int y) {
		super(player, input, inventory, index, x, y);
		this.locked = false;
	}
	
	public boolean canTakeItems(PlayerEntity playerEntity) {
		return !locked;
	}
	
	public void lock() {
		this.locked = true;
	}
	
	public void unlock() {
		this.locked = false;
	}
	
	
}
