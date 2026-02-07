package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.pathfinder.*;
import net.neoforged.neoforge.fluids.*;
import org.jetbrains.annotations.*;

public abstract class SludgeFluid extends SpectrumFluid {
	
	@Override
	public @NotNull FluidType getFluidType() {
		return SpectrumFluids.SLUDGE_TYPE.get();
	}
	
	@Override
	public @NotNull Fluid getSource() {
		return SpectrumFluids.SLUDGE.get();
	}
	
	@Override
	public @NotNull Fluid getFlowing() {
		return SpectrumFluids.FLOWING_SLUDGE.get();
	}
	
	@Override
	public @NotNull Item getBucket() {
		return SpectrumItems.SLUDGE_BUCKET.get();
	}
	
	@Override
	protected BlockState createLegacyBlock(FluidState fluidState) {
		return SpectrumBlocks.SLUDGE.get().defaultBlockState().setValue(BlockStateProperties.LEVEL, getLegacyLevel(fluidState));
	}
	
	@Override
	public boolean isSame(@NotNull Fluid fluid) {
		return fluid == SpectrumFluids.SLUDGE.get() || fluid == SpectrumFluids.FLOWING_SLUDGE.get();
	}
	
	@Override
	public void animateTick(Level world, BlockPos pos, FluidState state, RandomSource random) {
		BlockPos topPos = pos.above();
		BlockState topState = world.getBlockState(topPos);
		if (topState.isAir() && !topState.isSolidRender(world, topPos) && random.nextInt(1000) == 0) {
			world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SpectrumSoundEvents.SLUDGE_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
		}
	}
	
	@Override
	protected int getSlopeFindDistance(LevelReader worldView) {
		return 1;
	}
	
	@Override
	protected int getDropOff(LevelReader worldView) {
		return 3;
	}
	
	@Override
	public int getTickDelay(LevelReader worldView) {
		return 50;
	}
	
	@Override
	public ParticleOptions getDripParticle() {
		return SpectrumParticleTypes.DRIPPING_SLUDGE;
	}
	
	@Override
	public ParticleOptions getSplashParticle() {
		return SpectrumParticleTypes.SLUDGE_SPLASH;
	}
	
	@Override
	public RecipeType<? extends FluidConvertingRecipe> getDippingRecipeType() {
		return SpectrumRecipeTypes.SLUDGE_CONVERTING;
	}
	
	/**
	 * Entities colliding with sludge will get a slowness effect
	 * and losing their breath far quicker
	 */
	@Override
	public void onEntityCollision(BlockState state, Level world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		
		if (!world.isClientSide && entity instanceof LivingEntity livingEntity) {
			// the entity is hurt at air == -20 and then reset to air = 0
			// this way the entity loses its breath way faster, but gets damaged just as slow afterwards
			if (livingEntity.isEyeInFluid(SpectrumFluidTags.SLUDGE) && world.getGameTime() % 2 == 0 && livingEntity.getAirSupply() > 0) {
				livingEntity.setAirSupply(livingEntity.getAirSupply() - 1);
			}
			
			// just check every 20 ticks for performance
			if (world.getGameTime() % 20 == 0) {
				MobEffectInstance slownessInstance = livingEntity.getEffect(MobEffects.MOVEMENT_SLOWDOWN);
				if (slownessInstance == null || slownessInstance.getDuration() < 20) {
					MobEffectInstance newSlownessInstance = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 3);
					livingEntity.addEffect(newSlownessInstance);
				}
			}
		}
	}
	
	public static class FlowingSludge extends SludgeFluid {
		
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
	
	public static class StillSludge extends SludgeFluid {
		
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