package de.dafuqs.spectrum.recipe.crafting.dynamic;

import net.minecraft.core.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

public abstract class SingleItemCraftingRecipe extends CustomRecipe {
	
	public SingleItemCraftingRecipe() {
		super(CraftingBookCategory.MISC);
	}
	
	@Override
	public boolean matches(CraftingInput input, Level world) {
		boolean matchingItemFound = false;
		
		for (int slot = 0; slot < input.size(); ++slot) {
			ItemStack itemStack = input.getItem(slot);
			if (itemStack.isEmpty()) {
				continue;
			}
			
			if (!matchingItemFound && matches(world, itemStack)) {
				matchingItemFound = true;
			} else {
				return false;
			}
		}
		
		return matchingItemFound;
	}
	
	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registryLookup) {
		ItemStack stack;
		for (int slot = 0; slot < input.size(); ++slot) {
			stack = input.getItem(slot);
			if (!stack.isEmpty()) {
				return assemble(stack.copy());
			}
		}
		
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height > 0;
	}
	
	public abstract boolean matches(Level world, ItemStack stack);
	
	public abstract ItemStack assemble(ItemStack stack);
	
}
