package de.dafuqs.spectrum.inventories;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.collection.DefaultedList;

public class CraftingTabletInventory extends CraftingInventory {
	
	private final DefaultedList<ItemStack> gemAndOutputStacks;
	private final ScreenHandler handler;
	
	public CraftingTabletInventory(ScreenHandler handler) {
		super(handler, 3, 3);
		this.gemAndOutputStacks = DefaultedList.ofSize(6, ItemStack.EMPTY);
		this.handler = handler;
	}
	
	public ItemStack getStack(int slot) {
		if (slot > 8) {
			return gemAndOutputStacks.get(slot - 9);
		} else {
			return super.getStack(slot);
		}
	}
	
	public ItemStack removeStack(int slot) {
		if (slot > 8) {
			return Inventories.removeStack(gemAndOutputStacks, slot - 9);
		} else {
			return super.getStack(slot);
		}
	}
	
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
	
	public void setStack(int slot, ItemStack stack) {
		if (slot > 8) {
			this.gemAndOutputStacks.set(slot - 9, stack);
		} else {
			super.setStack(slot, stack);
		}
	}
	
	public void markDirty() {
	
	}
	
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}
	
	public void clear() {
		super.clear();
		this.gemAndOutputStacks.clear();
	}
	
	public void provideRecipeInputs(RecipeMatcher recipeMatcher) {
		super.provideRecipeInputs(recipeMatcher);
	}
}
