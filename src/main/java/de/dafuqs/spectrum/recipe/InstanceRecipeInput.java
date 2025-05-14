package de.dafuqs.spectrum.recipe;

import net.minecraft.world.item.*;

import java.util.*;

public class InstanceRecipeInput<T> extends SimpleRecipeInput {
	
	private final T instance;
	
	public InstanceRecipeInput(List<ItemStack> items, T instance) {
		super(items);
		this.instance = instance;
	}
	
	public T getInstance() {
		return this.instance;
	}
	
}
