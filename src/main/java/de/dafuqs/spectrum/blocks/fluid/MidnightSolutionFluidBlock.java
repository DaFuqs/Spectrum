package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.blocks.decay.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.pathfinder.*;
import org.jspecify.annotations.Nullable;

public class MidnightSolutionFluidBlock extends SpectrumFluidBlock {
	
	public static final BlockState SPREAD_BLOCKSTATE = SpectrumBlocks.BLACK_MATERIA.get().defaultBlockState().setValue(BlackMateriaBlock.AGE, 0);
	
	public MidnightSolutionFluidBlock(SpectrumFluid fluid, BlockState ultrawarmReplacementBlockState, Properties settings) {
		super(fluid, ultrawarmReplacementBlockState, settings);
	}

//	@Override
//	public MapCodec<? extends MidnightSolutionFluidBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}
	
	@Override
	public SimpleParticleType getSplashParticle() {
		return SpectrumParticleTypes.MIDNIGHT_SOLUTION_SPLASH;
	}
	
	@Override
	public Tuple<SimpleParticleType, SimpleParticleType> getFishingParticles() {
		return new Tuple<>(SpectrumParticleTypes.MIDNIGHT_SOLUTION_SPLASH, SpectrumParticleTypes.MIDNIGHT_SOLUTION_FISHING);
	}
	
	public static boolean tryConvertNeighbor(Level world, BlockPos fromPos) {
		FluidState fluidState = world.getFluidState(fromPos);
		if (!fluidState.isEmpty() && fluidState.is(SpectrumFluidTags.MIDNIGHT_SOLUTION_CONVERTED)) {
			world.setBlockAndUpdate(fromPos, SpectrumBlocks.MIDNIGHT_SOLUTION.get().defaultBlockState());
			fizz(world, fromPos);
			return true;
		}
		return false;
	}
	
	public static void fizz(LevelAccessor world, BlockPos pos) {
		world.levelEvent(LevelEvent.LAVA_FIZZ, pos, 0);
	}
	
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);
		if (!world.getBlockState(pos.above()).isRedstoneConductor(world, pos.above()) && random.nextFloat() < 0.03F) {
			world.addParticle(SpectrumParticleTypes.VOID_FOG, pos.getX() + random.nextDouble(), pos.getY() + 1, pos.getZ() + random.nextDouble(), 0, random.nextDouble() * 0.1, 0);
		}
	}
	
	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
	
	@Override
	public boolean shouldSpreadLiquid(Level world, BlockPos pos, BlockState state) {
		// Shouldn't happen but check anyway
		// If it IS true then do nothing, since no interaction can take place at this position
		final FluidState fluidState = state.getFluidState();
		if (fluidState.isEmpty()) return true;
		
		for (Direction direction : Direction.values()) {
			BlockPos neighborPos = pos.relative(direction);
			FluidState neighborFluidState = world.getFluidState(neighborPos);
			
			// Do nothing if neighbor fluid state is empty. [matters for both collision and spread]
			if (neighborFluidState.isEmpty()) continue;
			
			// Fluid collision interaction
			final BlockState setState = handleFluidCollision(world, fluidState, neighborFluidState, direction);
			if (setState != null) {
				fireExtinguishEvent(world, pos);
				world.setBlockAndUpdate(pos, setState);
				return false;
			}
			
			// World interaction
			boolean isNeighborFluidBlock = world.getBlockState(neighborPos).getBlock() instanceof LiquidBlock;
			// spread to the fluid
			boolean doesTickEntities = world.getChunkAt(pos).getFullStatus().isOrAfter(FullChunkStatus.ENTITY_TICKING);
			if (!neighborFluidState.isEmpty() && doesTickEntities) {
				if (!isNeighborFluidBlock) {
					world.setBlockAndUpdate(pos, SPREAD_BLOCKSTATE);
					fireExtinguishEvent(world, pos);
				} else {
					if (!neighborFluidState.is(this.fluid) && !neighborFluidState.is(SpectrumFluidTags.MIDNIGHT_SOLUTION_CONVERTED) && !world.getBlockState(neighborPos).is(this)) {
						world.setBlockAndUpdate(pos, SPREAD_BLOCKSTATE);
						fireExtinguishEvent(world, neighborPos);
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public @Nullable BlockState handleFluidCollision(Level world, FluidState state, FluidState otherState, Direction direction) {
		if (otherState.is(FluidTags.LAVA)) return Blocks.BLACKSTONE.defaultBlockState();
		if (otherState.is(SpectrumFluidTags.SLUDGE)) return SpectrumBlocks.BLACK_SLUDGE.get().defaultBlockState();
		return null;
	}
	
	@Override
	public @Nullable PathType getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
		return PathType.DAMAGE_OTHER;
	}
	
	@Override
	public @Nullable PathType getAdjacentBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, PathType originalType) {
		return PathType.DANGER_OTHER;
	}
	
}
