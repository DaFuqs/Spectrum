package de.dafuqs.spectrum.recipe.crafting.dynamic;

import de.dafuqs.spectrum.api.energy.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

public class ClearInkRecipe extends SingleItemCraftingRecipe {
	
	@Override
	public boolean matches(Level world, ItemStack stack) {
		return stack.getItem() instanceof InkStorageItem;
	}
	
	@Override
	public ItemStack assemble(ItemStack stack) {
		if (stack.getItem() instanceof InkStorageItem<?> inkStorageItem) {
			stack = stack.copy();
			stack.setCount(1);
			inkStorageItem.clearEnergyStorage(stack);
		}
		return stack;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.CLEAR_INK_SERIALIZER;
	}
	
}
