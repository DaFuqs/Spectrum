package de.dafuqs.spectrum.deeper_down;

import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumMusicType;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarvers;

public class DDBiome {
	
	public static final Biome INSTANCE = new Biome.Builder()
			.generationSettings(new GenerationSettings.Builder().build())
			.precipitation(Biome.Precipitation.NONE)
			.temperature(0.2F).downfall(0.2F)
			.temperatureModifier(Biome.TemperatureModifier.NONE)
			.effects(new BiomeEffects.Builder()
					.waterColor(0xd3cbb1)
					.waterFogColor(0x746a62)
					.fogColor(0x000000)
					.skyColor(0x000000)
					.foliageColor(0x3a482f)
					.grassColor(0x6e7d4e)
					.moodSound(new BiomeMoodSound(SpectrumSoundEvents.DIMENSION_SOUNDS, 6000, 8, 2))
					.particleConfig(new BiomeParticleConfig(SpectrumParticleTypes.VOID_FOG, 0.003F))
					.music(SpectrumMusicType.DEEPER_DOWN_THEME)
					.build())
			.spawnSettings(new SpawnSettings.Builder().creatureSpawnProbability(0).build())
			.generationSettings(new GenerationSettings.Builder()
					.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CAVE).carver(GenerationStep.Carver.AIR, ConfiguredCarvers.CAVE_EXTRA_UNDERGROUND).build())
			.build();
	
}
