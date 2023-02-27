package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.world.gen.feature.*;

public record WeightedRandomFeaturePatchConfig(int tries, int xzSpread, int ySpread,
                                               WeightedRandomFeatureConfig weightedRandomFeatureConfig) implements FeatureConfig {

    public static final Codec<WeightedRandomFeaturePatchConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codecs.POSITIVE_INT.fieldOf("tries").forGetter((weightedRandomFeatureConfig) -> weightedRandomFeatureConfig.tries),
            Codecs.POSITIVE_INT.fieldOf("xzspread").forGetter((weightedRandomFeatureConfig) -> weightedRandomFeatureConfig.xzSpread),
            Codecs.POSITIVE_INT.fieldOf("yspread").forGetter((weightedRandomFeatureConfig) -> weightedRandomFeatureConfig.ySpread),
            WeightedRandomFeatureConfig.CODEC.fieldOf("feature").forGetter(WeightedRandomFeaturePatchConfig::featureConfig)
    ).apply(instance, WeightedRandomFeaturePatchConfig::new));

    public WeightedRandomFeatureConfig featureConfig() {
        return this.weightedRandomFeatureConfig;
    }

}