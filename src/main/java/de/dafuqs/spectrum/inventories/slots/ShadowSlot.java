package de.dafuqs.spectrum.inventories.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ClickType;

public class ShadowSlot extends ReadOnlySlot {
	
	public ShadowSlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	/**
	 * Called when this slot is clicked, before calling any item methods.
	 * Used to determine whether the click is consumed by the slot.
	 *
	 * @param heldStack the stack the in the player's cursor
	 * @param type      the click type, either left or right click
	 * @param player    the player, the held stack can be safely mutated
	 * @return whether to consume the click event or not, returning false will have the event processed by items, and if left unconsumed will be processed by the screen handler
	 */
	public boolean onClicked(ItemStack heldStack, ClickType type, PlayerEntity player) {
		ItemStack newStack = heldStack.copy();
		newStack.setCount(1);
		this.setStack(newStack);
		
		return true;
	}
	
}
