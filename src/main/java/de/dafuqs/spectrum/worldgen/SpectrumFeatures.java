package de.dafuqs.spectrum.worldgen;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.worldgen.features.WeightedRandomFeaturePatch;
import de.dafuqs.spectrum.worldgen.features.WeightedRandomFeaturePatchConfig;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.GeodeFeatureConfig;

public class SpectrumFeatures {

    public static Feature<GeodeFeatureConfig> SOLID_BLOCKS_GEODE;
    public static Feature<WeightedRandomFeaturePatchConfig> WEIGHTED_RANDOM_FEATURE_PATCH;

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
        return Registry.register(Registry.FEATURE, new Identifier(SpectrumCommon.MOD_ID, name), feature);
    }

    public static void register() {
        SOLID_BLOCKS_GEODE = register("solid_blocks_geode", new SolidBlocksOnlyGeodeFeature(GeodeFeatureConfig.CODEC));
        WEIGHTED_RANDOM_FEATURE_PATCH = register("weighted_random_feature_patch", new WeightedRandomFeaturePatch(WeightedRandomFeaturePatchConfig.CODEC));
    }

}
