package de.dafuqs.spectrum.deeper_down;

import com.google.common.collect.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.worldgen.*;
import de.dafuqs.spectrum.worldgen.features.*;
import net.minecraft.block.*;
import net.minecraft.tag.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.*;

public class DDConfiguredFeatures {

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
