package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.items.magic_items.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class ClearEnderSpliceRecipe extends SingleItemCraftingRecipe {
	
	public static final RecipeSerializer<ClearEnderSpliceRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ClearEnderSpliceRecipe::new);
	
	public ClearEnderSpliceRecipe(Identifier identifier) {
		super(identifier);
	}
	
	@Override
	public boolean matches(World world, ItemStack stack) {
		return stack.getItem() instanceof EnderSpliceItem && EnderSpliceItem.hasTeleportTarget(stack);
	}
	
	@Override
	public ItemStack craft(ItemStack stack) {
		EnderSpliceItem.clearTeleportTarget(stack);
		return stack;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
