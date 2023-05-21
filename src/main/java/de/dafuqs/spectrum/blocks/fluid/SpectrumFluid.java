package de.dafuqs.spectrum.blocks.fluid;

import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.fluid.*;
import net.minecraft.particle.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public abstract class SpectrumFluid extends FlowableFluid {
	
	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == getStill() || fluid == getFlowing();
	}
	
	@Override
	protected boolean isInfinite(World world) {
		// TODO - hardcode this, or implement gamerules for it
		return world.getGameRules().getBoolean(GameRules.WATER_SOURCE_CONVERSION);
	}
	
	/**
	 * Perform actions when fluid flows into a replaceable block.
	 * => Drop the block
	 */
	@Override
	protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
		final BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
		Block.dropStacks(state, world, pos, blockEntity);
	}
	
	/**
	 * Lava returns true if its FluidState is above a certain height and the Fluid is Water.
	 *
	 * @return if the given Fluid can flow into this FluidState?
	 */
	@Override
	protected boolean canBeReplacedWith(FluidState fluidState, BlockView blockView, BlockPos blockPos, Fluid fluid, Direction direction) {
		return false;
	}
	
	/**
	 * Possibly related to the distance checks for flowing into nearby holes?
	 * Water returns 4. Lava returns 2 in the Overworld and 4 in the Nether.
	 */
	@Override
	protected int getFlowSpeed(WorldView worldView) {
		return 3;
	}
	
	/**
	 * Water returns 1. Lava returns 2 in the Overworld and 1 in the Nether.
	 */
	@Override
	protected int getLevelDecreasePerBlock(WorldView worldView) {
		return 1;
	}
	
	/**
	 * Water returns 5. Lava returns 30 in the Overworld and 10 in the Nether.
	 */
	@Override
	public int getTickRate(WorldView worldView) {
		return 20;
	}
	
	/**
	 * Water and Lava both return 100.0F.
	 */
	@Override
	protected float getBlastResistance() {
		return 100.0F;
	}
	
	@Override
	public Optional<SoundEvent> getBucketFillSound() {
		return Optional.of(SoundEvents.ITEM_BUCKET_FILL);
	}
	
	public abstract ParticleEffect getSplashParticle();

}