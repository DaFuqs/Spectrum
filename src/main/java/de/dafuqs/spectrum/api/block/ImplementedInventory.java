package de.dafuqs.spectrum.api.block;

import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;

/**
 * A simple {@code Inventory} implementation with only default methods + an item list getter.
 * <p>
 * Originally by Juuz
 */
public interface ImplementedInventory extends Container {
	
	/**
	 * Retrieves the item list of this inventory.
	 * Must return the same instance every time it's called.
	 */
	NonNullList<ItemStack> getItems();
	
	/**
	 * Creates an inventory from the item list.
	 */
	static ImplementedInventory of(NonNullList<ItemStack> items) {
		// FIXME
		return () -> items;
	}
	
	static ImplementedInventory of(ItemStack... items) {
		return of(NonNullList.of(ItemStack.EMPTY, items));
	}
	
	/**
	 * Creates a new inventory with the specified size.
	 */
	static ImplementedInventory ofSize(int size) {
		return of(NonNullList.withSize(size, ItemStack.EMPTY));
	}
	
	/**
	 * Returns the inventory size.
	 */
	@Override
	default int getContainerSize() {
		return getItems().size();
	}
	
	/**
	 * Checks if the inventory is empty.
	 *
	 * @return true if this inventory has only empty stacks, false otherwise.
	 */
	@Override
	default boolean isEmpty() {
		for (int i = 0; i < getContainerSize(); i++) {
			ItemStack stack = getItem(i);
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Retrieves the item in the slot.
	 */
	@Override
	default ItemStack getItem(int slot) {
		return getItems().get(slot);
	}
	
	/**
	 * Removes items from an inventory slot.
	 *
	 * @param slot  The slot to remove from.
	 * @param count How many items to remove. If there are less items in the slot than what are requested,
	 *              takes all items in that slot.
	 */
	@Override
	default ItemStack removeItem(int slot, int count) {
		ItemStack result = ContainerHelper.removeItem(getItems(), slot, count);
		if (!result.isEmpty()) {
			setChanged();
		}
		inventoryChanged();
		return result;
	}
	
	/**
	 * Removes all items from an inventory slot.
	 *
	 * @param slot The slot to remove from.
	 */
	@Override
	default ItemStack removeItemNoUpdate(int slot) {
		ItemStack stack = ContainerHelper.takeItem(getItems(), slot);
		if (!stack.isEmpty()) {
			inventoryChanged();
		}
		return stack;
	}
	
	/**
	 * Replaces the current stack in an inventory slot with the provided stack.
	 *
	 * @param slot  The inventory slot of which to replace the itemstack.
	 * @param stack The replacing itemstack. If the stack is too big for
	 *              this inventory ({@link Container#getMaxStackSize()}),
	 *              it gets resized to this inventory's maximum amount.
	 */
	@Override
	default void setItem(int slot, ItemStack stack) {
		getItems().set(slot, stack);
		if (stack.getCount() > stack.getMaxStackSize()) {
			stack.setCount(stack.getMaxStackSize());
		}
		inventoryChanged();
	}
	
	/**
	 * Clears the inventory.
	 */
	@Override
	default void clearContent() {
		getItems().clear();
		inventoryChanged();
	}
	
	/**
	 * Marks the state as dirty.
	 * Must be called after changes in the inventory, so that the game can properly save
	 * the inventory contents and notify neighboring blocks of inventory changes.
	 */
	@Override
	default void setChanged() {
		inventoryChanged();
	}
	
	/**
	 * @return true if the player can use the inventory, false otherwise.
	 */
	@Override
	default boolean stillValid(Player player) {
		return true;
	}
	
	default void inventoryChanged() {
	}
	
}