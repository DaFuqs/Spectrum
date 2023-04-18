package de.dafuqs.spectrum.deeper_down;

import de.dafuqs.spectrum.blocks.dd_deco.*;
import de.dafuqs.spectrum.features.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.tag.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.util.registry.*;
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
	public static final ConfiguredFeature<RandomPatchFeatureConfig, ?> YELLOW_DRAGONJAGS = new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(SimpleBlockStateProvider.of(SpectrumBlocks.SMALL_YELLOW_DRAGONJAG.getDefaultState()))));
	public static final ConfiguredFeature<RandomPatchFeatureConfig, ?> PURPLE_DRAGONJAGS = new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(SimpleBlockStateProvider.of(SpectrumBlocks.SMALL_PURPLE_DRAGONJAG.getDefaultState()))));
	public static final ConfiguredFeature<RandomPatchFeatureConfig, ?> BLACK_DRAGONJAGS = new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(SimpleBlockStateProvider.of(SpectrumBlocks.SMALL_BLACK_DRAGONJAG.getDefaultState()))));
	
	public static final ConfiguredFeature<RandomPatchFeatureConfig, ?> BRISTLE_SPROUTS = new ConfiguredFeature<>(Feature.RANDOM_PATCH,
			ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(SimpleBlockStateProvider.of(SpectrumBlocks.BRISTLE_SPROUTS.getDefaultState())), List.of(SpectrumBlocks.SAWBLADE_GRASS), 144));
	
	public static final RegistryEntry<ConfiguredFeature<SimpleBlockFeatureConfig, ?>> SNAPPING_IVY = ConfiguredFeatures.register("snapping_ivy", Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(
			new WeightedBlockStateProvider(DataPool.<BlockState>builder()
					.add(SpectrumBlocks.SNAPPING_IVY.getDefaultState().with(SnappingIvyBlock.AXIS, Direction.Axis.X), 1)
					.add(SpectrumBlocks.SNAPPING_IVY.getDefaultState().with(SnappingIvyBlock.AXIS, Direction.Axis.Z), 1).build())
	));
	
	public static final ConfiguredFeature<VegetationPatchFeatureConfig, ?> SNAPPING_IVY_PATCH = new ConfiguredFeature<>(Feature.VEGETATION_PATCH,
			new VegetationPatchFeatureConfig(BlockTags.MOSS_REPLACEABLE, BlockStateProvider.of(SpectrumBlocks.ROTTEN_GROUND),
					PlacedFeatures.createEntry(SNAPPING_IVY), VerticalSurfaceType.FLOOR, ConstantIntProvider.create(1), 0.0F, 5, 0.8F, UniformIntProvider.create(3, 5), 0.3F));
	
	// Deeper Down
	public static ConfiguredFeature<NephriteBlossomFeatureConfig, ?> NEPHRITE_BLOSSOM_BULB = new ConfiguredFeature<>(SpectrumFeatures.NEPHRITE_BLOSSOM, new NephriteBlossomFeatureConfig(false));
	public static ConfiguredFeature<JadeiteLotusFeatureConfig, ?> JADEITE_LOTUS_BULB = new ConfiguredFeature<>(SpectrumFeatures.JADEITE_LOTUS, new JadeiteLotusFeatureConfig(false));
	
}
