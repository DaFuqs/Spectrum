package de.dafuqs.spectrum.deeper_down;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.worldgen.*;
import de.dafuqs.spectrum.worldgen.features.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.*;

import java.util.*;

public class DDConfiguredFeatures extends WorldgenHelper {

	public static final ConfiguredFeature<GilledFungusFeatureConfig, ?> CHESTNUT_NOXFUNGUS = new ConfiguredFeature<>(SpectrumFeatures.GILLED_FUNGUS,
			new GilledFungusFeatureConfig(SpectrumBlocks.SHIMMEL.getDefaultState(), SpectrumBlocks.CHESTNUT_NOXCAP_CAP.getDefaultState(), SpectrumBlocks.CHESTNUT_NOXCAP_GILLS.getDefaultState(), SpectrumBlocks.CHESTNUT_NOXCAP_STEM.getDefaultState()));
	public static final ConfiguredFeature<GilledFungusFeatureConfig, ?> EBONY_NOXFUNGUS = new ConfiguredFeature<>(SpectrumFeatures.GILLED_FUNGUS,
			new GilledFungusFeatureConfig(SpectrumBlocks.SHIMMEL.getDefaultState(), SpectrumBlocks.EBONY_NOXCAP_CAP.getDefaultState(), SpectrumBlocks.EBONY_NOXCAP_GILLS.getDefaultState(), SpectrumBlocks.EBONY_NOXCAP_STEM.getDefaultState()));
	public static final ConfiguredFeature<GilledFungusFeatureConfig, ?> IVORY_NOXFUNGUS = new ConfiguredFeature<>(SpectrumFeatures.GILLED_FUNGUS,
			new GilledFungusFeatureConfig(SpectrumBlocks.SHIMMEL.getDefaultState(), SpectrumBlocks.IVORY_NOXCAP_CAP.getDefaultState(), SpectrumBlocks.IVORY_NOXCAP_GILLS.getDefaultState(), SpectrumBlocks.IVORY_NOXCAP_STEM.getDefaultState()));
	public static final ConfiguredFeature<GilledFungusFeatureConfig, ?> SLATE_NOXFUNGUS = new ConfiguredFeature<>(SpectrumFeatures.GILLED_FUNGUS,
			new GilledFungusFeatureConfig(SpectrumBlocks.SHIMMEL.getDefaultState(), SpectrumBlocks.SLATE_NOXCAP_CAP.getDefaultState(), SpectrumBlocks.SLATE_NOXCAP_GILLS.getDefaultState(), SpectrumBlocks.SLATE_NOXCAP_STEM.getDefaultState()));

	public static final ConfiguredFeature<RandomPatchFeatureConfig, ?> RED_DRAGONJAGS = new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(SimpleBlockStateProvider.of(SpectrumBlocks.SMALL_RED_DRAGONJAG.getDefaultState()))));
	public static final ConfiguredFeature<RandomPatchFeatureConfig, ?> PINK_DRAGONJAGS = new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(SimpleBlockStateProvider.of(SpectrumBlocks.SMALL_PINK_DRAGONJAG.getDefaultState()))));
	public static final ConfiguredFeature<RandomPatchFeatureConfig, ?> GREEN_DRAGONJAGS = new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(SimpleBlockStateProvider.of(SpectrumBlocks.SMALL_GREEN_DRAGONJAG.getDefaultState()))));
	public static final ConfiguredFeature<RandomPatchFeatureConfig, ?> PURPLE_DRAGONJAGS = new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(SimpleBlockStateProvider.of(SpectrumBlocks.SMALL_PURPLE_DRAGONJAG.getDefaultState()))));
	public static final ConfiguredFeature<RandomPatchFeatureConfig, ?> BLACK_DRAGONJAGS = new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(SimpleBlockStateProvider.of(SpectrumBlocks.SMALL_BLACK_DRAGONJAG.getDefaultState()))));

	public static final ConfiguredFeature<RandomPatchFeatureConfig, ?> BRISTLE_SPROUTS = new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(SimpleBlockStateProvider.of(SpectrumBlocks.BRISTLE_SPROUTS.getDefaultState())), List.of(SpectrumBlocks.SAWBLADE_GRASS), 144));

}
