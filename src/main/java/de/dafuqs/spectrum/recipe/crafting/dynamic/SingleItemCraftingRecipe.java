package de.dafuqs.spectrum.recipe.crafting.dynamic;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public abstract class SingleItemCraftingRecipe extends SpecialCraftingRecipe {
	
	public SingleItemCraftingRecipe(Identifier identifier) {
		super(identifier, CraftingRecipeCategory.MISC);
	}

	public SingleItemCraftingRecipe(Identifier identifier, CraftingRecipeCategory category) {
		super(identifier, category);
	}
	
	@Override
	public boolean matches(RecipeInputInventory craftingInventory, World world) {
		boolean matchingItemFound = false;
		
		for (int slot = 0; slot < craftingInventory.size(); ++slot) {
			ItemStack itemStack = craftingInventory.getStack(slot);
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
	public ItemStack craft(RecipeInputInventory craftingInventory, DynamicRegistryManager drm) {
		ItemStack stack;
		for (int slot = 0; slot < craftingInventory.size(); ++slot) {
			stack = craftingInventory.getStack(slot);
			if (!stack.isEmpty()) {
				return craft(stack.copy());
			}
		}
		
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return width * height > 0;
	}
	
	public abstract boolean matches(World world, ItemStack stack);
	
	public abstract ItemStack craft(ItemStack stack);
	
}
