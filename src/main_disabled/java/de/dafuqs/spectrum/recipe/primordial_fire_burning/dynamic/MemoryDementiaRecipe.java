package de.dafuqs.spectrum.recipe.primordial_fire_burning.dynamic;

import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.recipe.primordial_fire_burning.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;

import java.util.*;

public class MemoryDementiaRecipe extends PrimordialFireBurningRecipe {
	
	public MemoryDementiaRecipe() {
		super("", false, Optional.of(UNLOCK_IDENTIFIER),
				Ingredient.of(MemoryItem.getForEntityType(EntityType.BEE, 1), MemoryItem.getForEntityType(EntityType.FOX, 10), MemoryItem.getForEntityType(EntityType.SKELETON, 5), MemoryItem.getForEntityType(EntityType.HUSK, 50), MemoryItem.getForEntityType(EntityType.BLAZE, -1)),
				SpectrumBlocks.MEMORY.asItem().getDefaultInstance());
	}
	
	@Override
	public boolean matches(RecipeInput inv, Level world) {
		return inv.getItem(0).has(SpectrumDataComponentTypes.MEMORY);
	}
	
	@Override
	public ItemStack assemble(RecipeInput inv, HolderLookup.Provider drm) {
		ItemStack stack = inv.getItem(0);
		stack.remove(SpectrumDataComponentTypes.MEMORY);
		return stack;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpectrumRecipeSerializers.MEMORY_DEMENTIA;
	}
	
}
