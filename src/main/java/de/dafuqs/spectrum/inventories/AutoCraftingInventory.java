package de.dafuqs.spectrum.inventories;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.util.collection.*;

import java.util.*;

/**
 * Vanilla does autocrafting, too!
 * See SheepEntity::createDyeMixingCraftingInventory
 */
public class AutoCraftingInventory extends AutoInventory {
	
	List<ItemStack> inputInventory;
	
	public AutoCraftingInventory(int width, int height) {
		this(width, height, 0);
	}
	
	public AutoCraftingInventory(int width, int height, int additionalSlots) {
		super(width, height);
		inputInventory = DefaultedList.ofSize(width * height + additionalSlots, ItemStack.EMPTY);
	}
	
	public void setInputInventory(List<ItemStack> inputInventory) {
		this.inputInventory = inputInventory;
	}
	
	public void setInputInventory(Inventory inventory, int fromSlot, int toSlot) {
		this.inputInventory = DefaultedList.ofSize(toSlot - fromSlot, ItemStack.EMPTY);
		for (int i = fromSlot; i < toSlot; i++) {
			this.inputInventory.set(i, inventory.getStack(i));
		}
	}
	
	@Override
	public int size() {
		return inputInventory.size();
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public ItemStack getStack(int slot) {
		return inputInventory.get(slot);
	}
	
	@Override
	public ItemStack removeStack(int slot) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return false;
	}
	
	@Override
	public void clear() {
	}
	
	@Override
	public int getHeight() {
		return 3;
	}
	
	@Override
	public int getWidth() {
		return 3;
	}
	
}
