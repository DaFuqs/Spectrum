package de.dafuqs.spectrum.inventories.slots;

import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;

public class LockableCraftingResultSlot extends ResultSlot {
	
	boolean locked;
	
	public LockableCraftingResultSlot(Container craftingResultInventory, int index, int x, int y, Player player, TransientCraftingContainer input) {
		super(player, input, craftingResultInventory, index, x, y);
		this.locked = false;
	}
	
	public void lock() {
		this.locked = true;
	}
	
	public void unlock() {
		this.locked = false;
	}
	
	@Override
	public void onQuickCraft(ItemStack oldStack, ItemStack newStack) {
		if(!locked) {
			super.onQuickCraft(oldStack, newStack);
		}
	}
	
	@Override
	public boolean mayPickup(Player p_307569_) {
		return !locked;
	}
	
	@Override
	public ItemStack remove(int amount) {
		if(locked) {
			return ItemStack.EMPTY;
		}
		return super.remove(amount);
	}
	
	@Override
	public boolean isHighlightable() {
		return !locked;
	}
	
}
