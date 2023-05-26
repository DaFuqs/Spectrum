package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.items.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class ClearPotionFillableRecipe extends SpecialCraftingRecipe {
	
	public static final RecipeSerializer<ClearPotionFillableRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ClearPotionFillableRecipe::new);
	
	public ClearPotionFillableRecipe(Identifier identifier, CraftingRecipeCategory category) {
		super(identifier, category);
	}
	
	@Override
	public boolean matches(CraftingInventory craftingInventory, World world) {
		boolean potionFillableFound = false;
		
		for (int j = 0; j < craftingInventory.size(); ++j) {
			ItemStack itemStack = craftingInventory.getStack(j);
			if (!itemStack.isEmpty()) {
				if (itemStack.getItem() instanceof InkPoweredPotionFillable inkPoweredPotionFillable) {
					if (inkPoweredPotionFillable.isAtLeastPartiallyFilled(itemStack)) {
						potionFillableFound = true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		
		return potionFillableFound;
	}
	
	@Override
	public ItemStack craft(CraftingInventory craftingInventory, DynamicRegistryManager drm) {
		ItemStack itemStack;
		for (int j = 0; j < craftingInventory.size(); ++j) {
			itemStack = craftingInventory.getStack(j).copy();
			if (!itemStack.isEmpty() && itemStack.getItem() instanceof InkPoweredPotionFillable inkPoweredPotionFillable) {
				inkPoweredPotionFillable.removeEffects(itemStack);
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
