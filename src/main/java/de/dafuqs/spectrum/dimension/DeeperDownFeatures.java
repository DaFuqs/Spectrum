package de.dafuqs.spectrum.dimension;

import com.google.common.collect.ImmutableList;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.worldgen.SpectrumConfiguredFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.heightprovider.VeryBiasedToBottomHeightProvider;
import net.minecraft.world.gen.placementmodifier.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import static net.minecraft.world.gen.feature.OreConfiguredFeatures.*;

public class DeeperDownFeatures {
	
	public static final Identifier MOONSTONE_GEODE_IDENTIFIER = SpectrumCommon.locate("moonstone_geode");
	public static RegistryEntry<PlacedFeature> MOONSTONE_GEODE;
	
	public static void register() {
		registerSpectrumOres();
		registerVanillaOres();
		registerGeodes();
		registerDecorators();
	}
	
	// see: https://minecraft.fandom.com/wiki/Biome/JSON_format
	public static void registerAndAddOreFeature(String identifier, OreFeatureConfig oreFeatureConfig,  PlacementModifier... placementModifiers) {
		Identifier id = SpectrumCommon.locate(identifier);
		SpectrumConfiguredFeatures.registerConfiguredAndPlacedFeature(id, Feature.ORE, oreFeatureConfig, placementModifiers);
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(DeeperDownDimension.DEEPER_DOWN_BIOME_KEY), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, id));
	}
	
	public static void registerSpectrumOres() {
		registerAndAddOreFeature("dd_malachite_ore",
				new OreFeatureConfig(ImmutableList.of(
						OreFeatureConfig.createTarget(STONE_ORE_REPLACEABLES, SpectrumBlocks.MALACHITE_ORE.getDefaultState()),
						OreFeatureConfig.createTarget(DEEPSLATE_ORE_REPLACEABLES, SpectrumBlocks.DEEPSLATE_MALACHITE_ORE.getDefaultState())
				), 7),
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(64), YOffset.belowTop(256)),
				CountPlacementModifier.of(40));
		
		registerAndAddOreFeature("dd_sparklestone_ore",
				new OreFeatureConfig(ImmutableList.of(
						OreFeatureConfig.createTarget(STONE_ORE_REPLACEABLES, SpectrumBlocks.SPARKLESTONE_ORE.getDefaultState()),
						OreFeatureConfig.createTarget(DEEPSLATE_ORE_REPLACEABLES, SpectrumBlocks.DEEPSLATE_SPARKLESTONE_ORE.getDefaultState())
				), 7),
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(16), YOffset.belowTop(128)),
				CountPlacementModifier.of(24));
	}
	
	
	public static void registerVanillaOres() {
		registerAndAddOreFeature("dd_iron_ore",
				new OreFeatureConfig(ImmutableList.of(
						OreFeatureConfig.createTarget(STONE_ORE_REPLACEABLES, Blocks.IRON_ORE.getDefaultState()),
						OreFeatureConfig.createTarget(DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_IRON_ORE.getDefaultState())
				), 25),
				HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(192), YOffset.aboveBottom(384)),
				CountPlacementModifier.of(30));
		
		registerAndAddOreFeature("dd_gold_ore",
				new OreFeatureConfig(ImmutableList.of(
						OreFeatureConfig.createTarget(STONE_ORE_REPLACEABLES, Blocks.GOLD_ORE.getDefaultState()),
						OreFeatureConfig.createTarget(DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_GOLD_ORE.getDefaultState())
				), 15),
				HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(128), YOffset.aboveBottom(256)),
				CountPlacementModifier.of(20));
		
		registerAndAddOreFeature("dd_diamond_ore",
				new OreFeatureConfig(ImmutableList.of(
						OreFeatureConfig.createTarget(STONE_ORE_REPLACEABLES, Blocks.DIAMOND_ORE.getDefaultState()),
						OreFeatureConfig.createTarget(DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_DIAMOND_ORE.getDefaultState())
				), 9),
				HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(0), YOffset.aboveBottom(256)),
				CountPlacementModifier.of(5));
		
		registerAndAddOreFeature("dd_emerald_ore",
				new OreFeatureConfig(ImmutableList.of(
						OreFeatureConfig.createTarget(STONE_ORE_REPLACEABLES, Blocks.EMERALD_ORE.getDefaultState()),
						OreFeatureConfig.createTarget(DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_EMERALD_ORE.getDefaultState())
				), 32),
				HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(0), YOffset.aboveBottom(128)),
				RarityFilterPlacementModifier.of(3));
	}
	
	public static void registerGeodes() {
		MOONSTONE_GEODE = SpectrumConfiguredFeatures.registerConfiguredAndPlacedFeature(
				MOONSTONE_GEODE_IDENTIFIER,
				Feature.GEODE,
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
						-16, 16, 0.05D, 1),
				RarityFilterPlacementModifier.of(SpectrumCommon.CONFIG.MoonstoneGeodeChunkChance),
				SquarePlacementModifier.of(),
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(16), YOffset.aboveBottom(128)),
				BiomePlacementModifier.of()
		);
		
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(DeeperDownDimension.DEEPER_DOWN_BIOME_KEY), GenerationStep.Feature.LOCAL_MODIFICATIONS, RegistryKey.of(Registry.PLACED_FEATURE_KEY, MOONSTONE_GEODE_IDENTIFIER));
	}
	
	public static void registerDecorators() {
		Identifier id = SpectrumCommon.locate("dd_tuff");
		SpectrumConfiguredFeatures.registerPlacedFeature(id, ORE_TUFF, HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.getTop()), CountPlacementModifier.of(32));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(DeeperDownDimension.DEEPER_DOWN_BIOME_KEY), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, id));
		
		id = SpectrumCommon.locate("dd_gravel");
		SpectrumConfiguredFeatures.registerPlacedFeature(id, ORE_GRAVEL, HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.getTop()), CountPlacementModifier.of(32));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(DeeperDownDimension.DEEPER_DOWN_BIOME_KEY), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, id));
		
		id = SpectrumCommon.locate("dd_infested");
		SpectrumConfiguredFeatures.registerPlacedFeature(id, ORE_INFESTED, HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.getTop()), CountPlacementModifier.of(32));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(DeeperDownDimension.DEEPER_DOWN_BIOME_KEY), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, id));
		
		id = SpectrumCommon.locate("dd_granite");
		SpectrumConfiguredFeatures.registerPlacedFeature(id, ORE_GRANITE, HeightRangePlacementModifier.uniform(YOffset.belowTop(192), YOffset.belowTop(64)), CountPlacementModifier.of(8));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(DeeperDownDimension.DEEPER_DOWN_BIOME_KEY), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, id));
		
		id = SpectrumCommon.locate("dd_diorite");
		SpectrumConfiguredFeatures.registerPlacedFeature(id, ORE_DIORITE, HeightRangePlacementModifier.uniform(YOffset.belowTop(128), YOffset.getTop()), CountPlacementModifier.of(8));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(DeeperDownDimension.DEEPER_DOWN_BIOME_KEY), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, id));
		
		id = SpectrumCommon.locate("dd_bedrock");
		SpectrumConfiguredFeatures.registerConfiguredAndPlacedFeature(id, Feature.ORE, new OreFeatureConfig(BASE_STONE_OVERWORLD, Blocks.BEDROCK.getDefaultState(), 40), HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.aboveBottom(128)), CountPlacementModifier.of(16));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(DeeperDownDimension.DEEPER_DOWN_BIOME_KEY), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, id));
		
		id = SpectrumCommon.locate("dd_bedrock_sloped");
		SpectrumConfiguredFeatures.registerConfiguredAndPlacedFeature(id, Feature.ORE, new OreFeatureConfig(BASE_STONE_OVERWORLD, Blocks.BEDROCK.getDefaultState(), 40), HeightRangePlacementModifier.of(VeryBiasedToBottomHeightProvider.create(YOffset.getBottom(), YOffset.aboveBottom(50), 24)), CountPlacementModifier.of(16));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(DeeperDownDimension.DEEPER_DOWN_BIOME_KEY), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, id));
		
		id = SpectrumCommon.locate("dd_stone");
		SpectrumConfiguredFeatures.registerConfiguredAndPlacedFeature(id, Feature.ORE, new OreFeatureConfig(BASE_STONE_OVERWORLD, Blocks.STONE.getDefaultState(), 33), HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.getTop()), CountPlacementModifier.of(32));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(DeeperDownDimension.DEEPER_DOWN_BIOME_KEY), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, id));
		
		id = SpectrumCommon.locate("dd_springs");
		SpectrumConfiguredFeatures.registerPlacedFeature(id, MiscConfiguredFeatures.SPRING_WATER, HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.getTop()), CountPlacementModifier.of(256));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(DeeperDownDimension.DEEPER_DOWN_BIOME_KEY), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, id));
		
		id = SpectrumCommon.locate("dd_lichen");
		SpectrumConfiguredFeatures.registerPlacedFeature(id, UndergroundConfiguredFeatures.GLOW_LICHEN, HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.getTop()), CountPlacementModifier.of(UniformIntProvider.create(104, 157)), SquarePlacementModifier.of(), SurfaceThresholdFilterPlacementModifier.of(Heightmap.Type.OCEAN_FLOOR_WG, -2147483648, -13));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(DeeperDownDimension.DEEPER_DOWN_BIOME_KEY), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, id));
	}
	
	
}
