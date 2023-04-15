package de.dafuqs.spectrum.recipe.fusion_shrine;

import de.dafuqs.spectrum.blocks.fluid.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.explosion.*;
import net.minecraft.world.level.*;

import java.util.*;

/**
 * Effects that are played when crafting with the fusion shrine
 */
public enum FusionShrineRecipeWorldEffect {
	NOTHING,
	WEATHER_CLEAR,
	WEATHER_RAIN,
	WEATHER_THUNDER,
	LIGHTNING_ON_SHRINE,
	LIGHTNING_AROUND_SHRINE,
	VISUAL_EXPLOSIONS_ON_SHRINE,
	SINGLE_VISUAL_EXPLOSION_ON_SHRINE,
	MAYBE_PLACE_MIDNIGHT_SOLUTION,
	PLACE_MIDNIGHT_SOLUTION,
	EXPLOSIONS_AROUND_SHRINE,
	EXPLOSIONS_AND_LIGHTNING_AROUND_SHRINE,
	PLAY_GLASS_BREAKING_SOUND,
	RIDICULOUSLY_SQUEAKY_FART;

	public void doEffect(ServerWorld world, BlockPos shrinePos) {
		switch (this) {
			case WEATHER_CLEAR -> {
				ServerWorldProperties serverWorldProperties = ((ServerWorldProperties) world.getLevelProperties());
				serverWorldProperties.setRainTime(0);
				serverWorldProperties.setRaining(false);
				serverWorldProperties.setThunderTime(0);
				serverWorldProperties.setThundering(false);
			}
			case WEATHER_RAIN -> {
				ServerWorldProperties serverWorldProperties = ((ServerWorldProperties) world.getLevelProperties());
				serverWorldProperties.setRainTime(MathHelper.nextBetween(world.random, 12000, 24000));
				serverWorldProperties.setRaining(true);
				serverWorldProperties.setThunderTime(MathHelper.nextBetween(world.random, 3600, 15600));
				serverWorldProperties.setThundering(false);
				
				world.playSound(null, shrinePos.up(), SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
			}
			case WEATHER_THUNDER -> {
				ServerWorldProperties serverWorldProperties = ((ServerWorldProperties) world.getLevelProperties());
				serverWorldProperties.setRainTime(MathHelper.nextBetween(world.random, 12000, 180000));
				serverWorldProperties.setRaining(true);
				serverWorldProperties.setThunderTime(0);
				serverWorldProperties.setThundering(false);
				
				world.playSound(null, shrinePos.up(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
			}
			case LIGHTNING_ON_SHRINE -> {
				LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
				if (lightningEntity != null) {
					lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(shrinePos));
					lightningEntity.setCosmetic(true);
					world.spawnEntity(lightningEntity);
				}
			}
			case LIGHTNING_AROUND_SHRINE -> {
				if (world.getRandom().nextFloat() < 0.05F) {
					int randomX = shrinePos.getX() + 12 - world.getRandom().nextInt(24);
					int randomZ = shrinePos.getZ() + 12 - world.getRandom().nextInt(24);
					
					BlockPos randomTopPos = new BlockPos(randomX, world.getTopY(Heightmap.Type.WORLD_SURFACE, randomX, randomZ), randomZ);
					LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
					if (lightningEntity != null) {
						lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(randomTopPos));
						lightningEntity.setCosmetic(false);
						world.spawnEntity(lightningEntity);
					}
				}
			}
			case VISUAL_EXPLOSIONS_ON_SHRINE -> {
				if (world.getRandom().nextFloat() < 0.1) {
					world.playSound(null, shrinePos.up(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.5F, 0.8F + world.random.nextFloat() * 0.4F);
					SpectrumS2CPacketSender.playParticles(world, shrinePos.up(), ParticleTypes.EXPLOSION, 1);
				}
			}
			case EXPLOSIONS_AROUND_SHRINE -> {
				if (world.getRandom().nextFloat() < 0.1) {
					float randomX = shrinePos.getX() + 0.5F + 10 - world.getRandom().nextInt(20);
					float randomY = shrinePos.getY() + 0.5F + 1 - world.getRandom().nextInt(3);
					float randomZ = shrinePos.getZ() + 0.5F + 10 - world.getRandom().nextInt(20);
					world.createExplosion(null, randomX, randomY, randomZ, 4, Explosion.DestructionType.NONE);
				}
			}
			case EXPLOSIONS_AND_LIGHTNING_AROUND_SHRINE -> {
				if (world.getRandom().nextFloat() < 0.1) {
					float randomX = shrinePos.getX() + 0.5F + 10 - world.getRandom().nextInt(20);
					float randomY = shrinePos.getY() + 0.5F + 1 - world.getRandom().nextInt(3);
					float randomZ = shrinePos.getZ() + 0.5F + 10 - world.getRandom().nextInt(20);
					world.createExplosion(null, randomX, randomY, randomZ, 4, Explosion.DestructionType.NONE);
				}
				if (world.getRandom().nextFloat() < 0.05F) {
					int randomX = shrinePos.getX() + 12 - world.getRandom().nextInt(24);
					int randomZ = shrinePos.getZ() + 12 - world.getRandom().nextInt(24);

					BlockPos randomTopPos = new BlockPos(randomX, world.getTopY(Heightmap.Type.WORLD_SURFACE, randomX, randomZ), randomZ);
					LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
					if (lightningEntity != null) {
						lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(randomTopPos));
						lightningEntity.setCosmetic(false);
						world.spawnEntity(lightningEntity);
					}
				}
			}
			case SINGLE_VISUAL_EXPLOSION_ON_SHRINE -> {
				world.playSound(null, shrinePos.up(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.4F);
				SpectrumS2CPacketSender.playParticles(world, shrinePos, ParticleTypes.EXPLOSION, 1);
			}
			case PLAY_GLASS_BREAKING_SOUND -> {
				world.playSound(null, shrinePos.up(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
			case PLACE_MIDNIGHT_SOLUTION, MAYBE_PLACE_MIDNIGHT_SOLUTION -> {
				if (this == PLACE_MIDNIGHT_SOLUTION || world.getRandom().nextFloat() < 0.05F) {
					Optional<BlockPos> targetPos = Support.getNexReplaceableBlockPosUpDown(world, shrinePos.add(5 - world.getRandom().nextInt(10), 1, 5 - world.getRandom().nextInt(10)), 5);
					if (targetPos.isPresent()) {
						world.setBlockState(targetPos.get(), SpectrumBlocks.MIDNIGHT_SOLUTION.getDefaultState());
						MidnightSolutionFluidBlock.playExtinguishSound(world, targetPos.get());
					}
				}
			}
			case RIDICULOUSLY_SQUEAKY_FART -> {
				world.playSound(null, shrinePos.up(), SpectrumSoundEvents.SQUEAKER, SoundCategory.BLOCKS, 1.0F, 1.2F + world.random.nextFloat() * 0.4F);
			}
		}
	}
	
	/**
	 * True for all effects that should just play once.
	 * Otherwise, it will be triggered each tick of the recipe
	 */
	public boolean isOneTimeEffect(FusionShrineRecipeWorldEffect effect) {
		return effect == LIGHTNING_ON_SHRINE || effect == SINGLE_VISUAL_EXPLOSION_ON_SHRINE
				|| effect == PLAY_GLASS_BREAKING_SOUND || effect == RIDICULOUSLY_SQUEAKY_FART
				|| effect == WEATHER_CLEAR || effect == WEATHER_RAIN || effect == WEATHER_THUNDER;
	}
	
	
}
