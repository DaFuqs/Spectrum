package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.api.recipe.*;
import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.entity.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.level.*;

import java.util.*;

public class SpectrumFusionShrineWorldEffects {

	public static FusionShrineRecipeWorldEffect WEATHER_CLEAR = FusionShrineRecipeWorldEffect.register("weather_clear", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			ServerWorldProperties serverWorldProperties = ((ServerWorldProperties) world.getLevelProperties());
			serverWorldProperties.setRainTime(0);
			serverWorldProperties.setRaining(false);
			serverWorldProperties.setThunderTime(0);
			serverWorldProperties.setThundering(false);
		}
	});
	public static FusionShrineRecipeWorldEffect WEATHER_RAIN = FusionShrineRecipeWorldEffect.register("weather_rain", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			ServerWorldProperties serverWorldProperties = ((ServerWorldProperties) world.getLevelProperties());
			serverWorldProperties.setRainTime(MathHelper.nextBetween(world.random, 12000, 18000));
			serverWorldProperties.setRaining(true);
			serverWorldProperties.setThunderTime(0);
			serverWorldProperties.setThundering(false);

			world.playSound(null, pos.up(), SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
		}
	});
	public static FusionShrineRecipeWorldEffect WEATHER_THUNDER = FusionShrineRecipeWorldEffect.register("weather_thunder", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			ServerWorldProperties serverWorldProperties = ((ServerWorldProperties) world.getLevelProperties());
			serverWorldProperties.setRainTime(MathHelper.nextBetween(world.random, 12000, 24000));
			serverWorldProperties.setRaining(true);
			serverWorldProperties.setThunderTime(MathHelper.nextBetween(world.random, 3600, 15600));
			serverWorldProperties.setThundering(true);
			world.playSound(null, pos.up(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
		}
	});
	public static FusionShrineRecipeWorldEffect WEATHER_RAIN_SHORT = FusionShrineRecipeWorldEffect.register("weather_rain_short", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			ServerWorldProperties serverWorldProperties = ((ServerWorldProperties) world.getLevelProperties());
			serverWorldProperties.setRainTime(MathHelper.nextBetween(world.random, 4000, 6000));
			serverWorldProperties.setRaining(true);
			serverWorldProperties.setThunderTime(0);
			serverWorldProperties.setThundering(false);

			world.playSound(null, pos.up(), SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
		}
	});
	public static FusionShrineRecipeWorldEffect WEATHER_THUNDER_SHORT = FusionShrineRecipeWorldEffect.register("weather_thunder_short", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			ServerWorldProperties serverWorldProperties = ((ServerWorldProperties) world.getLevelProperties());
			serverWorldProperties.setRainTime(MathHelper.nextBetween(world.random, 4000, 6000));
			serverWorldProperties.setRaining(true);
			serverWorldProperties.setThunderTime(MathHelper.nextBetween(world.random, 3000, 4000));
			serverWorldProperties.setThundering(true);
			world.playSound(null, pos.up(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
		}
	});
	public static FusionShrineRecipeWorldEffect LIGHTNING_ON_SHRINE = FusionShrineRecipeWorldEffect.register("lightning_on_shrine", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
			if (lightningEntity != null) {
				lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(pos));
				lightningEntity.setCosmetic(true);
				world.spawnEntity(lightningEntity);
			}
		}
	});
	public static FusionShrineRecipeWorldEffect LIGHTNING_AROUND_SHRINE = FusionShrineRecipeWorldEffect.register("lightning_around_shrine", new FusionShrineRecipeWorldEffect.EveryTickRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			if (world.getRandom().nextFloat() < 0.05F) {
				int randomX = pos.getX() + 12 - world.getRandom().nextInt(24);
				int randomZ = pos.getZ() + 12 - world.getRandom().nextInt(24);

				BlockPos randomTopPos = new BlockPos(randomX, world.getTopY(Heightmap.Type.WORLD_SURFACE, randomX, randomZ), randomZ);
				LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
				if (lightningEntity != null) {
					lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(randomTopPos));
					lightningEntity.setCosmetic(false);
					world.spawnEntity(lightningEntity);
				}
			}
		}
	});
	public static FusionShrineRecipeWorldEffect VISUAL_EXPLOSIONS_ON_SHRINE = FusionShrineRecipeWorldEffect.register("visual_explosions_on_shrine", new FusionShrineRecipeWorldEffect.EveryTickRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			if (world.getRandom().nextFloat() < 0.1) {
				world.playSound(null, pos.up(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.5F, 0.8F + world.random.nextFloat() * 0.4F);
				SpectrumS2CPacketSender.playParticles(world, pos.up(), ParticleTypes.EXPLOSION, 1);
			}
		}
	});
	public static FusionShrineRecipeWorldEffect SINGLE_VISUAL_EXPLOSION_ON_SHRINE = FusionShrineRecipeWorldEffect.register("single_visual_explosion_on_shrine", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			world.playSound(null, pos.up(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.4F);
			SpectrumS2CPacketSender.playParticles(world, pos, ParticleTypes.EXPLOSION, 1);
		}
	});
	public static FusionShrineRecipeWorldEffect MAYBE_PLACE_MIDNIGHT_SOLUTION = FusionShrineRecipeWorldEffect.register("maybe_place_midnight_solution", new FusionShrineRecipeWorldEffect.EveryTickRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			if (world.getRandom().nextFloat() < 0.05F) {
				Optional<BlockPos> targetPos = Support.getNexReplaceableBlockPosUpDown(world, pos.add(5 - world.getRandom().nextInt(10), 1, 5 - world.getRandom().nextInt(10)), 5);
				if (targetPos.isPresent()) {
					world.setBlockState(targetPos.get(), SpectrumBlocks.MIDNIGHT_SOLUTION.getDefaultState());
					MidnightSolutionFluidBlock.playExtinguishSound(world, targetPos.get());
				}
			}
		}
	});
	public static FusionShrineRecipeWorldEffect PLACE_MIDNIGHT_SOLUTION = FusionShrineRecipeWorldEffect.register("place_midnight_solution", new FusionShrineRecipeWorldEffect.EveryTickRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			Optional<BlockPos> targetPos = Support.getNexReplaceableBlockPosUpDown(world, pos.add(5 - world.getRandom().nextInt(10), 1, 5 - world.getRandom().nextInt(10)), 5);
			if (targetPos.isPresent()) {
				world.setBlockState(targetPos.get(), SpectrumBlocks.MIDNIGHT_SOLUTION.getDefaultState());
				MidnightSolutionFluidBlock.playExtinguishSound(world, targetPos.get());
			}
		}
	});
	public static FusionShrineRecipeWorldEffect EXPLOSIONS_AROUND_SHRINE = FusionShrineRecipeWorldEffect.register("explosions_around_shrine", new FusionShrineRecipeWorldEffect.EveryTickRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			if (world.getRandom().nextFloat() < 0.1) {
				float randomX = pos.getX() + 0.5F + 10 - world.getRandom().nextInt(20);
				float randomY = pos.getY() + 0.5F + 1 - world.getRandom().nextInt(3);
				float randomZ = pos.getZ() + 0.5F + 10 - world.getRandom().nextInt(20);
				world.createExplosion(null, randomX, randomY, randomZ, 4, World.ExplosionSourceType.NONE);
			}
		}
	});
	public static FusionShrineRecipeWorldEffect EXPLOSIONS_AND_LIGHTNING_AROUND_SHRINE = FusionShrineRecipeWorldEffect.register("explosions_and_lightning_around_shrine", new FusionShrineRecipeWorldEffect.EveryTickRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			if (world.getRandom().nextFloat() < 0.1) {
				float randomX = pos.getX() + 0.5F + 10 - world.getRandom().nextInt(20);
				float randomY = pos.getY() + 0.5F + 1 - world.getRandom().nextInt(3);
				float randomZ = pos.getZ() + 0.5F + 10 - world.getRandom().nextInt(20);
				world.createExplosion(null, randomX, randomY, randomZ, 4, World.ExplosionSourceType.NONE);
			}
			if (world.getRandom().nextFloat() < 0.05F) {
				int randomX = pos.getX() + 12 - world.getRandom().nextInt(24);
				int randomZ = pos.getZ() + 12 - world.getRandom().nextInt(24);

				BlockPos randomTopPos = new BlockPos(randomX, world.getTopY(Heightmap.Type.WORLD_SURFACE, randomX, randomZ), randomZ);
				LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
				if (lightningEntity != null) {
					lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(randomTopPos));
					lightningEntity.setCosmetic(false);
					world.spawnEntity(lightningEntity);
				}
			}
		}
	});
	public static FusionShrineRecipeWorldEffect PLAY_GLASS_BREAKING_SOUND = FusionShrineRecipeWorldEffect.register("play_glass_breaking_sound", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			world.playSound(null, pos.up(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	});
	public static FusionShrineRecipeWorldEffect RIDICULOUSLY_SQUEAKY_FART = FusionShrineRecipeWorldEffect.register("ridiculously_squeaky_fart", new FusionShrineRecipeWorldEffect.SingleTimeRecipeWorldEffect() {
		@Override
		public void trigger(ServerWorld world, BlockPos pos) {
			world.playSound(null, pos.up(), SpectrumSoundEvents.SQUEAKER, SoundCategory.BLOCKS, 1.4F, 1.2F + world.random.nextFloat() * 0.4F);
		}
	});

	public static void register() {

	}

}
