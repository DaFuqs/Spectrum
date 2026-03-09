package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import net.neoforged.neoforge.fluids.*;
import org.jetbrains.annotations.*;

public abstract class DragonrotFluid extends SpectrumFluid {
	
	@Override
	public @NotNull FluidType getFluidType() {
		return SpectrumFluids.DRAGONROT_TYPE.get();
	}
	
	@Override
	public @NotNull Fluid getSource() {
		return SpectrumFluids.DRAGONROT.get();
	}
	
	@Override
	public @NotNull Fluid getFlowing() {
		return SpectrumFluids.FLOWING_DRAGONROT.get();
	}
	
	@Override
	public @NotNull Item getBucket() {
		return SpectrumItems.DRAGONROT_BUCKET.get();
	}
	
	@Override
	protected BlockState createLegacyBlock(FluidState fluidState) {
		return SpectrumBlocks.DRAGONROT.get().defaultBlockState().setValue(BlockStateProperties.LEVEL, getLegacyLevel(fluidState));
	}
	
	@Override
	public boolean isSame(@NotNull Fluid fluid) {
		return fluid == SpectrumFluids.DRAGONROT.get() || fluid == SpectrumFluids.FLOWING_DRAGONROT.get();
	}
	
	@Override
	public void animateTick(Level world, BlockPos pos, FluidState state, RandomSource random) {
		BlockPos topPos = pos.above();
		BlockState topState = world.getBlockState(topPos);
		if (topState.isAir() && random.nextInt(3) == 0) {
			float soundRandom = random.nextFloat();
			if (soundRandom < 0.0003F) {
				world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.HONEY_DRINK, SoundSource.AMBIENT, random.nextFloat() * 0.65F + 0.25F, random.nextFloat() * 0.2F, false);
			} else if (soundRandom < 0.0006F) {
				world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.HONEY_BLOCK_SLIDE, SoundSource.AMBIENT, random.nextFloat() * 0.4F + 0.25F, random.nextFloat() * 0.5F + 0.1F, false);
			} else if (soundRandom < 0.0008F) {
				world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.FROG_AMBIENT, SoundSource.AMBIENT, random.nextFloat() + 0.25F, random.nextFloat() * 0.3F + 0.01F, false);
			} else if (soundRandom < 0.001F) {
				world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SCULK_BLOCK_PLACE, SoundSource.AMBIENT, random.nextFloat() + 0.25F, random.nextFloat() * 0.4F + 0.2F, false);
			} else if (soundRandom < 0.00148F) {
				world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.PARROT_DEATH, SoundSource.AMBIENT, random.nextFloat() * 0.334F + 0.1F, 1F, false);
			} else if (soundRandom < 0.00152F) {
				world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.CAT_DEATH, SoundSource.AMBIENT, random.nextFloat() * 0.334F + 0.1F, 1F, false);
			} else if (soundRandom < 0.00156F) {
				world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.WOLF_DEATH, SoundSource.AMBIENT, random.nextFloat() * 0.3F + 0.1F, 1F, false);
			} else if (soundRandom < 0.001564F) {
				world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.AMBIENT, 2F, 0.1F, false);
			} else if (soundRandom < 0.001566F) {
				world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SpectrumSoundEvents.ENTITY_MONSTROSITY_AMBIENT, SoundSource.AMBIENT, random.nextFloat() * 0.65F + 0.25F, random.nextFloat(), false);
			}
		}
	}
	
	@Override
	protected int getDropOff(LevelReader worldView) {
		return 3;
	}
	
	@Override
	public int getTickDelay(LevelReader worldView) {
		return 40;
	}
	
	@Override
	public ParticleOptions getDripParticle() {
		return SpectrumParticleTypes.DRIPPING_DRAGONROT;
	}
	
	@Override
	public ParticleOptions getSplashParticle() {
		return SpectrumParticleTypes.DRAGONROT_SPLASH;
	}
	
	
	@Override
	public void onEntityCollision(BlockState state, Level world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		
		if (world instanceof ServerLevel serverWorld && entity instanceof LivingEntity livingEntity) {
			// just check every 20 ticks for performance
			if (!livingEntity.isDeadOrDying() && world.getGameTime() % 20 == 0 && !(livingEntity instanceof Enemy)) {
				var dragon = entity.getType().is(SpectrumEntityTypeTags.DRACONIC);
				var damage = dragon ? 30 : 6;
				var cut = dragon ? 100 : 40;
				
				if (livingEntity.isEyeInFluidType(SpectrumFluids.DRAGONROT_TYPE.get())) {
					livingEntity.hurt(SpectrumDamageTypes.dragonrot(world), damage);
				} else {
					livingEntity.hurt(SpectrumDamageTypes.dragonrot(world), damage / 2F);
				}
				if (!livingEntity.isDeadOrDying()) {
					MobEffectInstance existingEffect = livingEntity.getEffect(SpectrumMobEffects.LIFE_DRAIN);
					if (existingEffect == null) {
						livingEntity.addEffect(new MobEffectInstance(SpectrumMobEffects.LIFE_DRAIN, 600, 0));
					} else if (existingEffect.getDuration() < 500) {
						existingEffect.spectrum$setDuration(300);
						serverWorld.getChunkSource().broadcastAndSend(livingEntity, new ClientboundUpdateMobEffectPacket(livingEntity.getId(), existingEffect, true));
					}
					
					existingEffect = livingEntity.getEffect(SpectrumMobEffects.DEADLY_POISON);
					if (existingEffect == null || existingEffect.getDuration() < 80) {
						livingEntity.addEffect(new MobEffectInstance(SpectrumMobEffects.DEADLY_POISON, 160, 0));
					}
					
					existingEffect = livingEntity.getEffect(SpectrumMobEffects.IMMUNITY);
					if (existingEffect != null) {
						if (existingEffect.getDuration() <= cut) {
							livingEntity.removeEffect(SpectrumMobEffects.IMMUNITY);
						} else {
							existingEffect.spectrum$setDuration(existingEffect.getDuration() - cut);
							serverWorld.getChunkSource().broadcastAndSend(livingEntity, new ClientboundUpdateMobEffectPacket(livingEntity.getId(), existingEffect, true));
						}
					}
					
					if (!dragon)
						return;
					
					existingEffect = livingEntity.getEffect(SpectrumMobEffects.DENSITY);
					if (existingEffect == null || existingEffect.getDuration() < 120) {
						livingEntity.addEffect(new MobEffectInstance(SpectrumMobEffects.DENSITY, 2000, 1));
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
	
	public static class Still extends DragonrotFluid {
		
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