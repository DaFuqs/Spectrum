package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.energy.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class ClearInkRecipe extends SpecialCraftingRecipe {
	
	public static final RecipeSerializer<ClearInkRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ClearInkRecipe::new);
	
	public ClearInkRecipe(Identifier identifier) {
		super(identifier);
	}
	
	@Override
	public boolean matches(CraftingInventory craftingInventory, World world) {
		boolean inkStorageItemFound = false;
		
		for (int j = 0; j < craftingInventory.size(); ++j) {
			ItemStack itemStack = craftingInventory.getStack(j);
			if (!itemStack.isEmpty()) {
				if (itemStack.getItem() instanceof InkStorageItem) {
					inkStorageItemFound = true;
				} else {
					return false;
				}
			}
		}
		
		return inkStorageItemFound;
	}
	
	@Override
	public ItemStack craft(CraftingInventory craftingInventory) {
		ItemStack itemStack;
		for (int j = 0; j < craftingInventory.size(); ++j) {
			itemStack = craftingInventory.getStack(j).copy();
			if (!itemStack.isEmpty() && itemStack.getItem() instanceof InkStorageItem<?> inkStorageItem) {
				inkStorageItem.clearEnergyStorage(itemStack);
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
