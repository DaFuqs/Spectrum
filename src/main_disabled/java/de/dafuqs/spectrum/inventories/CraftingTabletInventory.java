package de.dafuqs.spectrum.inventories;

import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;

public class CraftingTabletInventory extends TransientCraftingContainer {
	
	private final NonNullList<ItemStack> gemAndOutputStacks;
	private final AbstractContainerMenu handler;
	
	public CraftingTabletInventory(AbstractContainerMenu handler) {
		super(handler, 3, 3);
		this.gemAndOutputStacks = NonNullList.withSize(6, ItemStack.EMPTY);
		this.handler = handler;
	}
	
	@Override
	public ItemStack getItem(int slot) {
		if (slot > 8) {
			return gemAndOutputStacks.get(slot - 9);
		} else {
			return super.getItem(slot);
		}
	}
	
	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		if (slot > 8) {
			return ContainerHelper.takeItem(gemAndOutputStacks, slot - 9);
		} else {
			return super.getItem(slot);
		}
	}
	
	@Override
	public ItemStack removeItem(int slot, int amount) {
		if (slot > 8) {
			ItemStack itemStack = ContainerHelper.removeItem(this.gemAndOutputStacks, slot - 9, amount);
			if (!itemStack.isEmpty()) {
				this.handler.slotsChanged(this);
			}
			return itemStack;
		} else {
			return super.removeItem(slot, amount);
		}
	}
	
	@Override
	public void setItem(int slot, ItemStack stack) {
		if (slot > 8) {
			this.gemAndOutputStacks.set(slot - 9, stack);
		} else {
			super.setItem(slot, stack);
		}
	}
	
	@Override
	public void setChanged() {
	
	}
	
	@Override
	public int getContainerSize() {
		return super.getContainerSize() + gemAndOutputStacks.size();
	}
	
	@Override
	public boolean stillValid(Player player) {
		return true;
	}
	
	@Override
	public void clearContent() {
		super.clearContent();
		this.gemAndOutputStacks.clear();
	}
	
	@Override
	public void fillStackedContents(StackedContents recipeMatcher) {
		super.fillStackedContents(recipeMatcher);
	}
	
}
