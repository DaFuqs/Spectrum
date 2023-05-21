package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.items.magic_items.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class ClearEnderSpliceRecipe extends SpecialCraftingRecipe {
	
	public static final RecipeSerializer<ClearEnderSpliceRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ClearEnderSpliceRecipe::new);
	
	public ClearEnderSpliceRecipe(Identifier identifier) {
		super(identifier);
	}
	
	@Override
	public boolean matches(CraftingInventory craftingInventory, World world) {
		boolean enderSpliceFound = false;
		
		for (int j = 0; j < craftingInventory.size(); ++j) {
			ItemStack itemStack = craftingInventory.getStack(j);
			if (!itemStack.isEmpty()) {
				if (!enderSpliceFound && itemStack.getItem() instanceof EnderSpliceItem) {
					if (EnderSpliceItem.hasTeleportTarget(itemStack)) {
						enderSpliceFound = true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		
		return enderSpliceFound;
	}
	
	@Override
	public ItemStack craft(CraftingInventory craftingInventory) {
		ItemStack itemStack;
		for (int j = 0; j < craftingInventory.size(); ++j) {
			itemStack = craftingInventory.getStack(j).copy();
			if (!itemStack.isEmpty() && itemStack.getItem() instanceof EnderSpliceItem) {
				EnderSpliceItem.clearTeleportTarget(itemStack);
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
