package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.pathfinder.*;
import org.jspecify.annotations.Nullable;

public class LiquidCrystalFluidBlock extends SpectrumFluidBlock {
	
	public LiquidCrystalFluidBlock(SpectrumFluid fluid, BlockState ultrawarmReplacementBlockState, Properties settings) {
		super(fluid, ultrawarmReplacementBlockState, settings);
	}

//	@Override
//	public MapCodec<? extends LiquidCrystalFluidBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}
	
	@Override
	public SimpleParticleType getSplashParticle() {
		return SpectrumParticleTypes.LIQUID_CRYSTAL_FISHING;
	}
	
	@Override
	public Tuple<SimpleParticleType, SimpleParticleType> getFishingParticles() {
		return new Tuple<>(SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, SpectrumParticleTypes.LIQUID_CRYSTAL_FISHING);
	}
	
	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return true;
	}
	
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);
		if (random.nextFloat() < 0.10F) {
			world.addParticle(SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0, random.nextDouble() * 0.1, 0);
		}
	}
	
	@Override
	public @Nullable BlockState handleFluidCollision(Level world, FluidState state, FluidState otherState, Direction direction) {
		if (otherState.is(FluidTags.WATER)) {
			return state.isSource() && direction != Direction.DOWN ? SpectrumBlocks.FROSTBITE_CRYSTAL.get().defaultBlockState() : Blocks.CALCITE.defaultBlockState();
		} else if (otherState.is(FluidTags.LAVA)) {
			return state.isSource() && direction != Direction.DOWN ? SpectrumBlocks.BLAZING_CRYSTAL.get().defaultBlockState() : Blocks.COBBLED_DEEPSLATE.defaultBlockState();
		} else if (otherState.is(SpectrumFluidTags.SLUDGE)) {
			return Blocks.CLAY.defaultBlockState();
		}
		return null;
	}
	
}
