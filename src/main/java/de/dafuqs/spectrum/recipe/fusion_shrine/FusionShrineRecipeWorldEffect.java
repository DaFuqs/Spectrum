package de.dafuqs.spectrum.recipe.fusion_shrine;

import de.dafuqs.spectrum.blocks.fluid.MidnightSolutionFluidBlock;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;

import java.util.Optional;

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
	PLACE_MIDNIGHT_SOLUTION;

	public void doEffect(ServerWorld world, BlockPos shrinePos) {
		switch (this) {
			case WEATHER_CLEAR -> {
				world.setWeather(6000, 0, false, false);
			}
			case WEATHER_RAIN -> {
				world.setWeather(0, 6000, true, false);
			}
			case WEATHER_THUNDER -> {
				world.playSound(null, shrinePos.up(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.BLOCKS, 0.8F, 0.9F + world.random.nextFloat() * 0.2F);
				world.setWeather(0, 6000, true, true);
			}
			case LIGHTNING_ON_SHRINE -> {
				LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
				if(lightningEntity != null) {
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
					if(lightningEntity != null) {
						lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(randomTopPos));
						lightningEntity.setCosmetic(false);
						world.spawnEntity(lightningEntity);
					}
				}
			}
			case VISUAL_EXPLOSIONS_ON_SHRINE -> {
				if (world.getRandom().nextFloat() < 0.1) {
					world.playSound(null, shrinePos.up(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.5F, 0.8F + world.random.nextFloat() * 0.4F);
					SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity(world, shrinePos.up(), ParticleTypes.EXPLOSION, 1);
				}
			}
			case SINGLE_VISUAL_EXPLOSION_ON_SHRINE -> {
				world.playSound(null, shrinePos.up(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.4F);
				SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity(world, shrinePos, ParticleTypes.EXPLOSION, 1);
			}
			case PLACE_MIDNIGHT_SOLUTION, MAYBE_PLACE_MIDNIGHT_SOLUTION -> {
				if (this == PLACE_MIDNIGHT_SOLUTION || world.getRandom().nextFloat() < 0.05F) {
					Optional<BlockPos> targetPos = Support.getNexReplaceableBlockPosUpDown(world, shrinePos.add(5 - world.getRandom().nextInt(10), 1, 5 - world.getRandom().nextInt(10)), 5);
					if(targetPos.isPresent()) {
						world.setBlockState(targetPos.get(), SpectrumBlocks.MIDNIGHT_SOLUTION.getDefaultState());
						MidnightSolutionFluidBlock.playExtinguishSound(world, targetPos.get());
					}
				}
			}
		}
	}

	/**
	 * True for all effects that should just play once.
	 * Otherwise, it will be triggered each tick of the recipe
	 */
	public boolean isOneTimeEffect(FusionShrineRecipeWorldEffect effect) {
		return effect == LIGHTNING_ON_SHRINE || effect == SINGLE_VISUAL_EXPLOSION_ON_SHRINE || effect == WEATHER_CLEAR || effect == WEATHER_RAIN || effect == WEATHER_THUNDER;
	}


}
