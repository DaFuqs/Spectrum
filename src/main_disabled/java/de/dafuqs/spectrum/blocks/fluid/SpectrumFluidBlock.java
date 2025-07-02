package de.dafuqs.spectrum.blocks.fluid;

import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import org.jetbrains.annotations.*;

public abstract class SpectrumFluidBlock extends LiquidBlock {
	
	public final BlockState ultrawarmReplacementBlockState;
	
	public SpectrumFluidBlock(SpectrumFluid fluid, BlockState ultrawarmReplacementBlockState, Properties settings) {
		super(fluid, settings);
		this.ultrawarmReplacementBlockState = ultrawarmReplacementBlockState;
	}

	public abstract SimpleParticleType getSplashParticle();
	
	public abstract Tuple<SimpleParticleType, SimpleParticleType> getFishingParticles();
	
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		super.entityInside(state, world, pos, entity);
		((SpectrumFluid) fluid).onEntityCollision(state, world, pos, entity);
	}
	
	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if (this.shouldSpreadLiquid(world, pos, state)) {
			world.scheduleTick(pos, state.getFluidState().getType(), this.fluid.getTickDelay(world));
		}
	}
	
	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		if (world.dimensionType().ultraWarm()) {
			world.setBlockAndUpdate(pos, ultrawarmReplacementBlockState);
			
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();
			world.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
			
			for (int l = 0; l < 8; ++l) {
				world.addParticle(ParticleTypes.LARGE_SMOKE, (double) x + Math.random(), (double) y + Math.random(), (double) z + Math.random(), 0.0, 0.0, 0.0);
			}
			
			return;
		}
		
		if (this.shouldSpreadLiquid(world, pos, state)) {
			world.scheduleTick(pos, state.getFluidState().getType(), this.fluid.getTickDelay(world));
		}
		
		super.onPlace(state, world, pos, oldState, notify);
	}

	/**
	 * @param world The world, because why not?
	 * @param state FluidState of this fluid.
	 * @param otherState FluidState of the other fluid.
	 * @return BlockState to be placed at the collision position. [null means no collision]
	 * @implNote Triggers the extinguish sound if result is not null.
	 */
	public abstract @Nullable BlockState handleFluidCollision(Level world, @NotNull FluidState state, @NotNull FluidState otherState, Direction direction);
	
	public void fireExtinguishEvent(LevelAccessor world, BlockPos pos) {
		world.levelEvent(LevelEvent.LAVA_FIZZ, pos, 0);
	}

	/**
	 * @param world The world
	 * @param pos   The position in the world
	 * @param state BlockState of the block at pos [normally assumed to be this fluid].
	 * @return Dunno, actually. I just mod things.
	 * @implNote Provides generic behavior for fluid collision. Extend/override for more advanced behaviors.
	 */
	public boolean shouldSpreadLiquid(Level world, BlockPos pos, BlockState state) {
		// Shouldn't happen but check anyway
		// If it IS true then do nothing, since no interaction can take place at this position
		final FluidState fluidState = state.getFluidState();
		if (fluidState.isEmpty()) return true;

		for (Direction direction : Direction.values()) {
			final FluidState otherState = world.getFluidState(pos.relative(direction));
			if (otherState.isEmpty()) continue;
			
			final BlockState setState = handleFluidCollision(world, fluidState, otherState, direction);
			if (setState != null) {
				this.fireExtinguishEvent(world, pos);
				world.setBlockAndUpdate(direction == Direction.DOWN ? pos.below() : pos, setState);
				return false;
			}
		}
		return true;
	}
	
}
