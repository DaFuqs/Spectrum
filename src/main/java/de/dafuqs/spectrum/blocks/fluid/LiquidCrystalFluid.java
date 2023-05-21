package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.tag.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;

public abstract class LiquidCrystalFluid extends SpectrumFluid {
	
	@Override
	public Fluid getStill() {
		return SpectrumFluids.LIQUID_CRYSTAL;
	}
	
	@Override
	public Fluid getFlowing() {
		return SpectrumFluids.FLOWING_LIQUID_CRYSTAL;
	}
	
	@Override
	public Item getBucketItem() {
		return SpectrumItems.LIQUID_CRYSTAL_BUCKET;
	}
	
	@Override
	protected BlockState toBlockState(FluidState fluidState) {
		return SpectrumBlocks.LIQUID_CRYSTAL.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
	}
	
	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == SpectrumFluids.LIQUID_CRYSTAL || fluid == SpectrumFluids.FLOWING_LIQUID_CRYSTAL;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
		BlockPos topPos = pos.up();
		BlockState topState = world.getBlockState(topPos);
		if (topState.isAir() && !topState.isOpaqueFullCube(world, topPos) && random.nextInt(1000) == 0) {
			world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SpectrumSoundEvents.LIQUID_CRYSTAL_AMBIENT, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
		}
	}
	
	@Override
	protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
		// if liquid crystal collides with a flower of any kind:
		// drop a resonant lily instead
		if (state.isIn(BlockTags.FLOWERS)) {
			Block.dropStacks(SpectrumBlocks.RESONANT_LILY.getDefaultState(), world, pos, null);
		} else {
			final BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
			Block.dropStacks(state, world, pos, blockEntity);
		}
	}
	
	@Override
	public ParticleEffect getParticle() {
		return SpectrumParticleTypes.DRIPPING_LIQUID_CRYSTAL;
	}
	
	@Override
	public ParticleEffect getSplashParticle() {
		return SpectrumParticleTypes.LIQUID_CRYSTAL_SPLASH;
	}
	
	public static class FlowingLiquidCrystal extends LiquidCrystalFluid {
		
		@Override
		protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
			super.appendProperties(builder);
			builder.add(LEVEL);
		}
		
		@Override
		public int getLevel(FluidState fluidState) {
			return fluidState.get(LEVEL);
		}
		
		@Override
		public boolean isStill(FluidState fluidState) {
			return false;
		}
	}
	
	public static class StillLiquidCrystal extends LiquidCrystalFluid {
		
		@Override
		public int getLevel(FluidState fluidState) {
			return 8;
		}
		
		@Override
		public boolean isStill(FluidState fluidState) {
			return true;
		}
		
	}
}