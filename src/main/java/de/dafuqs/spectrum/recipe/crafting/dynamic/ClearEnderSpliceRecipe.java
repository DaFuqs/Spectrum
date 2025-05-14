package de.dafuqs.spectrum.recipe.crafting.dynamic;

import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

public class ClearEnderSpliceRecipe extends SingleItemCraftingRecipe {
	
	@Override
	public boolean matches(Level world, ItemStack stack) {
		return stack.getItem() instanceof EnderSpliceItem && EnderSpliceItem.hasTeleportTarget(stack);
	}
	
	@Override
	public ItemStack assemble(ItemStack stack) {
		ItemStack returnStack = stack.copy();
		returnStack.setCount(1);
		EnderSpliceItem.clearTeleportTarget(returnStack);
		return returnStack;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.CLEAR_ENDER_SPLICE_SERIALIZER;
	}
	
}
