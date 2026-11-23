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
import net.minecraft.world.level.pathfinder.*;
import net.neoforged.neoforge.event.*;
import org.jspecify.annotations.*;

public abstract class SpectrumFluidBlock extends LiquidBlock {
	
	public final BlockState ultrawarmReplacementBlockState;
	
	public SpectrumFluidBlock(SpectrumFluid fluid, BlockState ultrawarmReplacementBlockState, Properties settings) {
		super(fluid, settings);
		this.ultrawarmReplacementBlockState = ultrawarmReplacementBlockState;
	}
	
	// TODO: unused
	public abstract SimpleParticleType getSplashParticle();
	
	public abstract Tuple<SimpleParticleType, SimpleParticleType> getFishingParticles();
	
	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		super.entityInside(state, level, pos, entity);
		((SpectrumFluid) fluid).onEntityCollision(state, level, pos, entity);
	}
	
	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean notify) {
		if (level.dimensionType().ultraWarm()) {
			level.setBlockAndUpdate(pos, ultrawarmReplacementBlockState);
			
			level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.8F);
			
			for (int l = 0; l < 8; ++l) {
				level.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0.0, 0.0, 0.0);
			}
			
			return;
		}
		
		super.onPlace(state, level, pos, oldState, notify);
	}
	
	@Override
	public @Nullable PathType getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
		return PathType.WATER;
	}
	
	@Override
	public @Nullable PathType getAdjacentBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, PathType originalType) {
		return PathType.WATER_BORDER;
	}
	
}
