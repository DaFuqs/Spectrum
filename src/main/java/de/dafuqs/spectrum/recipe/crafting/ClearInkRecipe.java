package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.energy.InkStorage;
import de.dafuqs.spectrum.energy.InkStorageItem;
import de.dafuqs.spectrum.registries.SpectrumItemTags;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ClearInkRecipe extends SpecialCraftingRecipe {
	
	public static final RecipeSerializer<ClearInkRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ClearInkRecipe::new);
	
	public ClearInkRecipe(Identifier identifier) {
		super(identifier);
	}
	
	public boolean matches(CraftingInventory craftingInventory, World world) {
		boolean inkStorageItemFound = false;
		
		for (int j = 0; j < craftingInventory.size(); ++j) {
			ItemStack itemStack = craftingInventory.getStack(j);
			if (!itemStack.isEmpty()) {
				if(itemStack.getItem() instanceof InkStorageItem) {
					inkStorageItemFound = true;
				} else {
					return false;
				}
			}
		}
		
		return inkStorageItemFound;
	}
	
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
	
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}
	
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
