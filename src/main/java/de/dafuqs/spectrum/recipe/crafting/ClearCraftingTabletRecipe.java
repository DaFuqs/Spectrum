package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.items.CraftingTabletItem;
import de.dafuqs.spectrum.items.magic_items.EnderSpliceItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ClearCraftingTabletRecipe extends SpecialCraftingRecipe {
	
	public static final RecipeSerializer<ClearCraftingTabletRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ClearCraftingTabletRecipe::new);
	
	public ClearCraftingTabletRecipe(Identifier identifier) {
		super(identifier);
	}
	
	public boolean matches(CraftingInventory craftingInventory, World world) {
		boolean craftingTabletWithStoredRecipeFound = false;
		
		for (int j = 0; j < craftingInventory.size(); ++j) {
			ItemStack itemStack = craftingInventory.getStack(j);
			if (!itemStack.isEmpty()) {
				if(itemStack.getItem() instanceof CraftingTabletItem) {
					if(CraftingTabletItem.getStoredRecipe(world, itemStack) != null) {
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
	
	public ItemStack craft(CraftingInventory craftingInventory) {
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
	
	public boolean fits(int width, int height) {
		return width * height >= 1;
	}
	
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
