package de.dafuqs.spectrum.blocks.fluid;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.particle.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public abstract class SpectrumFluidBlock extends FluidBlock {
	
	public final BlockState ultrawarmReplacementBlockState;
	
	public SpectrumFluidBlock(SpectrumFluid fluid, BlockState ultrawarmReplacementBlockState, Settings settings) {
		super(fluid, settings);
		this.ultrawarmReplacementBlockState = ultrawarmReplacementBlockState;
	}
	
	public abstract DefaultParticleType getSplashParticle();
	
	public abstract Pair<DefaultParticleType, DefaultParticleType> getFishingParticles();
	
	@Override
	@SuppressWarnings("deprecation")
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		((SpectrumFluid) fluid).onEntityCollision(state, world, pos, entity);
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
