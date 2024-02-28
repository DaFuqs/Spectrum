package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.api.energy.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class ClearInkRecipe extends SingleItemCraftingRecipe {
	
	public static final RecipeSerializer<ClearInkRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ClearInkRecipe::new);
	
	public ClearInkRecipe(Identifier identifier, CraftingRecipeCategory category) {
		super(identifier, category);
	}
	
	@Override
	public boolean matches(World world, ItemStack stack) {
		return stack.getItem() instanceof InkStorageItem;
	}
	
	@Override
	public ItemStack craft(ItemStack stack) {
		if (stack.getItem() instanceof InkStorageItem<?> inkStorageItem) {
			stack = stack.copy();
			stack.setCount(1);
			inkStorageItem.clearEnergyStorage(stack);
		}
		return stack;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
