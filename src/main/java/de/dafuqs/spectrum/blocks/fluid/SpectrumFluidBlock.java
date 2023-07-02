package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.recipe.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
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
	
	public abstract RecipeType<? extends FluidConvertingRecipe> getDippingRecipeType();
	
	public <R extends FluidConvertingRecipe> R getConversionRecipeFor(RecipeType<R> recipeType, @NotNull World world, ItemStack itemStack) {
		if (AUTO_INVENTORY == null) {
			AUTO_INVENTORY = new AutoCraftingInventory(1, 1);
		}
		AUTO_INVENTORY.setInputInventory(Collections.singletonList(itemStack));
		return world.getRecipeManager().getFirstMatch(recipeType, AUTO_INVENTORY, world).orElse(null);
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		
		if (!world.isClient) {
			if (entity instanceof ItemEntity itemEntity && !itemEntity.cannotPickup()) {
				if (world.random.nextInt(200) == 0) {
					ItemStack itemStack = itemEntity.getStack();
					FluidConvertingRecipe recipe = getConversionRecipeFor(getDippingRecipeType(), world, itemStack);
					if (recipe != null) {
						world.playSound(null, itemEntity.getBlockPos(), SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.NEUTRAL, 1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
						MultiblockCrafter.spawnItemStackAsEntitySplitViaMaxCount(world, itemEntity.getPos(), recipe.getOutput(), recipe.getOutput().getCount() * itemStack.getCount(), Vec3d.ZERO);
						itemEntity.discard();
					}
				}
			}
		}
	}
	
}
