package de.dafuqs.spectrum.inventories;

import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;

public enum AutoCraftingMode {
	OneXOne(1, 1),
	TwoXTwo(2, 2),
	ThreeXThree(3, 3);
	
	private final int width;
	private final int height;
	
	AutoCraftingMode(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getSize() {
		return width * height;
	}
	
	public AutoCraftingMode next() {
		return AutoCraftingMode.values()[(this.ordinal() + 1) % values().length];
	}
	
	public CraftingInput.Positioned createRecipeInput(ItemVariant variant) {
		ItemStack stack = variant.toStack();
		List<ItemStack> inputs = new ArrayList<>(getSize());
		for (int i = 0; i < getSize(); i++) {
			inputs.add(stack);
		}
		return CraftingInput.ofPositioned(width, height, inputs);
	}
	
}

