package de.dafuqs.spectrum.recipe;

import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.minecraft.world.item.*;

import java.util.*;

public class StorageRecipeInput<T extends Storage<?>> extends SimpleRecipeInput {
	
	private final T fluidStorage;
	
	public StorageRecipeInput(List<ItemStack> items, T fluidStorage) {
		super(items);
		this.fluidStorage = fluidStorage;
	}
	
	public T getFluidStorage() {
		return this.fluidStorage;
	}
}
