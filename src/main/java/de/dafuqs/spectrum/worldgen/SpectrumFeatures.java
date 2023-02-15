package de.dafuqs.spectrum.worldgen;

import de.dafuqs.spectrum.worldgen.features.*;
import net.minecraft.world.gen.feature.*;

import static de.dafuqs.spectrum.helpers.WorldgenHelper.*;

public class SpectrumFeatures {

	public static Feature<WeightedRandomFeatureConfig> WEIGHTED_RANDOM_FEATURE;
	public static Feature<WeightedRandomFeaturePatchConfig> WEIGHTED_RANDOM_FEATURE_PATCH;
	public static Feature<GeodeFeatureConfig> AIR_CHECK_GEODE;
	public static Feature<RandomBudsFeaturesConfig> RANDOM_BUDS;
	public static Feature<OreFeatureConfig> AIR_CHECK_DISK;
	public static Feature<GilledFungusFeatureConfig> GILLED_FUNGUS;

	public static void register() {
		WEIGHTED_RANDOM_FEATURE = registerFeature("weighted_random_feature", new WeightedRandomFeature(WeightedRandomFeatureConfig.CODEC));
		WEIGHTED_RANDOM_FEATURE_PATCH = registerFeature("weighted_random_feature_patch", new WeightedRandomFeaturePatch(WeightedRandomFeaturePatchConfig.CODEC));
		AIR_CHECK_GEODE = registerFeature("air_check_geode_feature", new SolidBlockCheckGeodeFeature(GeodeFeatureConfig.CODEC));
		RANDOM_BUDS = registerFeature("random_buds", new RandomBudsFeature(RandomBudsFeaturesConfig.CODEC));
		AIR_CHECK_DISK = registerFeature("air_check_disk", new AirCheckDiskFeature(OreFeatureConfig.CODEC));
		GILLED_FUNGUS = registerFeature("gilled_fungus", new GilledFungusFeature(GilledFungusFeatureConfig.CODEC));
	}
	
}
