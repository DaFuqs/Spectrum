package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.items.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class ClearCraftingTabletRecipe extends SpecialCraftingRecipe {
	
	public static final RecipeSerializer<ClearCraftingTabletRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ClearCraftingTabletRecipe::new);
	
	public ClearCraftingTabletRecipe(Identifier identifier, CraftingRecipeCategory category) {
		super(identifier, category);
	}
	
	@Override
	public boolean matches(CraftingInventory craftingInventory, World world) {
		boolean craftingTabletWithStoredRecipeFound = false;
		
		for (int j = 0; j < craftingInventory.size(); ++j) {
			ItemStack itemStack = craftingInventory.getStack(j);
			if (!itemStack.isEmpty()) {
				if (itemStack.getItem() instanceof CraftingTabletItem) {
					if (!craftingTabletWithStoredRecipeFound && CraftingTabletItem.getStoredRecipe(world, itemStack) != null) {
						craftingTabletWithStoredRecipeFound = true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		
		return craftingTabletWithStoredRecipeFound;
	}

	@Override
	public ItemStack craft(CraftingInventory craftingInventory, DynamicRegistryManager drm) {
		ItemStack itemStack;
		for (int j = 0; j < craftingInventory.size(); ++j) {
			itemStack = craftingInventory.getStack(j).copy();
			if (!itemStack.isEmpty() && itemStack.getItem() instanceof CraftingTabletItem) {
				CraftingTabletItem.clearStoredRecipe(itemStack);
				return itemStack;
			}
		}
		
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean fits(int width, int height) {
		return width * height >= 1;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
