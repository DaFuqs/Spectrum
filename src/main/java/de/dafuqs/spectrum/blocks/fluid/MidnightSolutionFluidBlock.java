package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.blocks.decay.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.fluid.*;
import net.minecraft.particle.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class MidnightSolutionFluidBlock extends SpectrumFluidBlock {

	public static final BlockState SPREAD_BLOCKSTATE = SpectrumBlocks.BLACK_MATERIA.getDefaultState().with(BlackMateriaBlock.AGE, 0);
	
	public MidnightSolutionFluidBlock(SpectrumFluid fluid, BlockState ultrawarmReplacementBlockState, Settings settings) {
		super(fluid, ultrawarmReplacementBlockState, settings);
	}

	@Override
	public DefaultParticleType getSplashParticle() {
		return SpectrumParticleTypes.MIDNIGHT_SOLUTION_SPLASH;
	}

	@Override
	public Pair<DefaultParticleType, DefaultParticleType> getFishingParticles() {
		return new Pair<>(SpectrumParticleTypes.GRAY_SPARKLE_RISING, SpectrumParticleTypes.MIDNIGHT_SOLUTION_FISHING);
	}

	public static boolean tryConvertNeighbor(@NotNull World world, BlockPos pos, BlockPos fromPos) {
		FluidState fluidState = world.getFluidState(fromPos);
		if (!fluidState.isEmpty() && fluidState.isIn(SpectrumFluidTags.MIDNIGHT_SOLUTION_CONVERTED)) {
			world.setBlockState(fromPos, SpectrumBlocks.MIDNIGHT_SOLUTION.getDefaultState());
			playExtinguishSound(world, fromPos);
			return true;
		}
		return false;
	}

	public static void playExtinguishSound(@NotNull WorldAccess world, BlockPos pos) {
		world.syncWorldEvent(WorldEvents.LAVA_EXTINGUISHED, pos, 0);
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if (!world.getBlockState(pos.up()).isSolidBlock(world, pos.up()) && random.nextFloat() < 0.03F) {
			world.addParticle(SpectrumParticleTypes.VOID_FOG, pos.getX() + random.nextDouble(), pos.getY() + 1, pos.getZ() + random.nextDouble(), 0, random.nextDouble() * 0.1, 0);
		}
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}

	@Override
	public boolean receiveNeighborFluids(World world, BlockPos pos, BlockState state) {
		// Shouldn't happen but check anyway
		// If it IS true then do nothing, since no interaction can take place at this position
		final FluidState fluidState = state.getFluidState();
		if (fluidState == null || fluidState.isEmpty()) return true;

		for (Direction direction : Direction.values()) {
			BlockPos neighborPos = pos.offset(direction);
			FluidState neighborFluidState = world.getFluidState(neighborPos);

			// Do nothing if neighbor fluid state is empty. [matters for both collision and spread]
			if (neighborFluidState == null || neighborFluidState.isEmpty()) continue;

			// Fluid collision interaction
			final BlockState setState = handleFluidCollision(world, fluidState, neighborFluidState);
			if (setState != null) {
				fireExtinguishEvent(world, pos);
				world.setBlockState(pos, setState);
				return false;
			}

			// World interaction
			boolean isNeighborFluidBlock = world.getBlockState(neighborPos).getBlock() instanceof FluidBlock;
			// spread to the fluid
			boolean doesTickEntities = world.getWorldChunk(pos).getLevelType().isAfter(ChunkLevelType.ENTITY_TICKING);
			if (!neighborFluidState.isEmpty() && doesTickEntities) {
				if (!isNeighborFluidBlock) {
					world.setBlockState(pos, SPREAD_BLOCKSTATE);
					fireExtinguishEvent(world, pos);
				} else {
					if (!neighborFluidState.isOf(this.fluid) && !neighborFluidState.isIn(SpectrumFluidTags.MIDNIGHT_SOLUTION_CONVERTED) && !world.getBlockState(neighborPos).isOf(this)) {
						world.setBlockState(pos, SPREAD_BLOCKSTATE);
						fireExtinguishEvent(world, neighborPos);
					}
				}
			}
		}
		return true;
	}

	public @Nullable BlockState handleFluidCollision(World world, @NotNull FluidState state, @NotNull FluidState otherState) {
		if (otherState.isIn(FluidTags.LAVA)) return Blocks.TERRACOTTA.getDefaultState();
		return null;
	}

}
