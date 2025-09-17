package de.dafuqs.spectrum.api.recipe;

import de.dafuqs.spectrum.recipe.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.fluids.*;
import net.neoforged.neoforge.fluids.capability.*;

import java.util.*;

public class FluidRecipeInput<T extends IFluidHandler & IFluidTank> extends SimpleRecipeInput {
	
	private final T tank;
	
	public FluidRecipeInput(List<ItemStack> items, T tank) {
		super(items);
		this.tank = tank;
	}
	
	public T getTank() {
		return this.tank;
	}
}