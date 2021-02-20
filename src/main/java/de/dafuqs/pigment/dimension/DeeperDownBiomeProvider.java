package de.dafuqs.pigment.dimension;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.dafuqs.pigment.PigmentCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.RegistryLookupCodec;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

import java.util.List;

public class DeeperDownBiomeProvider extends BiomeSource {

    public static final Identifier DEEPER_DOWN_BIOME_IDENTIFIER = new Identifier(PigmentCommon.MOD_ID, "deeper_down");

    public static void register() {
        Registry.register(Registry.BIOME_SOURCE, new Identifier(PigmentCommon.MOD_ID, "biome_source"), DeeperDownBiomeProvider.CODEC);
    }

    public static final Codec<DeeperDownBiomeProvider> CODEC =
            RecordCodecBuilder.create((instance) -> instance.group(
                    Codec.LONG.fieldOf("seed").stable().forGetter((StarrySkyBiomeProvider) -> StarrySkyBiomeProvider.SEED),
                    RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter((vanillaLayeredBiomeSource) -> vanillaLayeredBiomeSource.BIOME_REGISTRY))
            .apply(instance, instance.stable(DeeperDownBiomeProvider::new)));

    private final long SEED;
    private final Registry<Biome> BIOME_REGISTRY;
    public static Registry<Biome> layersBiomeRegistry;

    private static final List<RegistryKey<Biome>> BIOMES = ImmutableList.of(
            RegistryKey.of(Registry.BIOME_KEY, DEEPER_DOWN_BIOME_IDENTIFIER));

    public DeeperDownBiomeProvider(long seed, Registry<Biome> biomeRegistry) {
        super(BIOMES.stream().map((registryKey) -> () -> (Biome)biomeRegistry.get(registryKey)));
        this.SEED = seed;
        this.BIOME_REGISTRY = biomeRegistry;
        DeeperDownBiomeProvider.layersBiomeRegistry = biomeRegistry;
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
        return new DeeperDownBiomeProvider(seed, this.BIOME_REGISTRY);
    }

}
