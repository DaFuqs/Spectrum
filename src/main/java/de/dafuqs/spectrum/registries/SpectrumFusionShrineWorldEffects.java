package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.storage.*;
import net.minecraft.world.phys.*;

import java.util.*;

@SuppressWarnings("unused")
public class SpectrumFusionShrineWorldEffects {
	
	public static FusionShrineRecipeWorldEffect WEATHER_CLEAR = FusionShrineRecipeWorldEffect.register("weather_clear", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerLevel world, BlockPos pos) {
			ServerLevelData serverWorldProperties = ((ServerLevelData) world.getLevelData());
			serverWorldProperties.setRainTime(0);
			serverWorldProperties.setRaining(false);
			serverWorldProperties.setThunderTime(0);
			serverWorldProperties.setThundering(false);
		}
	});
	public static FusionShrineRecipeWorldEffect WEATHER_RAIN = FusionShrineRecipeWorldEffect.register("weather_rain", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerLevel world, BlockPos pos) {
			ServerLevelData serverWorldProperties = ((ServerLevelData) world.getLevelData());
			serverWorldProperties.setRainTime(Mth.randomBetweenInclusive(world.random, 12000, 18000));
			serverWorldProperties.setRaining(true);
			serverWorldProperties.setThunderTime(0);
			serverWorldProperties.setThundering(false);
			
			world.playSound(null, pos.above(), SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
		}
	});
	public static FusionShrineRecipeWorldEffect WEATHER_THUNDER = FusionShrineRecipeWorldEffect.register("weather_thunder", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerLevel world, BlockPos pos) {
			ServerLevelData serverWorldProperties = ((ServerLevelData) world.getLevelData());
			serverWorldProperties.setRainTime(Mth.randomBetweenInclusive(world.random, 12000, 24000));
			serverWorldProperties.setRaining(true);
			serverWorldProperties.setThunderTime(Mth.randomBetweenInclusive(world.random, 3600, 15600));
			serverWorldProperties.setThundering(true);
			world.playSound(null, pos.above(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
		}
	});
	public static FusionShrineRecipeWorldEffect WEATHER_RAIN_SHORT = FusionShrineRecipeWorldEffect.register("weather_rain_short", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerLevel world, BlockPos pos) {
			ServerLevelData serverWorldProperties = ((ServerLevelData) world.getLevelData());
			serverWorldProperties.setRainTime(Mth.randomBetweenInclusive(world.random, 4000, 6000));
			serverWorldProperties.setRaining(true);
			serverWorldProperties.setThunderTime(0);
			serverWorldProperties.setThundering(false);
			
			world.playSound(null, pos.above(), SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
		}
	});
	public static FusionShrineRecipeWorldEffect WEATHER_THUNDER_SHORT = FusionShrineRecipeWorldEffect.register("weather_thunder_short", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerLevel world, BlockPos pos) {
			ServerLevelData serverWorldProperties = ((ServerLevelData) world.getLevelData());
			serverWorldProperties.setRainTime(Mth.randomBetweenInclusive(world.random, 4000, 6000));
			serverWorldProperties.setRaining(true);
			serverWorldProperties.setThunderTime(Mth.randomBetweenInclusive(world.random, 3000, 4000));
			serverWorldProperties.setThundering(true);
			world.playSound(null, pos.above(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
		}
	});
	public static FusionShrineRecipeWorldEffect LIGHTNING_ON_SHRINE = FusionShrineRecipeWorldEffect.register("lightning_on_shrine", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerLevel world, BlockPos pos) {
			LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
			if (lightningEntity != null) {
				lightningEntity.moveTo(Vec3.atBottomCenterOf(pos));
				lightningEntity.setVisualOnly(true);
				world.addFreshEntity(lightningEntity);
			}
		}
	});
	public static FusionShrineRecipeWorldEffect LIGHTNING_AROUND_SHRINE = FusionShrineRecipeWorldEffect.register("lightning_around_shrine", new FusionShrineRecipeWorldEffect.EveryTickRecipeWorldEffect() {
		@Override
		public void trigger(ServerLevel world, BlockPos pos) {
			if (world.getRandom().nextFloat() < 0.05F) {
				int randomX = pos.getX() + 12 - world.getRandom().nextInt(24);
				int randomZ = pos.getZ() + 12 - world.getRandom().nextInt(24);
				
				BlockPos randomTopPos = new BlockPos(randomX, world.getHeight(Heightmap.Types.WORLD_SURFACE, randomX, randomZ), randomZ);
				LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
				if (lightningEntity != null) {
					lightningEntity.moveTo(Vec3.atBottomCenterOf(randomTopPos));
					lightningEntity.setVisualOnly(false);
					world.addFreshEntity(lightningEntity);
				}
			}
		}
	});
	public static FusionShrineRecipeWorldEffect VISUAL_EXPLOSIONS_ON_SHRINE = FusionShrineRecipeWorldEffect.register("visual_explosions_on_shrine", new FusionShrineRecipeWorldEffect.EveryTickRecipeWorldEffect() {
		@Override
		public void trigger(ServerLevel world, BlockPos pos) {
			if (world.getRandom().nextFloat() < 0.1) {
				world.playSound(null, pos.above(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 0.5F, 0.8F + world.random.nextFloat() * 0.4F);
				PlayParticleWithExactVelocityPayload.playParticles(world, pos.above(), ParticleTypes.EXPLOSION, 1);
			}
		}
	});
	public static FusionShrineRecipeWorldEffect SINGLE_VISUAL_EXPLOSION_ON_SHRINE = FusionShrineRecipeWorldEffect.register("single_visual_explosion_on_shrine", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerLevel world, BlockPos pos) {
			world.playSound(null, pos.above(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.4F);
			PlayParticleWithExactVelocityPayload.playParticles(world, pos, ParticleTypes.EXPLOSION, 1);
		}
	});
	public static FusionShrineRecipeWorldEffect MAYBE_PLACE_MIDNIGHT_SOLUTION = FusionShrineRecipeWorldEffect.register("maybe_place_midnight_solution", new FusionShrineRecipeWorldEffect.EveryTickRecipeWorldEffect() {
		@Override
		public void trigger(ServerLevel world, BlockPos pos) {
			if (world.getRandom().nextFloat() < 0.05F) {
				Optional<BlockPos> targetPos = Support.getNexReplaceableBlockPosUpDown(world, pos.offset(5 - world.getRandom().nextInt(10), 1, 5 - world.getRandom().nextInt(10)), 5);
				if (targetPos.isPresent()) {
					BlockPos p = targetPos.get();
					if (p.getX() == pos.getX() && p.getZ() == pos.getZ()) {
						return;
					}
					world.setBlockAndUpdate(targetPos.get(), SpectrumBlocks.MIDNIGHT_SOLUTION.defaultBlockState());
					MidnightSolutionFluidBlock.fizz(world, targetPos.get());
				}
			}
		}
	});
	public static FusionShrineRecipeWorldEffect PLACE_MIDNIGHT_SOLUTION = FusionShrineRecipeWorldEffect.register("place_midnight_solution", new FusionShrineRecipeWorldEffect.EveryTickRecipeWorldEffect() {
		@Override
		public void trigger(ServerLevel world, BlockPos pos) {
			Optional<BlockPos> targetPos = Support.getNexReplaceableBlockPosUpDown(world, pos.offset(5 - world.getRandom().nextInt(10), 1, 5 - world.getRandom().nextInt(10)), 5);
			if (targetPos.isPresent()) {
				BlockPos p = targetPos.get();
				if (p.getX() == pos.getX() && p.getZ() == pos.getZ()) {
					return;
				}
				world.setBlockAndUpdate(p, SpectrumBlocks.MIDNIGHT_SOLUTION.defaultBlockState());
				MidnightSolutionFluidBlock.fizz(world, targetPos.get());
			}
		}
	});
	public static FusionShrineRecipeWorldEffect EXPLOSIONS_AROUND_SHRINE = FusionShrineRecipeWorldEffect.register("explosions_around_shrine", new FusionShrineRecipeWorldEffect.EveryTickRecipeWorldEffect() {
		@Override
		public void trigger(ServerLevel world, BlockPos pos) {
			if (world.getRandom().nextFloat() < 0.1) {
				float randomX = pos.getX() + 0.5F + 10 - world.getRandom().nextInt(20);
				float randomY = pos.getY() + 0.5F + 1 - world.getRandom().nextInt(3);
				float randomZ = pos.getZ() + 0.5F + 10 - world.getRandom().nextInt(20);
				world.explode(null, randomX, randomY, randomZ, 4, Level.ExplosionInteraction.NONE);
			}
		}
	});
	public static FusionShrineRecipeWorldEffect EXPLOSIONS_AND_LIGHTNING_AROUND_SHRINE = FusionShrineRecipeWorldEffect.register("explosions_and_lightning_around_shrine", new FusionShrineRecipeWorldEffect.EveryTickRecipeWorldEffect() {
		@Override
		public void trigger(ServerLevel world, BlockPos pos) {
			if (world.getRandom().nextFloat() < 0.1) {
				float randomX = pos.getX() + 0.5F + 10 - world.getRandom().nextInt(20);
				float randomY = pos.getY() + 0.5F + 1 - world.getRandom().nextInt(3);
				float randomZ = pos.getZ() + 0.5F + 10 - world.getRandom().nextInt(20);
				world.explode(null, randomX, randomY, randomZ, 4, Level.ExplosionInteraction.NONE);
			}
			if (world.getRandom().nextFloat() < 0.05F) {
				int randomX = pos.getX() + 12 - world.getRandom().nextInt(24);
				int randomZ = pos.getZ() + 12 - world.getRandom().nextInt(24);
				
				BlockPos randomTopPos = new BlockPos(randomX, world.getHeight(Heightmap.Types.WORLD_SURFACE, randomX, randomZ), randomZ);
				LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
				if (lightningEntity != null) {
					lightningEntity.moveTo(Vec3.atBottomCenterOf(randomTopPos));
					lightningEntity.setVisualOnly(false);
					world.addFreshEntity(lightningEntity);
				}
			}
		}
	});
	public static FusionShrineRecipeWorldEffect PLAY_GLASS_BREAKING_SOUND = FusionShrineRecipeWorldEffect.register("play_glass_breaking_sound", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerLevel world, BlockPos pos) {
			world.playSound(null, pos.above(), SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	});
	
	public static FusionShrineRecipeWorldEffect LEGENDARY_TOOL_CRAFT = FusionShrineRecipeWorldEffect.register("legendary_tool_craft", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerLevel world, BlockPos pos) {
			world.playSound(null, pos.above(), SpectrumSoundEvents.LEGENDARY_WEAPON_CRAFT, SoundSource.BLOCKS, 1.5F, 1F);
		}
	});
	
	public static FusionShrineRecipeWorldEffect RIDICULOUSLY_SQUEAKY_FART = FusionShrineRecipeWorldEffect.register("ridiculously_squeaky_fart", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerLevel world, BlockPos pos) {
			world.playSound(null, pos.above(), SpectrumSoundEvents.SQUEAKER, SoundSource.BLOCKS, 1.4F, 1.2F + world.random.nextFloat() * 0.4F);
		}
	});
	
	public static void register() {
	
	}
	
}
