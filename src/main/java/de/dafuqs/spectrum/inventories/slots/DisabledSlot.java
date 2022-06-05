package de.dafuqs.spectrum.inventories.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class DisabledSlot extends Slot {
	
	
	public DisabledSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	public boolean canInsert(ItemStack stack) {
		return false;
	}
	
	public boolean isEnabled() {
		return false;
	}
	
	public boolean canTakeItems(PlayerEntity playerEntity) {
		return false;
	}
	
}
