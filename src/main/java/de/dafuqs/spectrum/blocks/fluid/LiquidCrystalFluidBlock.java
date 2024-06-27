package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LiquidCrystalFluidBlock extends SpectrumFluidBlock {
	
	public static final int LUMINANCE = 11;
	
	public LiquidCrystalFluidBlock(SpectrumFluid fluid, BlockState ultrawarmReplacementBlockState, Settings settings) {
		super(fluid, ultrawarmReplacementBlockState, settings);
	}
	
	@Override
	public DefaultParticleType getSplashParticle() {
		return SpectrumParticleTypes.LIQUID_CRYSTAL_FISHING;
	}
	
	@Override
	public Pair<DefaultParticleType, DefaultParticleType> getFishingParticles() {
		return new Pair<>(SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, SpectrumParticleTypes.LIQUID_CRYSTAL_FISHING);
	}
	
	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return true;
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if (random.nextFloat() < 0.10F) {
			world.addParticle(SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0, random.nextDouble() * 0.1, 0);
		}
	}

	public @Nullable BlockState handleFluidCollision(World world, @NotNull FluidState state, @NotNull FluidState otherState) {
		if (otherState.isIn(FluidTags.WATER)) {
			return state.isStill() ? SpectrumBlocks.FROSTBITE_CRYSTAL.getDefaultState() : Blocks.CALCITE.getDefaultState();
		}
		else if (otherState.isIn(FluidTags.LAVA)) {
			return state.isStill() ? SpectrumBlocks.BLAZING_CRYSTAL.getDefaultState() : Blocks.COBBLED_DEEPSLATE.getDefaultState();
		}
		else if (otherState.isIn(SpectrumFluidTags.MUD)) {
			return Blocks.CLAY.getDefaultState();
		}
		return null;
	}
	
}
