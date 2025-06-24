package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.particle.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.pathfinder.*;
import org.jetbrains.annotations.*;

public class SludgeFluidBlock extends SpectrumFluidBlock {
	
	public SludgeFluidBlock(SpectrumFluid fluid, BlockState ultrawarmReplacementBlockState, Properties settings) {
		super(fluid, ultrawarmReplacementBlockState, settings);
	}

//	@Override
//	public MapCodec<? extends MudFluidBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}
	
	@Override
	public SimpleParticleType getSplashParticle() {
		return SpectrumParticleTypes.SLUDGE_SPLASH;
	}
	
	@Override
	public Tuple<SimpleParticleType, SimpleParticleType> getFishingParticles() {
		return new Tuple<>(SpectrumParticleTypes.SLUDGE_POP, SpectrumParticleTypes.SLUDGE_FISHING);
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);
		if (!world.getBlockState(pos.above()).isRedstoneConductor(world, pos.above()) && random.nextFloat() < 0.03F) {
			world.addParticle(SpectrumParticleTypes.SLUDGE_POP, pos.getX() + random.nextDouble(), pos.getY() + 1, pos.getZ() + random.nextDouble(), 0, random.nextDouble() * 0.1, 0);
		}
	}
	
	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return true;
	}
	
	@Override
	public @Nullable BlockState handleFluidCollision(Level world, @NotNull FluidState state, @NotNull FluidState otherState, Direction direction) {
		if (otherState.is(FluidTags.WATER)) {
			return direction == Direction.DOWN ? Blocks.COARSE_DIRT.defaultBlockState() : Blocks.DIRT.defaultBlockState();
		}
		if (otherState.is(FluidTags.LAVA)) {
			return Blocks.MUD.defaultBlockState();
		}
		return null;
	}
	
}
