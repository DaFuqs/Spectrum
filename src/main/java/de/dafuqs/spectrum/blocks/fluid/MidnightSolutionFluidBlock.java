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
import net.neoforged.neoforge.fluids.*;
import org.jspecify.annotations.*;

public class MidnightSolutionFluidBlock extends SpectrumFluidBlock {
	
	public static final BlockState SPREAD_BLOCKSTATE = SpectrumBlocks.BLACK_MATERIA.get().defaultBlockState().setValue(BlackMateriaBlock.AGE, 0);
	
	public MidnightSolutionFluidBlock(SpectrumFluid fluid, BlockState ultrawarmReplacementBlockState, Properties settings) {
		super(fluid, ultrawarmReplacementBlockState, settings);
	}
	
	@Override
	public SimpleParticleType getSplashParticle() {
		return SpectrumParticleTypes.MIDNIGHT_SOLUTION_SPLASH;
	}
	
	@Override
	public Tuple<SimpleParticleType, SimpleParticleType> getFishingParticles() {
		return new Tuple<>(SpectrumParticleTypes.MIDNIGHT_SOLUTION_SPLASH, SpectrumParticleTypes.MIDNIGHT_SOLUTION_FISHING);
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
	public @Nullable PathType getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
		return PathType.DAMAGE_OTHER;
	}
	
	@Override
	public @Nullable PathType getAdjacentBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, PathType originalType) {
		return PathType.DANGER_OTHER;
	}
	
}
