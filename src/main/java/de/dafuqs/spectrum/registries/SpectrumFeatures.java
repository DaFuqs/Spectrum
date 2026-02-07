package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.worldgen.features.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.neoforged.bus.api.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumFeatures {
	
	private static final DeferredRegister<Feature<?>> REGISTRAR = DeferredRegister.create(Registries.FEATURE, SpectrumCommon.MOD_ID);
	
	public static DeferredHolder<Feature<?>, WeightedRandomFeature> WEIGHTED_RANDOM_FEATURE = registerFeature("weighted_random_feature", new WeightedRandomFeature(WeightedRandomFeatureConfig.CODEC));
	public static DeferredHolder<Feature<?>, SolidBlockCheckGeodeFeature> AIR_CHECK_GEODE = registerFeature("air_check_geode", new SolidBlockCheckGeodeFeature(GeodeConfiguration.CODEC));
	public static DeferredHolder<Feature<?>, RandomBudsFeature> RANDOM_BUDS = registerFeature("random_buds", new RandomBudsFeature(RandomBudsFeaturesConfig.CODEC));
	public static DeferredHolder<Feature<?>, AirCheckDiskFeature> AIR_CHECK_DISK = registerFeature("air_check_disk", new AirCheckDiskFeature(OreConfiguration.CODEC));
	public static DeferredHolder<Feature<?>, GilledFungusFeature> GILLED_FUNGUS = registerFeature("gilled_fungus", new GilledFungusFeature(GilledFungusFeatureConfig.CODEC));
	public static DeferredHolder<Feature<?>, GiantGilledFungusFeature> GIANT_GILLED_FUNGUS = registerFeature("giant_gilled_fungus", new GiantGilledFungusFeature(GilledFungusFeatureConfig.CODEC));
	public static DeferredHolder<Feature<?>, NephriteBlossomFeature> NEPHRITE_BLOSSOM = registerFeature("nephrite_blossom", new NephriteBlossomFeature(NephriteBlossomFeatureConfig.CODEC));
	public static DeferredHolder<Feature<?>, JadeiteLotusFeature> JADEITE_LOTUS = registerFeature("jadeite_lotus", new JadeiteLotusFeature(JadeiteLotusFeatureConfig.CODEC));
	public static DeferredHolder<Feature<?>, TriStateVineFeature> TRISTATE_VINE = registerFeature("tristate_vine", new TriStateVineFeature(TriStateVineFeatureConfig.CODEC));
	public static DeferredHolder<Feature<?>, PillarFeature> PILLAR = registerFeature("pillar", new PillarFeature(BlockStateFeatureConfig.CODEC));
	public static DeferredHolder<Feature<?>, ColumnsFeature> COLUMNS = registerFeature("columns", new ColumnsFeature(ColumnsFeatureConfig.CODEC));
	public static DeferredHolder<Feature<?>, CrystalFormationFeature> BLOB = registerFeature("crystal_formation", new CrystalFormationFeature(CrystalFormationFeatureFeatureConfig.CODEC));
	public static DeferredHolder<Feature<?>, RandomBlockProximityPatchFeature> RANDOM_BLOCK_PROXIMITY_PATCH = registerFeature("random_block_proximity_patch", new RandomBlockProximityPatchFeature(RandomBlockProximityPatchFeatureConfig.CODEC));
	public static DeferredHolder<Feature<?>, ExposedFossilFeature> EXPOSED_FOSSIL = registerFeature("exposed_fossil", new ExposedFossilFeature(FossilFeatureConfiguration.CODEC));
	public static DeferredHolder<Feature<?>, WallPatchFeature> WALL_PATCH = registerFeature("wall_patch", new WallPatchFeature(WallPatchFeatureConfig.CODEC));
	public static DeferredHolder<Feature<?>, AshDunesFeature> ASH_DUNES = registerFeature("ash_dunes", new AshDunesFeature(AshDunesFeatureConfig.CODEC));
	
	private static <C extends FeatureConfiguration, F extends Feature<C>> DeferredHolder<Feature<?>, F> registerFeature(String name, F feature) {
		return REGISTRAR.register(name, () -> feature);
	}
	
	public static void register(IEventBus modBus) {
		REGISTRAR.register(modBus);
	}
	
}
