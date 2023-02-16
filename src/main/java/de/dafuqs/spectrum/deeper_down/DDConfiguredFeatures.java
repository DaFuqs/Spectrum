package de.dafuqs.spectrum.deeper_down;

import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.worldgen.*;
import de.dafuqs.spectrum.worldgen.features.*;
import net.minecraft.world.gen.feature.*;

public class DDConfiguredFeatures {

	public static final ConfiguredFeature<GilledFungusFeatureConfig, ?> CHESTNUT_NOXFUNGUS = new ConfiguredFeature<>(SpectrumFeatures.GILLED_FUNGUS,
			new GilledFungusFeatureConfig(SpectrumBlocks.SHIMMEL.getDefaultState(), SpectrumBlocks.CHESTNUT_NOXCAP_CAP.getDefaultState(), SpectrumBlocks.CHESTNUT_NOXCAP_GILLS.getDefaultState(), SpectrumBlocks.CHESTNUT_NOXCAP_STEM.getDefaultState()));
	public static final ConfiguredFeature<GilledFungusFeatureConfig, ?> EBONY_NOXFUNGUS = new ConfiguredFeature<>(SpectrumFeatures.GILLED_FUNGUS,
			new GilledFungusFeatureConfig(SpectrumBlocks.SHIMMEL.getDefaultState(), SpectrumBlocks.EBONY_NOXCAP_CAP.getDefaultState(), SpectrumBlocks.EBONY_NOXCAP_GILLS.getDefaultState(), SpectrumBlocks.EBONY_NOXCAP_STEM.getDefaultState()));
	public static final ConfiguredFeature<GilledFungusFeatureConfig, ?> IVORY_NOXFUNGUS = new ConfiguredFeature<>(SpectrumFeatures.GILLED_FUNGUS,
			new GilledFungusFeatureConfig(SpectrumBlocks.SHIMMEL.getDefaultState(), SpectrumBlocks.IVORY_NOXCAP_CAP.getDefaultState(), SpectrumBlocks.IVORY_NOXCAP_GILLS.getDefaultState(), SpectrumBlocks.IVORY_NOXCAP_STEM.getDefaultState()));
	public static final ConfiguredFeature<GilledFungusFeatureConfig, ?> SLATE_NOXFUNGUS = new ConfiguredFeature<>(SpectrumFeatures.GILLED_FUNGUS,
			new GilledFungusFeatureConfig(SpectrumBlocks.SHIMMEL.getDefaultState(), SpectrumBlocks.SLATE_NOXCAP_CAP.getDefaultState(), SpectrumBlocks.SLATE_NOXCAP_GILLS.getDefaultState(), SpectrumBlocks.SLATE_NOXCAP_STEM.getDefaultState()));

}
