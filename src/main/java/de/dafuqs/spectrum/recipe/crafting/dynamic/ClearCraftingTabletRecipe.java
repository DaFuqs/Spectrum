package de.dafuqs.spectrum.recipe.crafting.dynamic;

import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

public class ClearCraftingTabletRecipe extends SingleItemCraftingRecipe {
	
	@Override
	public boolean matches(Level world, ItemStack stack) {
		return stack.getItem() instanceof CraftingTabletItem && CraftingTabletItem.getStoredRecipe(world, stack) != null;
	}
	
	@Override
	public ItemStack assemble(ItemStack stack) {
		CraftingTabletItem.clearStoredRecipe(stack);
		return stack;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.CLEAR_CRAFTING_TABLET_SERIALIZER;
	}
	
}
