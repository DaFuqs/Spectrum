package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.items.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class ClearCraftingTabletRecipe extends SingleItemCraftingRecipe {
	
	public static final RecipeSerializer<ClearCraftingTabletRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ClearCraftingTabletRecipe::new);
	
	public ClearCraftingTabletRecipe(Identifier identifier) {
		super(identifier);
	}
	
	@Override
	public boolean matches(World world, ItemStack stack) {
		return stack.getItem() instanceof CraftingTabletItem && CraftingTabletItem.getStoredRecipe(world, stack) != null;
	}
	
	@Override
	public ItemStack craft(ItemStack stack) {
		CraftingTabletItem.clearStoredRecipe(stack);
		return stack;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
