package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.mob.*;
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

public abstract class DragonrotFluid extends SpectrumFluid {

	@Override
	public Fluid getStill() {
		return SpectrumFluids.DRAGONROT;
	}
	
	@Override
	public Fluid getFlowing() {
		return SpectrumFluids.FLOWING_DRAGONROT;
	}
	
	@Override
	public Item getBucketItem() {
		return SpectrumItems.DRAGONROT_BUCKET;
	}
	
	@Override
	protected BlockState toBlockState(FluidState fluidState) {
		return SpectrumBlocks.DRAGONROT.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
	}
	
	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == SpectrumFluids.DRAGONROT || fluid == SpectrumFluids.FLOWING_DRAGONROT;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
		BlockPos topPos = pos.up();
		BlockState topState = world.getBlockState(topPos);
		if (topState.isAir() && !topState.isOpaqueFullCube(world, topPos)) {
			float soundRandom = random.nextFloat();
			if (soundRandom < 0.0003F) {
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_HONEY_BOTTLE_DRINK, SoundCategory.AMBIENT, random.nextFloat() * 0.65F + 0.25F, random.nextFloat() * 0.2F, false);
			} else if (soundRandom < 0.00048F) {
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SpectrumSoundEvents.MUD_AMBIENT, SoundCategory.AMBIENT, random.nextFloat() * 0.65F + 0.25F, random.nextFloat() * 0.2F, false);
			} else if (soundRandom < 0.0006F) {
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_HONEY_BLOCK_SLIDE, SoundCategory.AMBIENT, random.nextFloat() * 0.4F + 0.25F, random.nextFloat() * 0.5F + 0.1F, false);
			} else if (soundRandom < 0.0008F) {
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_FROG_AMBIENT, SoundCategory.AMBIENT, random.nextFloat() + 0.25F, random.nextFloat() * 0.3F + 0.01F, false);
			} else if (soundRandom < 0.001F) {
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_SCULK_PLACE, SoundCategory.AMBIENT, random.nextFloat() + 0.25F, random.nextFloat() * 0.4F + 0.2F, false);
			} else if (soundRandom < 0.0014F) {
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_HONEY_BLOCK_STEP, SoundCategory.AMBIENT, random.nextFloat() * 2F, 0.1F, false);
			} else if (soundRandom < 0.00144F) {
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_VILLAGER_DEATH, SoundCategory.AMBIENT, random.nextFloat() * 0.334F + 0.1F, 1F, false);
			} else if (soundRandom < 0.00148F) {
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_PARROT_DEATH, SoundCategory.AMBIENT, random.nextFloat() * 0.334F + 0.1F, 1F, false);
			} else if (soundRandom < 0.00152F) {
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_CAT_DEATH, SoundCategory.AMBIENT, random.nextFloat() * 0.334F + 0.1F, 1F, false);
			} else if (soundRandom < 0.00156F) {
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_WOLF_DEATH, SoundCategory.AMBIENT, random.nextFloat() * 0.3F + 0.1F, 1F, false);
			} else if (soundRandom < 0.001564F) {
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_SCULK_SHRIEKER_SHRIEK, SoundCategory.AMBIENT, 2F, 0.1F, false);
			} else if (soundRandom < 0.001566F) {
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SpectrumSoundEvents.ENTITY_MONSTROSITY_AMBIENT, SoundCategory.AMBIENT, random.nextFloat() * 0.65F + 0.25F, random.nextFloat(), false);
			}
		}
	}
	
	@Override
	protected int getFlowSpeed(WorldView worldView) {
		return 3;
	}
	
	@Override
	protected int getLevelDecreasePerBlock(WorldView worldView) {
		return 3;
	}
	
	@Override
	public int getTickRate(WorldView worldView) {
		return 40;
	}
	
	@Override
	public ParticleEffect getParticle() {
		return SpectrumParticleTypes.DRIPPING_DRAGONROT;
	}
	
	@Override
	public ParticleEffect getSplashParticle() {
		return SpectrumParticleTypes.DRAGONROT_SPLASH;
	}
	
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		
		if (!world.isClient && entity instanceof LivingEntity livingEntity) {
			// just check every 20 ticks for performance
			if (!livingEntity.isDead() && world.getTime() % 20 == 0 && !(livingEntity instanceof Monster)) {
				if (livingEntity.isSubmergedIn(SpectrumFluidTags.DRAGONROT)) {
					livingEntity.damage(SpectrumDamageTypes.dragonrot(world), 6);
				} else {
					livingEntity.damage(SpectrumDamageTypes.dragonrot(world), 3);
				}
				if (!livingEntity.isDead()) {
					StatusEffectInstance existingEffect = livingEntity.getStatusEffect(SpectrumStatusEffects.LIFE_DRAIN);
					if (existingEffect == null || existingEffect.getDuration() < 1000) {
						livingEntity.addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.LIFE_DRAIN, 2000, 0));
					}
					existingEffect = livingEntity.getStatusEffect(SpectrumStatusEffects.DEADLY_POISON);
					if (existingEffect == null || existingEffect.getDuration() < 80) {
						livingEntity.addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.DEADLY_POISON, 160, 0));
					}
				}
			}
		}
	}
	
	@Override
	public RecipeType<? extends FluidConvertingRecipe> getDippingRecipeType() {
		return SpectrumRecipeTypes.DRAGONROT_CONVERTING;
	}
	
	public static class Flowing extends DragonrotFluid {
		
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
	
	public static class Still extends DragonrotFluid {
		
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