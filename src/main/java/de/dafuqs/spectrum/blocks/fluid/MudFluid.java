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
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;

public abstract class MudFluid extends SpectrumFluid {
	
	@Override
	public Fluid getStill() {
		return SpectrumFluids.MUD;
	}
	
	@Override
	public Fluid getFlowing() {
		return SpectrumFluids.FLOWING_MUD;
	}
	
	@Override
	public Item getBucketItem() {
		return SpectrumItems.MUD_BUCKET;
	}
	
	@Override
	protected BlockState toBlockState(FluidState fluidState) {
		return SpectrumBlocks.MUD.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
	}
	
	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == SpectrumFluids.MUD || fluid == SpectrumFluids.FLOWING_MUD;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
		BlockPos topPos = pos.up();
		BlockState topState = world.getBlockState(topPos);
		if (topState.isAir() && !topState.isOpaqueFullCube(world, topPos) && random.nextInt(1000) == 0) {
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), SpectrumSoundEvents.MUD_AMBIENT, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
		}
	}
	
	@Override
	protected int getFlowSpeed(WorldView worldView) {
		return 1;
	}
	
	@Override
	protected int getLevelDecreasePerBlock(WorldView worldView) {
		return 3;
	}
	
	@Override
	public int getTickRate(WorldView worldView) {
		return 50;
	}
	
	@Override
	public ParticleEffect getParticle() {
		return SpectrumParticleTypes.DRIPPING_MUD;
	}
	
	@Override
	public ParticleEffect getSplashParticle() {
		return SpectrumParticleTypes.MUD_SPLASH;
	}
	
	@Override
	public RecipeType<? extends FluidConvertingRecipe> getDippingRecipeType() {
		return SpectrumRecipeTypes.MUD_CONVERTING;
	}
	
	/**
	 * Entities colliding with mud will get a slowness effect
	 * and losing their breath far quicker
	 */
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		
		if (!world.isClient && entity instanceof LivingEntity livingEntity) {
			// the entity is hurt at air == -20 and then reset to air = 0
			// this way the entity loses its breath way faster, but gets damaged just as slow afterwards
			if (livingEntity.isSubmergedIn(SpectrumFluidTags.MUD) && world.getTime() % 2 == 0 && livingEntity.getAir() > 0) {
				livingEntity.setAir(livingEntity.getAir() - 1);
			}
			
			// just check every 20 ticks for performance
			if (world.getTime() % 20 == 0) {
				StatusEffectInstance slownessInstance = livingEntity.getStatusEffect(StatusEffects.SLOWNESS);
				if (slownessInstance == null || slownessInstance.getDuration() < 20) {
					StatusEffectInstance newSlownessInstance = new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 3);
					livingEntity.addStatusEffect(newSlownessInstance);
				}
			}
		}
	}
	
	public static class FlowingMud extends MudFluid {
		
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
	
	public static class StillMud extends MudFluid {
		
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