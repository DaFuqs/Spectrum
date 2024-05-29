package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.tag.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
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
			super.beforeBreakingBlock(world, pos, state);
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
	
	/**
	 * Entities colliding with liquid crystal will get a slight regeneration effect
	 */
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		
		if (!world.isClient && entity instanceof LivingEntity livingEntity) {
			// just check every x ticks for performance and slow healing
			if (world.getTime() % 200 == 0) {
				StatusEffectInstance regenerationInstance = livingEntity.getStatusEffect(StatusEffects.REGENERATION);
				if (regenerationInstance == null) {
					StatusEffectInstance newRegenerationInstance = new StatusEffectInstance(StatusEffects.REGENERATION, 80);
					livingEntity.addStatusEffect(newRegenerationInstance);
				}
			}
		}
	}
	
	@Override
	public RecipeType<? extends FluidConvertingRecipe> getDippingRecipeType() {
		return SpectrumRecipeTypes.LIQUID_CRYSTAL_CONVERTING;
	}
	
	public static class Flowing extends LiquidCrystalFluid {
		
		@Override
		protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
			super.appendProperties(builder);
			builder.add(LEVEL);
		}
		
		@Override
		protected boolean isInfinite(World world) {
			return false;
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
	
	public static class Still extends LiquidCrystalFluid {
		
		@Override
		protected boolean isInfinite(World world) {
			return false;
		}
		
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