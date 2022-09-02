package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.inventories.AutoCraftingInventory;
import de.dafuqs.spectrum.recipe.fluid_converting.FluidConvertingRecipe;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

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
