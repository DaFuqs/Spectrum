package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.items.magic_items.EnderSpliceItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ClearEnderSpliceRecipe extends SpecialCraftingRecipe {
	
	public static final RecipeSerializer<ClearEnderSpliceRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ClearEnderSpliceRecipe::new);
	
	public ClearEnderSpliceRecipe(Identifier identifier, CraftingRecipeCategory category) {
		super(identifier, category);
	}
	
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
	
	public ItemStack craft(CraftingInventory craftingInventory, DynamicRegistryManager drm) {
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
	
	public boolean fits(int width, int height) {
		return width * height >= 1;
	}
	
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
