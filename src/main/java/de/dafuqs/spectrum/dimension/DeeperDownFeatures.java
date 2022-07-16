package de.dafuqs.spectrum.dimension;

import com.google.common.collect.ImmutableList;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.dimension.mod_integration.DeeperDownAE2Ores;
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
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class DeeperDownFeatures {
	
	public static final Identifier MOONSTONE_GEODE_IDENTIFIER = SpectrumCommon.locate("moonstone_geode");
	public static RegistryEntry<PlacedFeature> MOONSTONE_GEODE;
	
	public static void register() {
		registerSpectrumOres();
		registerVanillaOres();
		registerGeodes();
		
		if(DeeperDownAE2Ores.shouldRun()) {
			DeeperDownAE2Ores.register();
		}
	}
	
	// see: https://minecraft.fandom.com/wiki/Biome/JSON_format
	public static void registerAndAddOreFeature(String identifier, OreFeatureConfig oreFeatureConfig, HeightRangePlacementModifier heightRangePlacementModifier, CountPlacementModifier countPlacementModifier) {
		Identifier id = SpectrumCommon.locate(identifier);
		SpectrumConfiguredFeatures.registerConfiguredAndPlacedFeature(id, Feature.ORE, oreFeatureConfig, heightRangePlacementModifier, countPlacementModifier);
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(DeeperDownDimension.DEEPER_DOWN_BIOME_KEY), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, id));
	}
	
	public static void registerSpectrumOres() {
		registerAndAddOreFeature("dd_malachite_ore",
				new OreFeatureConfig(ImmutableList.of(
						OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, SpectrumBlocks.MALACHITE_ORE.getDefaultState()),
						OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, SpectrumBlocks.DEEPSLATE_MALACHITE_ORE.getDefaultState())
				), 7),
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(64), YOffset.belowTop(256)),
				CountPlacementModifier.of(80));
		
		registerAndAddOreFeature("dd_sparklestone_ore",
				new OreFeatureConfig(ImmutableList.of(
						OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, SpectrumBlocks.SPARKLESTONE_ORE.getDefaultState()),
						OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, SpectrumBlocks.DEEPSLATE_SPARKLESTONE_ORE.getDefaultState())
				), 7),
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(16), YOffset.belowTop(128)),
				CountPlacementModifier.of(15));
	}
	
	
	public static void registerVanillaOres() {
		registerAndAddOreFeature("dd_coal_ore",
				new OreFeatureConfig(ImmutableList.of(
						OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, Blocks.COAL_ORE.getDefaultState()),
						OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_COAL_ORE.getDefaultState())
				), 48),
				HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(64), YOffset.aboveBottom(320)),
				CountPlacementModifier.of(10));
		
		registerAndAddOreFeature("dd_iron_ore",
				new OreFeatureConfig(ImmutableList.of(
						OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, Blocks.IRON_ORE.getDefaultState()),
						OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_IRON_ORE.getDefaultState())
				), 25),
				HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(192), YOffset.aboveBottom(384)),
				CountPlacementModifier.of(30));
		
		registerAndAddOreFeature("dd_gold_ore",
				new OreFeatureConfig(ImmutableList.of(
						OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, Blocks.GOLD_ORE.getDefaultState()),
						OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_GOLD_ORE.getDefaultState())
				), 15),
				HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(128), YOffset.aboveBottom(256)),
				CountPlacementModifier.of(20));
		
		registerAndAddOreFeature("dd_lapis_ore",
				new OreFeatureConfig(ImmutableList.of(
						OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, Blocks.LAPIS_ORE.getDefaultState()),
						OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_LAPIS_ORE.getDefaultState())
				), 15),
				HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(64), YOffset.aboveBottom(192)),
				CountPlacementModifier.of(10));
		
		registerAndAddOreFeature("dd_diamond_ore",
				new OreFeatureConfig(ImmutableList.of(
						OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, Blocks.DIAMOND_ORE.getDefaultState()),
						OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_DIAMOND_ORE.getDefaultState())
				), 9),
				HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(0), YOffset.aboveBottom(128)),
				CountPlacementModifier.of(5));
		
		registerAndAddOreFeature("dd_emerald_ore",
				new OreFeatureConfig(ImmutableList.of(
						OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, Blocks.EMERALD_ORE.getDefaultState()),
						OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_EMERALD_ORE.getDefaultState())
				), 32),
				HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(0), YOffset.aboveBottom(128)),
				CountPlacementModifier.of(1));
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
	
	
}
