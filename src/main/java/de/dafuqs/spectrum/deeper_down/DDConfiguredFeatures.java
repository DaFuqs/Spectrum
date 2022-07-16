package de.dafuqs.spectrum.deeper_down;

import com.google.common.collect.ImmutableList;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import static net.minecraft.world.gen.feature.OreConfiguredFeatures.BASE_STONE_OVERWORLD;

public class DDConfiguredFeatures {
	
	public static final ConfiguredFeature<OreFeatureConfig, ?> BEDROCK_DISK = new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(BASE_STONE_OVERWORLD, Blocks.BEDROCK.getDefaultState(), 40));
	public static final ConfiguredFeature<OreFeatureConfig, ?> BEDROCK_DISK_SLOPED = new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(BASE_STONE_OVERWORLD, Blocks.BEDROCK.getDefaultState(), 40));
	public static final ConfiguredFeature<OreFeatureConfig, ?> STONE_DISK = new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(BASE_STONE_OVERWORLD, Blocks.STONE.getDefaultState(), 33));
	
	public static final ConfiguredFeature<GeodeFeatureConfig, ?> MOONSTONE_GEODE = new ConfiguredFeature<>(Feature.GEODE,
			new GeodeFeatureConfig(
					new GeodeLayerConfig(
							BlockStateProvider.of(Blocks.AIR.getDefaultState()),
							BlockStateProvider.of(SpectrumBlocks.MOONSTONE_BLOCK.getDefaultState()),
							BlockStateProvider.of(SpectrumBlocks.BUDDING_MOONSTONE.getDefaultState()),
							BlockStateProvider.of(Blocks.CALCITE.getDefaultState()),
							BlockStateProvider.of(Blocks.SMOOTH_BASALT.getDefaultState()),
							ImmutableList.of(SpectrumBlocks.MOONSTONE_CLUSTER.getDefaultState()), // forever untouched by man: generate with clusters only
							BlockTags.FEATURES_CANNOT_REPLACE,
							BlockTags.GEODE_INVALID_BLOCKS),
					new GeodeLayerThicknessConfig(4.0D, 5.0D, 6.0D, 7.0D),
					new GeodeCrackConfig(0.95D, 4.0D, 4),
					1.0D, 0.1D, true,
					UniformIntProvider.create(4, 6),
					UniformIntProvider.create(3, 4),
					UniformIntProvider.create(1, 2),
					-16, 16, 0.05D, 1));
	
}
