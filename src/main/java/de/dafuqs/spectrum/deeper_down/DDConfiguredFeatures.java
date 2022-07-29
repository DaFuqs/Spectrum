package de.dafuqs.spectrum.deeper_down;

import com.google.common.collect.ImmutableList;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.worldgen.SpectrumFeatures;
import de.dafuqs.spectrum.worldgen.features.RandomBudsFeaturesConfig;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.structure.rule.AlwaysTrueRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.List;

public class DDConfiguredFeatures {
	
	public static final RuleTest BASE_STONE_DD = new TagMatchRuleTest(SpectrumBlockTags.BASE_STONE_DEEPER_DOWN);
	public static final AlwaysTrueRuleTest ALWAYS_TRUE = AlwaysTrueRuleTest.INSTANCE;
	
	public static final RegistryEntryList<Block> DD_BASE_BLOCKS = RegistryEntryList.of(Block::getRegistryEntry, Blocks.STONE, Blocks.DEEPSLATE, SpectrumBlocks.BLACKSLAG, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE);
	
	public static final ConfiguredFeature<OreFeatureConfig, ?> STONE_DISK = new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(BASE_STONE_DD, Blocks.STONE.getDefaultState(), 33));
	public static final ConfiguredFeature<OreFeatureConfig, ?> TUFF_DISK = new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(BASE_STONE_DD, Blocks.TUFF.getDefaultState(), 64));
	public static final ConfiguredFeature<OreFeatureConfig, ?> GRAVEL_DISK = new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(BASE_STONE_DD, Blocks.GRAVEL.getDefaultState(), 33));
	public static final ConfiguredFeature<OreFeatureConfig, ?> GRANITE_DISK = new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(BASE_STONE_DD, Blocks.GRANITE.getDefaultState(), 64));
	public static final ConfiguredFeature<OreFeatureConfig, ?> DIORITE_DISK = new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(BASE_STONE_DD, Blocks.DIORITE.getDefaultState(), 64));
	public static final ConfiguredFeature<OreFeatureConfig, ?> ANDESITE_DISK = new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(BASE_STONE_DD, Blocks.ANDESITE.getDefaultState(), 64));
	public static final ConfiguredFeature<OreFeatureConfig, ?> BEDROCK_DISK = new ConfiguredFeature<>(SpectrumFeatures.AIR_CHECK_DISK, new OreFeatureConfig(ALWAYS_TRUE, Blocks.BEDROCK.getDefaultState(), 40));
	public static final ConfiguredFeature<OreFeatureConfig, ?> BEDROCK_DISK_SLOPED = new ConfiguredFeature<>(SpectrumFeatures.AIR_CHECK_DISK, new OreFeatureConfig(ALWAYS_TRUE, Blocks.BEDROCK.getDefaultState(), 40));
	
	public static final ConfiguredFeature<SpringFeatureConfig, ?> WATER_SPRING = new ConfiguredFeature<>(Feature.SPRING_FEATURE, new SpringFeatureConfig(Fluids.WATER.getDefaultState(), true, 4, 1, DD_BASE_BLOCKS));
	public static final ConfiguredFeature<GlowLichenFeatureConfig, ?> GLOW_LICHEN = new ConfiguredFeature<>(Feature.GLOW_LICHEN, new GlowLichenFeatureConfig(20, false, true, true, 0.5F, DD_BASE_BLOCKS));
	
	
	public static final ConfiguredFeature<RandomBudsFeaturesConfig, ?> BISMUTH_BUDS = new ConfiguredFeature<>(SpectrumFeatures.RANDOM_BUDS,
			new RandomBudsFeaturesConfig(20, false, true, true, DD_BASE_BLOCKS,
					List.of(SpectrumBlocks.SMALL_BISMUTH_BUD.getDefaultState(), SpectrumBlocks.LARGE_BISMUTH_BUD.getDefaultState())));

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
					UniformIntProvider.create(2, 3),
					UniformIntProvider.create(1, 2),
					-16, 16, 0.05D, 1));
	
}
