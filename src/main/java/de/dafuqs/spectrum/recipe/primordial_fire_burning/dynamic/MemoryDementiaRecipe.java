package de.dafuqs.spectrum.recipe.primordial_fire_burning.dynamic;

import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.primordial_fire_burning.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

public class MemoryDementiaRecipe extends PrimordialFireBurningRecipe {
	
	public static final RecipeSerializer<MemoryDementiaRecipe> SERIALIZER = new EmptyRecipeSerializer<>(MemoryDementiaRecipe::new);
	
	public MemoryDementiaRecipe(Identifier identifier) {
		super(identifier, "", false, UNLOCK_IDENTIFIER,
				Ingredient.ofStacks(MemoryItem.getForEntityType(EntityType.BEE, 1), MemoryItem.getForEntityType(EntityType.FOX, 10), MemoryItem.getForEntityType(EntityType.SKELETON, 5), MemoryItem.getForEntityType(EntityType.HUSK, 50), MemoryItem.getForEntityType(EntityType.BLAZE, -1)),
				SpectrumBlocks.MEMORY.asItem().getDefaultStack());
	}
	
	@Override
	public boolean matches(Inventory inv, World world) {
		return MemoryItem.getEntityType(inv.getStack(0).getNbt()).isPresent();
	}
	
	@Override
	public ItemStack craft(Inventory inv, DynamicRegistryManager drm) {
		ItemStack stack = inv.getStack(0);
		stack.removeSubNbt("EntityTag");
		stack.removeSubNbt("TicksToManifest");
		stack.removeSubNbt("BrokenPromise");
		stack.removeSubNbt("SpawnAsAdult");
		stack.removeSubNbt("Unrecognizable");
		return stack;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
}
