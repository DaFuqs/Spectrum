package de.dafuqs.spectrum.inventories;

import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;

import java.util.*;

public enum AutoCraftingMode {
	OneXOne(1, 1),
	TwoXTwo(2, 2),
	ThreeXThree(3, 3);
	
	private static final Map<AutoCraftingMode, Map<ItemStackHash, Optional<RecipeHolder<CraftingRecipe>>>> CACHE = new EnumMap<>(AutoCraftingMode.class);
	
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
	
	public static Map<ItemStackHash, Optional<RecipeHolder<CraftingRecipe>>> getCache(AutoCraftingMode mode) {
		return CACHE.computeIfAbsent(mode, m -> new HashMap<>());
	}
	
	// Why tf does ItemStack not override hashCode() or equals()???
	public static class ItemStackHash {
		
		private final int hash;
		
		public ItemStackHash(ItemStack stack) {
			hash = ItemStack.hashItemAndComponents(stack);
		}
		
		@Override
		public int hashCode() {
			return hash;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ItemStackHash other = (ItemStackHash) obj;
			return hash == other.hash;
		}
	}
	
	public static void clearCache() {
		CACHE.clear();
	}
	
}

