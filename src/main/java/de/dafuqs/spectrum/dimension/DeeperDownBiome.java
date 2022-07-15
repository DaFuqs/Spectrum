package de.dafuqs.spectrum.dimension;

import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumMusicType;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarvers;

public class DeeperDownBiome {
	
	public static final Biome INSTANCE = new Biome.Builder()
			.generationSettings(new GenerationSettings.Builder().build())
			.precipitation(Biome.Precipitation.NONE)
			.temperature(0.2F).downfall(0.2F)
			.category(Biome.Category.NONE)
			.temperatureModifier(Biome.TemperatureModifier.NONE)
			.effects(new BiomeEffects.Builder()
					.waterColor(4159204)
					.waterFogColor(329011)
					.fogColor(0)
					.skyColor(0x111111)
					.moodSound(new BiomeMoodSound(SpectrumSoundEvents.DIMENSION_SOUNDS, 6000, 8, 2))
					.particleConfig(new BiomeParticleConfig(SpectrumParticleTypes.VOID_FOG, 0.003F))
					.music(SpectrumMusicType.DEEPER_DOWN_THEME)
					.build())
			.spawnSettings(new SpawnSettings.Builder().creatureSpawnProbability(0).build())
			.generationSettings(new GenerationSettings.Builder()
					.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CAVE).carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CAVE_EXTRA_UNDERGROUND).build())
			.build();
	
}
