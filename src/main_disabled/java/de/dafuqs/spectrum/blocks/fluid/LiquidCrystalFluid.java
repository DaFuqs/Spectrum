package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;

public abstract class LiquidCrystalFluid extends SpectrumFluid {
	
	@Override
	public Fluid getSource() {
		return SpectrumFluids.LIQUID_CRYSTAL;
	}
	
	@Override
	public Fluid getFlowing() {
		return SpectrumFluids.FLOWING_LIQUID_CRYSTAL;
	}
	
	@Override
	public Item getBucket() {
		return SpectrumItems.LIQUID_CRYSTAL_BUCKET;
	}
	
	@Override
	protected BlockState createLegacyBlock(FluidState fluidState) {
		return SpectrumBlocks.LIQUID_CRYSTAL.defaultBlockState().setValue(BlockStateProperties.LEVEL, getLegacyLevel(fluidState));
	}
	
	@Override
	public boolean isSame(Fluid fluid) {
		return fluid == SpectrumFluids.LIQUID_CRYSTAL || fluid == SpectrumFluids.FLOWING_LIQUID_CRYSTAL;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void animateTick(Level world, BlockPos pos, FluidState state, RandomSource random) {
		BlockPos topPos = pos.above();
		BlockState topState = world.getBlockState(topPos);
		if (topState.isAir() && !topState.isSolidRender(world, topPos) && random.nextInt(1000) == 0) {
			world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SpectrumSoundEvents.LIQUID_CRYSTAL_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
		}
	}
	
	@Override
	protected void beforeDestroyingBlock(LevelAccessor world, BlockPos pos, BlockState state) {
		// if liquid crystal collides with a flower of any kind:
		// drop a resonant lily instead
		if (state.is(BlockTags.FLOWERS)) {
			Block.dropResources(SpectrumBlocks.RESONANT_LILY.defaultBlockState(), world, pos, null);
		} else {
			super.beforeDestroyingBlock(world, pos, state);
		}
	}
	
	@Override
	public ParticleOptions getDripParticle() {
		return SpectrumParticleTypes.DRIPPING_LIQUID_CRYSTAL;
	}
	
	@Override
	public ParticleOptions getSplashParticle() {
		return SpectrumParticleTypes.LIQUID_CRYSTAL_SPLASH;
	}
	
	/**
	 * Entities colliding with liquid crystal will get a slight regeneration effect
	 */
	@Override
	public void onEntityCollision(BlockState state, Level world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		
		if (!world.isClientSide && entity instanceof LivingEntity livingEntity) {
			// just check every x ticks for performance and slow healing
			if (world.getGameTime() % 200 == 0) {
				MobEffectInstance regenerationInstance = livingEntity.getEffect(MobEffects.REGENERATION);
				if (regenerationInstance == null) {
					MobEffectInstance newRegenerationInstance = new MobEffectInstance(MobEffects.REGENERATION, 80);
					livingEntity.addEffect(newRegenerationInstance);
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
		protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}
		
		@Override
		public int getAmount(FluidState fluidState) {
			return fluidState.getValue(LEVEL);
		}
		
		@Override
		public boolean isSource(FluidState fluidState) {
			return false;
		}
	}
	
	public static class Still extends LiquidCrystalFluid {
		
		@Override
		public int getAmount(FluidState fluidState) {
			return 8;
		}
		
		@Override
		public boolean isSource(FluidState fluidState) {
			return true;
		}
		
	}
}