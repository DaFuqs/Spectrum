package de.dafuqs.spectrum.worldgen;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.worldgen.features.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.GeodeFeatureConfig;

public class SpectrumFeatures {
	
	public static Feature<WeightedRandomFeatureConfig> WEIGHTED_RANDOM_FEATURE;
	public static Feature<WeightedRandomFeaturePatchConfig> WEIGHTED_RANDOM_FEATURE_PATCH;
	public static Feature<GeodeFeatureConfig> AIR_CHECK_GEODE;
	
	private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
		return Registry.register(Registry.FEATURE, new Identifier(SpectrumCommon.MOD_ID, name), feature);
	}
	
	public static void register() {
		WEIGHTED_RANDOM_FEATURE = register("weighted_random_feature", new WeightedRandomFeature(WeightedRandomFeatureConfig.CODEC));
		WEIGHTED_RANDOM_FEATURE_PATCH = register("weighted_random_feature_patch", new WeightedRandomFeaturePatch(WeightedRandomFeaturePatchConfig.CODEC));
		AIR_CHECK_GEODE = register("air_check_geode_feature", new SolidBlockCheckGeodeFeature(GeodeFeatureConfig.CODEC));
	}
	
}
