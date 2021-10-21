package de.dafuqs.spectrum.dimension;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.RegistryLookupCodec;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

import java.util.List;

import static org.apache.logging.log4j.Level.INFO;

public class DeeperDownBiomeSource extends BiomeSource {

	public static final Identifier DEEPER_DOWN_BIOME_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "deeper_down_biome");

	public static final Codec<DeeperDownBiomeSource> CODEC =
			RecordCodecBuilder.create((instance) -> instance.group(
					Codec.LONG.fieldOf("seed").stable().forGetter((StarrySkyBiomeProvider) -> StarrySkyBiomeProvider.SEED),
					RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter((vanillaLayeredBiomeSource) -> vanillaLayeredBiomeSource.BIOME_REGISTRY))
			.apply(instance, instance.stable(DeeperDownBiomeSource::new)));

	private final long SEED;
	private final Registry<Biome> BIOME_REGISTRY;
	public static Registry<Biome> layersBiomeRegistry;

	private static final List<RegistryKey<Biome>> BIOMES = ImmutableList.of(RegistryKey.of(Registry.BIOME_KEY, DEEPER_DOWN_BIOME_IDENTIFIER));

	// reference: VanillaLayeredBiomeSource
	public DeeperDownBiomeSource(long seed, Registry<Biome> biomeRegistry) {
		super(BIOMES.stream().map((key) -> () -> (Biome) biomeRegistry.getOrThrow(key)));
		this.SEED = seed;
		this.BIOME_REGISTRY = biomeRegistry;
		DeeperDownBiomeSource.layersBiomeRegistry = biomeRegistry;
	}

	public static void register() {
		SpectrumCommon.log(INFO, "Registering the Deeper Down biome source...");
		Registry.register(Registry.BIOME_SOURCE, new Identifier(SpectrumCommon.MOD_ID, "deeper_down_biome_source"), DeeperDownBiomeSource.CODEC);
	}

	public Biome getBiomeForNoiseGen(int x, int y, int z) {
		return BIOME_REGISTRY.get(DEEPER_DOWN_BIOME_IDENTIFIER);
	}

	@Override
	protected Codec<? extends BiomeSource> getCodec() {
		return CODEC;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public BiomeSource withSeed(long seed) {
		return new DeeperDownBiomeSource(seed, this.BIOME_REGISTRY);
	}

}
