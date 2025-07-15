package de.dafuqs.spectrum.recipe;

import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class SimpleRecipeInput implements RecipeInput {
	private final List<ItemStack> stacks;
	
	public SimpleRecipeInput(List<ItemStack> stacks) {
		this.stacks = stacks;
	}
	
	@Override
	public @NotNull ItemStack getItem(int slot) {
		return stacks.get(slot);
	}
	
	@Override
	public int size() {
		return stacks.size();
	}
	
	@Override
	public boolean isEmpty() {
		return stacks.isEmpty();
	}
	
}
