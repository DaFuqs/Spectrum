package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.block.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.feature.*;

public record RandomBlockCheckingPatchFeatureConfig(int tries, int xzSpread, int ySpread,
                                                    RegistryEntryList<Block> blocksToCheckFor, int blockScanRange,
                                                    RegistryEntry<PlacedFeature> closeToBlockFeature,
                                                    RegistryEntry<PlacedFeature> fallbackFeature) implements FeatureConfig {

    public static final Codec<RandomBlockCheckingPatchFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codecs.POSITIVE_INT.fieldOf("tries").orElse(128).forGetter(RandomBlockCheckingPatchFeatureConfig::tries),
            Codecs.NONNEGATIVE_INT.fieldOf("xz_spread").orElse(7).forGetter(RandomBlockCheckingPatchFeatureConfig::xzSpread),
            Codecs.NONNEGATIVE_INT.fieldOf("y_spread").orElse(3).forGetter(RandomBlockCheckingPatchFeatureConfig::ySpread),
            RegistryCodecs.entryList(Registry.BLOCK_KEY).fieldOf("blocks_to_find").forGetter(RandomBlockCheckingPatchFeatureConfig::blocksToCheckFor),
            Codecs.NONNEGATIVE_INT.fieldOf("block_scan_range").orElse(1).forGetter(RandomBlockCheckingPatchFeatureConfig::ySpread),
            PlacedFeature.REGISTRY_CODEC.fieldOf("close_to_block_feature").forGetter(RandomBlockCheckingPatchFeatureConfig::closeToBlockFeature),
            PlacedFeature.REGISTRY_CODEC.fieldOf("fallback_feature").forGetter(RandomBlockCheckingPatchFeatureConfig::fallbackFeature)
    ).apply(instance, RandomBlockCheckingPatchFeatureConfig::new));

}
