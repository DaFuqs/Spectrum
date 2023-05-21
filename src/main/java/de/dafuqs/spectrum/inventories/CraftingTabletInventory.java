package de.dafuqs.spectrum.inventories;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.screen.*;
import net.minecraft.util.collection.*;

public class CraftingTabletInventory extends CraftingInventory {
	
	private final DefaultedList<ItemStack> gemAndOutputStacks;
	private final ScreenHandler handler;
	
	public CraftingTabletInventory(ScreenHandler handler) {
		super(handler, 3, 3);
		this.gemAndOutputStacks = DefaultedList.ofSize(6, ItemStack.EMPTY);
		this.handler = handler;
	}
	
	@Override
	public ItemStack getStack(int slot) {
		if (slot > 8) {
			return gemAndOutputStacks.get(slot - 9);
		} else {
			return super.getStack(slot);
		}
	}
	
	@Override
	public ItemStack removeStack(int slot) {
		if (slot > 8) {
			return Inventories.removeStack(gemAndOutputStacks, slot - 9);
		} else {
			return super.getStack(slot);
		}
	}
	
	@Override
	public ItemStack removeStack(int slot, int amount) {
		if (slot > 8) {
			ItemStack itemStack = Inventories.splitStack(this.gemAndOutputStacks, slot - 9, amount);
			if (!itemStack.isEmpty()) {
				this.handler.onContentChanged(this);
			}
			return itemStack;
		} else {
			return super.removeStack(slot, amount);
		}
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		if (slot > 8) {
			this.gemAndOutputStacks.set(slot - 9, stack);
		} else {
			super.setStack(slot, stack);
		}
	}
	
	@Override
	public void markDirty() {
	
	}
	
	@Override
	public int size() {
		return 9 + 5;
	}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}
	
	@Override
	public void clear() {
		super.clear();
		this.gemAndOutputStacks.clear();
	}
	
	@Override
	public void provideRecipeInputs(RecipeMatcher recipeMatcher) {
		super.provideRecipeInputs(recipeMatcher);
	}
}
