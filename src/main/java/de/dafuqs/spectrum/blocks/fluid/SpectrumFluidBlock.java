package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.recipe.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class SpectrumFluidBlock extends FluidBlock {
	
	private static AutoCraftingInventory AUTO_INVENTORY;
	
	public final BlockState ultrawarmReplacementBlockState;
	
	public SpectrumFluidBlock(FlowableFluid fluid, BlockState ultrawarmReplacementBlockState, Settings settings) {
		super(fluid, settings);
		this.ultrawarmReplacementBlockState = ultrawarmReplacementBlockState;
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
	@SuppressWarnings("deprecation")
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		
		if (!world.isClient) {
			if (entity instanceof ItemEntity itemEntity && !itemEntity.cannotPickup()) {
				if (world.random.nextInt(100) == 0) {
					ItemStack itemStack = itemEntity.getStack();
					FluidConvertingRecipe recipe = getConversionRecipeFor(getDippingRecipeType(), world, itemStack);
					if (recipe != null) {
						world.playSound(null, itemEntity.getBlockPos(), SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.NEUTRAL, 1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
						
						ItemStack result = recipe.getOutput(world.getRegistryManager());
						int count = recipe.getOutput(world.getRegistryManager()).getCount() * itemStack.getCount();
						result.setCount(count);
						
						if (itemEntity.getOwner() instanceof ServerPlayerEntity serverPlayerEntity) {
							SpectrumAdvancementCriteria.FLUID_DIPPING.trigger(serverPlayerEntity, (ServerWorld) world, pos, itemStack, result);
						}
						
						MultiblockCrafter.spawnItemStackAsEntitySplitViaMaxCount(world, itemEntity.getPos(), result, count, Vec3d.ZERO, false, itemEntity.getOwner());
						itemEntity.discard();
					}
				}
			}
		}
	}
	
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if (this.receiveNeighborFluids(world, pos, state)) {
			world.scheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
		}
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (world.getDimension().ultrawarm()) {
			world.setBlockState(pos, ultrawarmReplacementBlockState);
			
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
			
			for (int l = 0; l < 8; ++l) {
				world.addParticle(ParticleTypes.LARGE_SMOKE, (double) x + Math.random(), (double) y + Math.random(), (double) z + Math.random(), 0.0, 0.0, 0.0);
			}
			
			return;
		}
		
		if (this.receiveNeighborFluids(world, pos, state)) {
			world.scheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
		}
		
		super.onBlockAdded(state, world, pos, oldState, notify);
	}
	
	abstract boolean receiveNeighborFluids(World world, BlockPos pos, BlockState state);
	
}
