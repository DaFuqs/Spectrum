package de.dafuqs.spectrum.helpers.enchantments;

import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class FoundryHelper {
	
	@Nullable
	public static ItemStack getSmeltedItemStack(ItemStack inputItemStack, Level world) {
		var drm = world.registryAccess();
		var input = new SingleRecipeInput(inputItemStack);
		return world.getRecipeManager().getRecipeFor(RecipeType.SMELTING, input, world)
				.map(recipe -> {
					var recipeOutputStack = recipe.value().getResultItem(drm).copy();
					recipeOutputStack.setCount(recipeOutputStack.getCount() * inputItemStack.getCount());
					return recipeOutputStack;
				})
				.orElse(null);
	}
	
	@NotNull
	public static List<ItemStack> applyFoundry(Level world, List<ItemStack> originalStacks) {
		List<ItemStack> returnItemStacks = new ArrayList<>();
		
		for (ItemStack is : originalStacks) {
			ItemStack smeltedStack = FoundryHelper.getSmeltedItemStack(is, world);
			if (smeltedStack == null) {
				returnItemStacks.add(is);
			} else {
				while (!smeltedStack.isEmpty()) {
					int currentAmount = Math.min(smeltedStack.getCount(), smeltedStack.getItem().getDefaultMaxStackSize());
					ItemStack currentStack = smeltedStack.copyWithCount(currentAmount);
					returnItemStacks.add(currentStack);
					smeltedStack.setCount(smeltedStack.getCount() - currentAmount);
				}
			}
		}
		return returnItemStacks;
	}
	
}