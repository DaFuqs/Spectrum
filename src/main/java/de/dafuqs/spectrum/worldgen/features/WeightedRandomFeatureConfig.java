package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.util.collection.*;
import net.minecraft.world.gen.feature.*;

public record WeightedRandomFeatureConfig(DataPool<PlacedFeature> features) implements FeatureConfig {

    public static final Codec<WeightedRandomFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            DataPool.createCodec(PlacedFeature.CODEC).fieldOf("features").forGetter((weightedRandomFeatureConfig) -> weightedRandomFeatureConfig.features)
    ).apply(instance, WeightedRandomFeatureConfig::new));

}