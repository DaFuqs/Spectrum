package de.dafuqs.spectrum.helpers.enchantments;

import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import javax.annotation.*;

import java.util.*;

// See: SmeltItemFunction
public class FoundryHelper {
	
	public static List<ItemStack> applyFoundry(Level world, List<ItemStack> originalStacks) {
		List<ItemStack> returnItemStacks = new ArrayList<>();
		
		for (ItemStack stack : originalStacks) {
			ItemStack smeltedStack = FoundryHelper.getSmeltedItemStack(stack, world);
			returnItemStacks.add(smeltedStack);
		}
		
		return returnItemStacks;
	}
	
	private static ItemStack getSmeltedItemStack(ItemStack stack, Level world) {
		Optional<RecipeHolder<SmeltingRecipe>> optionalRecipe = world.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), world);
		if (optionalRecipe.isPresent()) {
			ItemStack itemstack = optionalRecipe.get().value().getResultItem(world.registryAccess());
			if (!itemstack.isEmpty()) {
				return itemstack.copyWithCount(stack.getCount() * itemstack.getCount());
			}
		}
		return stack;
	}
	
}