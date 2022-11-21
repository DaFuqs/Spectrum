package de.dafuqs.spectrum.worldgen;

import com.google.common.collect.ImmutableList;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.conditional.ColoredLeavesBlock;
import de.dafuqs.spectrum.blocks.conditional.ColoredLogBlock;
import de.dafuqs.spectrum.blocks.conditional.MermaidsBrushBlock;
import de.dafuqs.spectrum.registries.SpectrumBiomeTags;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.worldgen.features.WeightedRandomFeatureConfig;
import de.dafuqs.spectrum.worldgen.features.WeightedRandomFeaturePatchConfig;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.tag.BiomeTags;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.BiasedToBottomIntProvider;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.placementmodifier.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.RandomizedIntBlockStateProvider;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static de.dafuqs.spectrum.helpers.WorldgenHelper.*;

public class SpectrumConfiguredFeatures {
	
	// Overworld
	public static RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> CLOVER_PATCH; // for bonemealing
	public static HashMap<DyeColor, RegistryEntry<? extends ConfiguredFeature<?, ?>>> COLORED_TREE_CONFIGURED_FEATURES = new HashMap<>(); // for sapling growing
	public static RegistryEntry<PlacedFeature> RANDOM_COLORED_TREES_FEATURE; // for worldgen placing
	
	public static final Identifier CITRINE_GEODE_IDENTIFIER = SpectrumCommon.locate("citrine_geode");
	public static final Identifier TOPAZ_GEODE_IDENTIFIER = SpectrumCommon.locate("topaz_geode");
	public static RegistryEntry<PlacedFeature> TOPAZ_GEODE;
	public static RegistryEntry<PlacedFeature> CITRINE_GEODE;
	
	// Deeper Down
	
	public static void register() {
		registerGeodes();
		registerOres();
		registerColoredTrees();
		registerPlants();
	}
	
	private static void registerOres() {
		BlockState sparklestoneOre = SpectrumBlocks.SPARKLESTONE_ORE.getDefaultState();
		BlockState deepslateSparklestoneOre = SpectrumBlocks.DEEPSLATE_SPARKLESTONE_ORE.getDefaultState();
		BlockState azuriteOre = SpectrumBlocks.AZURITE_ORE.getDefaultState();
		BlockState deepslateAzuriteOre = SpectrumBlocks.DEEPSLATE_AZURITE_ORE.getDefaultState();
		BlockState scarletOre = SpectrumBlocks.SCARLET_ORE.getDefaultState();
		BlockState paleturOre = SpectrumBlocks.PALETUR_ORE.getDefaultState();
		
		Identifier sparklestoneOreIdentifier = SpectrumCommon.locate("sparklestone_ore");
		Identifier azuriteOreIdentifier = SpectrumCommon.locate("azurite_ore");
		Identifier scarletOreIdentifier = SpectrumCommon.locate("scarlet_ore");
		Identifier paleturOreIdentifier = SpectrumCommon.locate("paletur_ore");
		
		ImmutableList<OreFeatureConfig.Target> sparklestoneOreTargets = ImmutableList.of(OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, sparklestoneOre), OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateSparklestoneOre));
		ImmutableList<OreFeatureConfig.Target> azuriteOreTargets = ImmutableList.of(OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, azuriteOre), OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateAzuriteOre));
		
		registerConfiguredAndPlacedFeature(
				sparklestoneOreIdentifier,
				Feature.ORE,
				new OreFeatureConfig(sparklestoneOreTargets, 8),
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(48), YOffset.fixed(128)), // min and max height
				CountPlacementModifier.of(9), // number of veins per chunk
				SquarePlacementModifier.of() // spread through the chunk
		);
		
		registerConfiguredAndPlacedFeature(
				azuriteOreIdentifier,
				Feature.ORE,
				new OreFeatureConfig(azuriteOreTargets, 5, 0.5F),
				HeightRangePlacementModifier.trapezoid(YOffset.getBottom(), YOffset.aboveBottom(32)), // min and max height
				CountPlacementModifier.of(6), // number of veins per chunk
				SquarePlacementModifier.of() // spread through the chunk
		);
		
		registerConfiguredAndPlacedFeature(
				scarletOreIdentifier,
				Feature.ORE,
				new OreFeatureConfig(OreConfiguredFeatures.BASE_STONE_NETHER, scarletOre, 6),
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(10), YOffset.belowTop(64)), // min and max height
				CountPlacementModifier.of(18), // number of veins per chunk
				SquarePlacementModifier.of() // spread through the chunk
		);
		
		registerConfiguredAndPlacedFeature(
				paleturOreIdentifier,
				Feature.ORE,
				new OreFeatureConfig(Rules.END_STONE, paleturOre, 4, 0.3F),
				HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.getTop()), // min and max height
				CountPlacementModifier.of(16), // number of veins per chunk
				SquarePlacementModifier.of() // spread through the chunk
		);
		
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_OVERWORLD), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, sparklestoneOreIdentifier));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_OVERWORLD), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, azuriteOreIdentifier));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_NETHER), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, scarletOreIdentifier));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_THE_END), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, paleturOreIdentifier));
	}
	
	private static void registerColoredTree(@NotNull DyeColor dyeColor) {
		String identifierString = dyeColor + "_tree";
		Identifier identifier = SpectrumCommon.locate(identifierString);
		
		TreeFeatureConfig treeFeatureConfig = new TreeFeatureConfig.Builder(
				BlockStateProvider.of(ColoredLogBlock.byColor(dyeColor).getDefaultState()),
				new StraightTrunkPlacer(4, 2, 2), // 4-8 height
				BlockStateProvider.of(ColoredLeavesBlock.byColor(dyeColor).getDefaultState()),
				new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
				new TwoLayersFeatureSize(1, 0, 1)
		).ignoreVines().build();
		
		COLORED_TREE_CONFIGURED_FEATURES.put(dyeColor, registerConfiguredFeature(identifier, Feature.TREE, treeFeatureConfig));
	}
	
	private static void registerColoredTrees() {
		
		for (DyeColor dyeColor : DyeColor.values()) {
			registerColoredTree(dyeColor);
		}
		
		// Black/White and brown variants are not found in the wild and have to be created by the player
		List<RegistryEntry> treeList = new ArrayList<>();
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.BLUE));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.CYAN));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.GREEN));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.LIGHT_BLUE));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.LIME));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.MAGENTA));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.ORANGE));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.PINK));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.PURPLE));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.RED));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.YELLOW));
		
		List<Integer> weightList = new ArrayList<>();
		weightList.add(25);
		weightList.add(75);
		weightList.add(25);
		weightList.add(25);
		weightList.add(25);
		weightList.add(75);
		weightList.add(25);
		weightList.add(25);
		weightList.add(25);
		weightList.add(25);
		weightList.add(75);
		
		List<PlacementModifier> treePlacementModifiers = List.of(
				VegetationPlacedFeatures.NOT_IN_SURFACE_WATER_MODIFIER,
				(PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP),
				(BlockFilterPlacementModifier.of(BlockPredicate.wouldSurvive(SpectrumBlocks.RED_SAPLING.getDefaultState(), BlockPos.ORIGIN)))
		);
		
		List<PlacedFeature> placedTreeFeatures = new ArrayList<>();
		for (RegistryEntry configuredFeature : treeList) {
			placedTreeFeatures.add(new PlacedFeature(configuredFeature, treePlacementModifiers));
		}
		
		Identifier randomColoredTreesFeatureIdentifier = SpectrumCommon.locate("random_colored_trees");
		// every x chunks
		RANDOM_COLORED_TREES_FEATURE = registerConfiguredAndPlacedFeature(
				randomColoredTreesFeatureIdentifier,
				SpectrumFeatures.WEIGHTED_RANDOM_FEATURE_PATCH,
				new WeightedRandomFeaturePatchConfig(5, 4, 3, new WeightedRandomFeatureConfig(placedTreeFeatures, weightList)),
				RarityFilterPlacementModifier.of(SpectrumCommon.CONFIG.ColoredTreePatchChanceChunk), // every x chunks
				HeightmapPlacementModifier.of(Heightmap.Type.WORLD_SURFACE_WG),
				BiomePlacementModifier.of(),
				SquarePlacementModifier.of()
		);
		
		BiomeModifications.addFeature(BiomeSelectors.tag(SpectrumBiomeTags.COLORED_TREES_GENERATING_IN), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, randomColoredTreesFeatureIdentifier));
	}
	
	private static void registerGeodes() {
		BlockState AIR = Blocks.AIR.getDefaultState();
		BlockState CALCITE = Blocks.CALCITE.getDefaultState();
		BlockState SMOOTH_BASALT = Blocks.SMOOTH_BASALT.getDefaultState();
		
		BlockState CITRINE_BLOCK = SpectrumBlocks.CITRINE_BLOCK.getDefaultState();
		BlockState BUDDING_CITRINE = SpectrumBlocks.BUDDING_CITRINE.getDefaultState();
		BlockState SMALL_CITRINE_BUD = SpectrumBlocks.SMALL_CITRINE_BUD.getDefaultState();
		BlockState MEDIUM_CITRINE_BUD = SpectrumBlocks.MEDIUM_CITRINE_BUD.getDefaultState();
		BlockState LARGE_CITRINE_BUD = SpectrumBlocks.LARGE_CITRINE_BUD.getDefaultState();
		BlockState CITRINE_CLUSTER = SpectrumBlocks.CITRINE_CLUSTER.getDefaultState();
		
		BlockState TOPAZ_BLOCK = SpectrumBlocks.TOPAZ_BLOCK.getDefaultState();
		BlockState BUDDING_TOPAZ = SpectrumBlocks.BUDDING_TOPAZ.getDefaultState();
		BlockState SMALL_TOPAZ_BUD = SpectrumBlocks.SMALL_TOPAZ_BUD.getDefaultState();
		BlockState MEDIUM_TOPAZ_BUD = SpectrumBlocks.MEDIUM_TOPAZ_BUD.getDefaultState();
		BlockState LARGE_TOPAZ_BUD = SpectrumBlocks.LARGE_TOPAZ_BUD.getDefaultState();
		BlockState TOPAZ_CLUSTER = SpectrumBlocks.TOPAZ_CLUSTER.getDefaultState();
		
		CITRINE_GEODE = registerConfiguredAndPlacedFeature(
				CITRINE_GEODE_IDENTIFIER,
				SpectrumFeatures.AIR_CHECK_GEODE,
				new GeodeFeatureConfig(
						new GeodeLayerConfig(
								BlockStateProvider.of(AIR),
								BlockStateProvider.of(CITRINE_BLOCK),
								BlockStateProvider.of(BUDDING_CITRINE),
								BlockStateProvider.of(CALCITE),
								BlockStateProvider.of(SMOOTH_BASALT),
								ImmutableList.of(SMALL_CITRINE_BUD, MEDIUM_CITRINE_BUD, LARGE_CITRINE_BUD, CITRINE_CLUSTER),
								BlockTags.FEATURES_CANNOT_REPLACE,
								BlockTags.GEODE_INVALID_BLOCKS),
						new GeodeLayerThicknessConfig(1.3D, 1.7D, 2.5D, 3.1),
						new GeodeCrackConfig(0.98D, 2.0D, 2),
						0.35D, 0.093D, true,
						UniformIntProvider.create(4, 6),
						UniformIntProvider.create(3, 4),
						UniformIntProvider.create(1, 2),
						-16, 16, 0.05D, 0),
				RarityFilterPlacementModifier.of(SpectrumCommon.CONFIG.CitrineGeodeChunkChance),
				SquarePlacementModifier.of(),
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(SpectrumCommon.CONFIG.CitrineGeodeMinAboveBottomGenerationHeight), YOffset.fixed(SpectrumCommon.CONFIG.CitrineGeodeFixedMaxGenerationHeight)),
				BiomePlacementModifier.of()
		);
		
		TOPAZ_GEODE = registerConfiguredAndPlacedFeature(
				TOPAZ_GEODE_IDENTIFIER,
				SpectrumFeatures.AIR_CHECK_GEODE,
				new GeodeFeatureConfig(
						new GeodeLayerConfig(
								BlockStateProvider.of(AIR),
								BlockStateProvider.of(TOPAZ_BLOCK),
								BlockStateProvider.of(BUDDING_TOPAZ),
								BlockStateProvider.of(CALCITE),
								BlockStateProvider.of(SMOOTH_BASALT),
								ImmutableList.of(SMALL_TOPAZ_BUD, MEDIUM_TOPAZ_BUD, LARGE_TOPAZ_BUD, TOPAZ_CLUSTER),
								BlockTags.FEATURES_CANNOT_REPLACE,
								BlockTags.GEODE_INVALID_BLOCKS),
						new GeodeLayerThicknessConfig(1.9D, 2.5D, 3.9D, 5.0D),
						new GeodeCrackConfig(0.6D, 2.0D, 2),
						0.35D, 0.073D, true,
						UniformIntProvider.create(4, 6),
						UniformIntProvider.create(3, 4),
						UniformIntProvider.create(1, 2),
						-16, 16, 0.05D, 1),
				RarityFilterPlacementModifier.of(SpectrumCommon.CONFIG.TopazGeodeChunkChance),
				SquarePlacementModifier.of(),
				HeightRangePlacementModifier.uniform(YOffset.fixed(SpectrumCommon.CONFIG.TopazGeodeMinFixedGenerationHeight), YOffset.belowTop(SpectrumCommon.CONFIG.TopazGeodeMaxBelowTopGenerationHeight)),
				BiomePlacementModifier.of()
		);
		
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_OVERWORLD), GenerationStep.Feature.UNDERGROUND_STRUCTURES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, CITRINE_GEODE_IDENTIFIER));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_OVERWORLD), GenerationStep.Feature.UNDERGROUND_STRUCTURES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, TOPAZ_GEODE_IDENTIFIER));
	}
	
	private static void registerPlants() {
		// MERMAIDS BRUSH
		Identifier mermaidsBrushIdentifier = SpectrumCommon.locate("mermaids_brush");
		registerConfiguredAndPlacedFeature(
				mermaidsBrushIdentifier,
				Feature.SIMPLE_BLOCK,
				new SimpleBlockFeatureConfig(new RandomizedIntBlockStateProvider(SimpleBlockStateProvider.of(SpectrumBlocks.MERMAIDS_BRUSH), MermaidsBrushBlock.AGE, UniformIntProvider.create(5, 6))),
				BiomePlacementModifier.of(),
				RarityFilterPlacementModifier.of(SpectrumCommon.CONFIG.MermaidsBrushChanceChunk),
				CountPlacementModifier.of(UniformIntProvider.create(2, 3)),
				SquarePlacementModifier.of(),
				PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP,
				BlockFilterPlacementModifier.of(BlockPredicate.allOf(BlockPredicate.wouldSurvive(SpectrumBlocks.MERMAIDS_BRUSH.getDefaultState(), BlockPos.ORIGIN)))
		);

		BiomeModifications.addFeature(BiomeSelectors.tag(BiomeTags.IS_OCEAN), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, mermaidsBrushIdentifier));
		
		// QUITOXIC REEDS
		Identifier quitoxicReedsIdentifier = SpectrumCommon.locate("quitoxic_reeds");
		registerConfiguredAndPlacedFeature(quitoxicReedsIdentifier,
				Feature.BLOCK_COLUMN,
				BlockColumnFeatureConfig.create(BiasedToBottomIntProvider.create(2, 4), BlockStateProvider.of(SpectrumBlocks.QUITOXIC_REEDS)),
				BiomePlacementModifier.of(),
				CountPlacementModifier.of(SpectrumCommon.CONFIG.QuitoxicReedsCountPerChunk),
				SquarePlacementModifier.of(),
				PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP,
				BlockFilterPlacementModifier.of(BlockPredicate.allOf(BlockPredicate.wouldSurvive(SpectrumBlocks.QUITOXIC_REEDS.getDefaultState(), BlockPos.ORIGIN)))
		);
		
		BiomeModifications.addFeature(BiomeSelectors.tag(SpectrumBiomeTags.QUITOXIC_REEDS_GENERATING_IN), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, quitoxicReedsIdentifier));
		
		// CLOVER
		Identifier cloversIdentifier = SpectrumCommon.locate("clovers");
		DataPool cloverBlockDataPool = DataPool.builder().add(SpectrumBlocks.CLOVER.getDefaultState(), 9).add(SpectrumBlocks.FOUR_LEAF_CLOVER.getDefaultState(), 1).build();
		RandomPatchFeatureConfig cloverPatchFeatureConfig = ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(cloverBlockDataPool)), List.of(Blocks.GRASS_BLOCK), 4);
		CLOVER_PATCH = registerConfiguredFeature(
				cloversIdentifier,
				Feature.RANDOM_PATCH,
				cloverPatchFeatureConfig);
		registerPlacedFeature(
				cloversIdentifier,
				CLOVER_PATCH,
				SquarePlacementModifier.of(),
				PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
				BiomePlacementModifier.of()
		);
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.PLAINS), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, cloversIdentifier));
	}
	
	public static final class Rules {
		public static final RuleTest END_STONE;
		
		static {
			END_STONE = new BlockMatchRuleTest(Blocks.END_STONE);
		}
	}
	
}
