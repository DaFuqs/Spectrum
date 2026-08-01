package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.pathfinder.*;
import org.jspecify.annotations.*;

public class DragonrotFluidBlock extends SpectrumFluidBlock {
	
	public DragonrotFluidBlock(SpectrumFluid fluid, BlockState ultrawarmReplacementBlockState, Properties settings) {
		super(fluid, ultrawarmReplacementBlockState, settings);
	}

//	@Override
//	public MapCodec<? extends DragonrotFluidBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}
	
	@Override
	public SimpleParticleType getSplashParticle() {
		return SpectrumParticleTypes.DRAGONROT;
	}
	
	@Override
	public Tuple<SimpleParticleType, SimpleParticleType> getFishingParticles() {
		return new Tuple<>(SpectrumParticleTypes.DRAGONROT, SpectrumParticleTypes.DRAGONROT_FISHING);
	}
	
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);
		if (!world.getBlockState(pos.above()).isRedstoneConductor(world, pos.above()) && random.nextFloat() < 0.03F) {
			world.addParticle(SpectrumParticleTypes.DRAGONROT, pos.getX() + random.nextDouble(), pos.getY() + 1, pos.getZ() + random.nextDouble(), 0, random.nextDouble() * 0.1, 0);
		}
	}
	
	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
	
	@Override
	public @Nullable BlockState handleFluidCollision(Level world, FluidState state, FluidState otherState, Direction direction) {
		if (otherState.is(FluidTags.WATER)) {
			return SpectrumBlocks.SLUSH.get().defaultBlockState();
		} else if (otherState.is(FluidTags.LAVA)) {
			return Blocks.TERRACOTTA.defaultBlockState();
		} else if (otherState.is(SpectrumFluidTags.LIQUID_CRYSTAL)) {
			return Blocks.TUFF.defaultBlockState();
		} else if (otherState.is(SpectrumFluidTags.MIDNIGHT_SOLUTION)) {
			return Blocks.MUD.defaultBlockState();
		}
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
