package de.dafuqs.spectrum.deeper_down;

import com.google.common.collect.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.worldgen.*;
import de.dafuqs.spectrum.worldgen.features.*;
import net.minecraft.block.*;
import net.minecraft.fluid.*;
import net.minecraft.structure.rule.*;
import net.minecraft.tag.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.*;

import java.util.*;

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
	public static final ConfiguredFeature<OreFeatureConfig, ?> DOWNSTONE_DISK = new ConfiguredFeature<>(SpectrumFeatures.AIR_CHECK_DISK, new OreFeatureConfig(ALWAYS_TRUE, SpectrumBlocks.DOWNSTONE.getDefaultState(), 40));
	public static final ConfiguredFeature<OreFeatureConfig, ?> DOWNSTONE_DISK_SLOPED = new ConfiguredFeature<>(SpectrumFeatures.AIR_CHECK_DISK, new OreFeatureConfig(ALWAYS_TRUE, SpectrumBlocks.DOWNSTONE.getDefaultState(), 40));
	
	public static final ConfiguredFeature<SpringFeatureConfig, ?> WATER_SPRING = new ConfiguredFeature<>(Feature.SPRING_FEATURE, new SpringFeatureConfig(Fluids.WATER.getDefaultState(), true, 3, 1, DD_BASE_BLOCKS));
	public static final ConfiguredFeature<MultifaceGrowthFeatureConfig, ?> GLOW_LICHEN = new ConfiguredFeature<>(Feature.MULTIFACE_GROWTH, new MultifaceGrowthFeatureConfig((GlowLichenBlock) Blocks.GLOW_LICHEN, 20, false, true, true, 0.5F, DD_BASE_BLOCKS));
	
	
	public static final ConfiguredFeature<RandomBudsFeaturesConfig, ?> BISMUTH_BUDS = new ConfiguredFeature<>(SpectrumFeatures.RANDOM_BUDS,
			new RandomBudsFeaturesConfig(12, false, true, true, DD_BASE_BLOCKS,
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
					new GeodeLayerThicknessConfig(5.0D, 6.0D, 7.0D, 8.0D),
					new GeodeCrackConfig(0.15D, 4.0D, 4),
					1.0D, 0.1D, true,
					UniformIntProvider.create(8, 10),
					UniformIntProvider.create(2, 3),
					UniformIntProvider.create(1, 2),
					-16, 16, 0.05D, 1));

	public static final ConfiguredFeature<GilledFungusFeatureConfig, ?> CHESTNUT_NOXFUNGUS = new ConfiguredFeature<>(SpectrumFeatures.GILLED_FUNGUS,
			new GilledFungusFeatureConfig(SpectrumBlocks.SHIMMEL.getDefaultState(), SpectrumBlocks.CHESTNUT_NOXCAP_CAP.getDefaultState(), SpectrumBlocks.CHESTNUT_NOXCAP_GILLS.getDefaultState(), SpectrumBlocks.CHESTNUT_NOXCAP_STEM.getDefaultState()));
	public static final ConfiguredFeature<GilledFungusFeatureConfig, ?> EBONY_NOXFUNGUS = new ConfiguredFeature<>(SpectrumFeatures.GILLED_FUNGUS,
			new GilledFungusFeatureConfig(SpectrumBlocks.SHIMMEL.getDefaultState(), SpectrumBlocks.EBONY_NOXCAP_CAP.getDefaultState(), SpectrumBlocks.EBONY_NOXCAP_GILLS.getDefaultState(), SpectrumBlocks.EBONY_NOXCAP_STEM.getDefaultState()));
	public static final ConfiguredFeature<GilledFungusFeatureConfig, ?> IVORY_NOXFUNGUS = new ConfiguredFeature<>(SpectrumFeatures.GILLED_FUNGUS,
			new GilledFungusFeatureConfig(SpectrumBlocks.SHIMMEL.getDefaultState(), SpectrumBlocks.IVORY_NOXCAP_CAP.getDefaultState(), SpectrumBlocks.IVORY_NOXCAP_GILLS.getDefaultState(), SpectrumBlocks.IVORY_NOXCAP_STEM.getDefaultState()));
	public static final ConfiguredFeature<GilledFungusFeatureConfig, ?> SLATE_NOXFUNGUS = new ConfiguredFeature<>(SpectrumFeatures.GILLED_FUNGUS,
			new GilledFungusFeatureConfig(SpectrumBlocks.SHIMMEL.getDefaultState(), SpectrumBlocks.SLATE_NOXCAP_CAP.getDefaultState(), SpectrumBlocks.SLATE_NOXCAP_GILLS.getDefaultState(), SpectrumBlocks.SLATE_NOXCAP_STEM.getDefaultState()));

}
