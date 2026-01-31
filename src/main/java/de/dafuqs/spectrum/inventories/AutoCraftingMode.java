package de.dafuqs.spectrum.inventories;

import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;

public enum AutoCraftingMode {
	OneXOne(1, 1),
	TwoXTwo(2, 2),
	ThreeXThree(3, 3);
	
	private static final Map<AutoCraftingMode, Map<ItemStack, Optional<RecipeHolder<CraftingRecipe>>>> CACHE = new EnumMap<>(AutoCraftingMode.class);
	
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
	
	public CraftingInput.Positioned createRecipeInput(ItemStack stack) {
		List<ItemStack> inputs = new ArrayList<>(getSize());
		for (int i = 0; i < getSize(); i++) {
			inputs.add(stack);
		}
		return CraftingInput.ofPositioned(width, height, inputs);
	}
	
	public static Map<ItemStack, Optional<RecipeHolder<CraftingRecipe>>> getCache(AutoCraftingMode mode) {
		return CACHE.computeIfAbsent(mode, m -> new HashMap<>());
	}
	
	public static void clearCache() {
		CACHE.clear();
	}
	
}

