package de.dafuqs.spectrum.blocks.fluid;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.fluid.*;
import net.minecraft.particle.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

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

	/**
	 * @param world The world, because why not?
	 * @param state FluidState of this fluid.
	 * @param otherState FluidState of the other fluid.
	 * @return BlockState to be placed at the collision position. [null means no collision]
	 * @implNote Triggers the extinguish sound if result is not null.
	 */
	public abstract @Nullable BlockState handleFluidCollision(World world, FluidState state, FluidState otherState);

	public void fireExtinguishEvent(WorldAccess world, BlockPos pos) {
		world.syncWorldEvent(WorldEvents.LAVA_EXTINGUISHED, pos, 0);
	}

	/**
	 * @param world The world
	 * @param pos   The position in the world
	 * @param state BlockState of the block at pos [normally assumed to be this fluid].
	 * @return Dunno, actually. I just mod things.
	 * @implNote Provides generic behavior for fluid collision. Extend/override for more advanced behaviors.
	 */
	public boolean receiveNeighborFluids(World world, BlockPos pos, BlockState state) {
		// Shouldn't happen but check anyway
		// If it IS true then do nothing, since no interaction can take place at this position
		final FluidState fluidState = state.getFluidState();
		if (fluidState == null || fluidState.isEmpty()) return true;

		for (Direction direction : Direction.values()) {
			final FluidState otherState = world.getFluidState(pos.offset(direction));
			if (otherState == null || otherState.isEmpty()) continue;

			final BlockState setState = handleFluidCollision(world, fluidState, otherState);
			if (setState != null) {
				this.fireExtinguishEvent(world, pos);
				world.setBlockState(pos, setState);
				return false;
			}
		}
		return true;
	}
	
}
