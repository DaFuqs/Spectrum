package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.worldgen.features.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumFeatures {
	
	private static final DeferredRegister<Feature<?>> REGISTRAR = DeferredRegister.create(Registries.FEATURE, SpectrumCommon.MOD_ID);
	
	public static Feature<WeightedRandomFeatureConfig> WEIGHTED_RANDOM_FEATURE = new WeightedRandomFeature(WeightedRandomFeatureConfig.CODEC);
	public static Feature<GeodeConfiguration> AIR_CHECK_GEODE = new SolidBlockCheckGeodeFeature(GeodeConfiguration.CODEC);
	public static Feature<RandomBudsFeaturesConfig> RANDOM_BUDS = new RandomBudsFeature(RandomBudsFeaturesConfig.CODEC);
	public static Feature<OreConfiguration> AIR_CHECK_DISK = new AirCheckDiskFeature(OreConfiguration.CODEC);
	public static Feature<GilledFungusFeatureConfig> GILLED_FUNGUS = new GilledFungusFeature(GilledFungusFeatureConfig.CODEC);
	public static Feature<GilledFungusFeatureConfig> GIANT_GILLED_FUNGUS = new GiantGilledFungusFeature(GilledFungusFeatureConfig.CODEC);
	public static Feature<NephriteBlossomFeatureConfig> NEPHRITE_BLOSSOM = new NephriteBlossomFeature(NephriteBlossomFeatureConfig.CODEC);
	public static Feature<JadeiteLotusFeatureConfig> JADEITE_LOTUS = new JadeiteLotusFeature(JadeiteLotusFeatureConfig.CODEC);
	public static Feature<TriStateVineFeatureConfig> TRISTATE_VINE = new TriStateVineFeature(TriStateVineFeatureConfig.CODEC);
	public static Feature<BlockStateFeatureConfig> PILLAR = new PillarFeature(BlockStateFeatureConfig.CODEC);
	public static Feature<ColumnsFeatureConfig> COLUMNS = new ColumnsFeature(ColumnsFeatureConfig.CODEC);
	public static Feature<CrystalFormationFeatureFeatureConfig> BLOB = new CrystalFormationFeature(CrystalFormationFeatureFeatureConfig.CODEC);
	public static Feature<RandomBlockProximityPatchFeatureConfig> RANDOM_BLOCK_PROXIMITY_PATCH = new RandomBlockProximityPatchFeature(RandomBlockProximityPatchFeatureConfig.CODEC);
	public static Feature<FossilFeatureConfiguration> EXPOSED_FOSSIL = new ExposedFossilFeature(FossilFeatureConfiguration.CODEC);
	public static Feature<WallPatchFeatureConfig> WALL_PATCH = new WallPatchFeature(WallPatchFeatureConfig.CODEC);
	public static Feature<AshDunesFeatureConfig> ASH_DUNES = new AshDunesFeature(AshDunesFeatureConfig.CODEC);
	
	private static <C extends FeatureConfiguration, F extends Feature<C>> F registerFeature(String name, F feature) {
		return Registry.register(BuiltInRegistries.FEATURE, SpectrumCommon.locate(name), feature);
	}
	
	public static void register(IEventBus modBus) {
		registerFeature("weighted_random_feature", WEIGHTED_RANDOM_FEATURE);
		registerFeature("air_check_geode", AIR_CHECK_GEODE);
		registerFeature("random_buds", RANDOM_BUDS);
		registerFeature("air_check_disk", AIR_CHECK_DISK);
		registerFeature("gilled_fungus", GILLED_FUNGUS);
		registerFeature("giant_gilled_fungus", GIANT_GILLED_FUNGUS);
		registerFeature("nephrite_blossom", NEPHRITE_BLOSSOM);
		registerFeature("jadeite_lotus", JADEITE_LOTUS);
		registerFeature("tristate_vine", TRISTATE_VINE);
		registerFeature("pillar", PILLAR);
		registerFeature("columns", COLUMNS);
		registerFeature("crystal_formation", BLOB);
		registerFeature("random_block_proximity_patch", RANDOM_BLOCK_PROXIMITY_PATCH);
		registerFeature("exposed_fossil", EXPOSED_FOSSIL);
		registerFeature("wall_patch", WALL_PATCH);
		registerFeature("ash_dunes", ASH_DUNES);
		
		REGISTRAR.register(modBus);
	}
	
}
