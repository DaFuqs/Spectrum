package de.dafuqs.spectrum.recipe.crafting;

import de.dafuqs.spectrum.api.item.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class ClearPotionFillableRecipe extends SingleItemCraftingRecipe {
	
	public static final RecipeSerializer<ClearPotionFillableRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ClearPotionFillableRecipe::new);
	
	public ClearPotionFillableRecipe(Identifier identifier, CraftingRecipeCategory category) {
		super(identifier, category);
	}
	
	@Override
	public boolean matches(World world, ItemStack stack) {
		return stack.getItem() instanceof InkPoweredPotionFillable inkPoweredPotionFillable && inkPoweredPotionFillable.isAtLeastPartiallyFilled(stack);
	}
	
	@Override
	public ItemStack craft(ItemStack stack) {
		if (stack.getItem() instanceof InkPoweredPotionFillable inkPoweredPotionFillable) {
			stack = stack.copy();
			stack.setCount(1);
			inkPoweredPotionFillable.clearEffects(stack);
		}
		return stack;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
