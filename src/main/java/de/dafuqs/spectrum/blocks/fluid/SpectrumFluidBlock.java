package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import net.minecraft.block.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class SpectrumFluidBlock extends FluidBlock {
	
	private static AutoCraftingInventory AUTO_INVENTORY;
	
	public SpectrumFluidBlock(FlowableFluid fluid, Settings settings) {
		super(fluid, settings);
	}
	
	public abstract DefaultParticleType getSplashParticle();
	
	public abstract Pair<DefaultParticleType, DefaultParticleType> getFishingParticles();
	
	public <R extends FluidConvertingRecipe> R getConversionRecipeFor(RecipeType<R> recipeType, @NotNull World world, ItemStack itemStack) {
		if (AUTO_INVENTORY == null) {
			AUTO_INVENTORY = new AutoCraftingInventory(1, 1);
		}
		AUTO_INVENTORY.setInputInventory(Collections.singletonList(itemStack));
		return world.getRecipeManager().getFirstMatch(recipeType, AUTO_INVENTORY, world).orElse(null);
	}
	
}
