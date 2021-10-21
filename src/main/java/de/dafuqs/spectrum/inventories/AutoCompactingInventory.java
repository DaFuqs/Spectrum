package de.dafuqs.spectrum.inventories;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class AutoCompactingInventory extends AutoInventory {

	public enum AutoCraftingMode {
		OnexOne,
		TwoXTwo,
		ThreeXTree
	}

	private AutoCraftingMode autoCraftingMode;
	ItemStack inputItemStack;

	public AutoCompactingInventory() {
		super(3, 3);
		this.inputItemStack = ItemStack.EMPTY;
		this.autoCraftingMode = AutoCraftingMode.ThreeXTree;
	}

	public void setCompacting(AutoCraftingMode autoCraftingMode, ItemStack itemStack) {
		this.autoCraftingMode = autoCraftingMode;
		this.inputItemStack = itemStack;
	}

	@Override
	public int size() {
		return getSize() * getSize();
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack getStack(int slot) {
		return inputItemStack;
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
		return getSize();
	}

	@Override
	public int getWidth() {
		return getSize();
	}

	private int getSize() {
		switch (this.autoCraftingMode) {
			case OnexOne:
				return 1;
			case TwoXTwo:
				return 2;
			default:
				return 3;
		}
	}

}
