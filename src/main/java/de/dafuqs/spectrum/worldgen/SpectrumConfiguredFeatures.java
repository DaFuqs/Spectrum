package de.dafuqs.spectrum.worldgen;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.conditional.*;
import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.worldgen.features.*;
import net.fabricmc.fabric.api.biome.v1.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.block.*;
import net.minecraft.structure.rule.*;
import net.minecraft.tag.*;
import net.minecraft.util.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.blockpredicate.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.*;
import net.minecraft.world.gen.foliage.*;
import net.minecraft.world.gen.placementmodifier.*;
import net.minecraft.world.gen.stateprovider.*;
import net.minecraft.world.gen.trunk.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static de.dafuqs.spectrum.helpers.WorldgenHelper.*;

public class SpectrumConfiguredFeatures {
	
	// Overworld
	public static RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> CLOVER_PATCH; // for bonemealing
	public static HashMap<DyeColor, RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>>> COLORED_TREE_CONFIGURED_FEATURES = new HashMap<>(); // for sapling growing
	public static RegistryEntry<PlacedFeature> RANDOM_COLORED_TREES_FEATURE; // for worldgen placing
	
	public static void register() {
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_OVERWORLD), GenerationStep.Feature.UNDERGROUND_STRUCTURES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, SpectrumCommon.locate("citrine_geode")));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_OVERWORLD), GenerationStep.Feature.UNDERGROUND_STRUCTURES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, SpectrumCommon.locate("topaz_geode")));
		
		registerOres();
		registerColoredTrees();
		registerPlants();
	}
	
	private static void registerOres() {
		BlockState shimmerstoneOre = SpectrumBlocks.SHIMMERSTONE_ORE.getDefaultState();
		BlockState deepslateShimmerstoneOre = SpectrumBlocks.DEEPSLATE_SHIMMERSTONE_ORE.getDefaultState();
		BlockState azuriteOre = SpectrumBlocks.AZURITE_ORE.getDefaultState();
		BlockState deepslateAzuriteOre = SpectrumBlocks.DEEPSLATE_AZURITE_ORE.getDefaultState();
		BlockState stratineOre = SpectrumBlocks.STRATINE_ORE.getDefaultState();
		BlockState paltaeriaOre = SpectrumBlocks.PALTAERIA_ORE.getDefaultState();
		
		Identifier shimmerstoneOreIdentifier = SpectrumCommon.locate("shimmerstone_ore");
		Identifier azuriteOreIdentifier = SpectrumCommon.locate("azurite_ore");
		Identifier stratineOreIdentifier = SpectrumCommon.locate("stratine_ore");
		Identifier paltaeriaOreIdentifier = SpectrumCommon.locate("paltaeria_ore");
		
		ImmutableList<OreFeatureConfig.Target> shimmerstoneOreTargets = ImmutableList.of(OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, shimmerstoneOre), OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateShimmerstoneOre));
		ImmutableList<OreFeatureConfig.Target> azuriteOreTargets = ImmutableList.of(OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, azuriteOre), OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateAzuriteOre));
		
		registerConfiguredAndPlacedFeature(
				shimmerstoneOreIdentifier,
				Feature.ORE,
				new OreFeatureConfig(shimmerstoneOreTargets, 8),
				CountPlacementModifier.of(9), // number of veins per chunk
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(48), YOffset.fixed(128)), // min and max height
				SquarePlacementModifier.of() // spread through the chunk
		);
		
		registerConfiguredAndPlacedFeature(
				azuriteOreIdentifier,
				Feature.ORE,
				new OreFeatureConfig(azuriteOreTargets, 5, 0.5F),
				CountPlacementModifier.of(6), // number of veins per chunk
				HeightRangePlacementModifier.trapezoid(YOffset.getBottom(), YOffset.aboveBottom(32)), // min and max height
				SquarePlacementModifier.of() // spread through the chunk
		);
		
		registerConfiguredAndPlacedFeature(
				stratineOreIdentifier,
				Feature.ORE,
				new OreFeatureConfig(OreConfiguredFeatures.BASE_STONE_NETHER, stratineOre, 6),
				CountPlacementModifier.of(12), // number of veins per chunk
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(10), YOffset.belowTop(64)), // min and max height
				SquarePlacementModifier.of() // spread through the chunk
		);
		
		registerConfiguredAndPlacedFeature(
				paltaeriaOreIdentifier,
				Feature.ORE,
				new OreFeatureConfig(new BlockMatchRuleTest(Blocks.END_STONE), paltaeriaOre, 12, 0.3F),
				CountPlacementModifier.of(4), // number of veins per chunk
				HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.getTop()), // min and max height
				SquarePlacementModifier.of() // spread through the chunk
		);
		
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_OVERWORLD), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, shimmerstoneOreIdentifier));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_OVERWORLD), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, azuriteOreIdentifier));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_NETHER), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, stratineOreIdentifier));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_THE_END), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, paltaeriaOreIdentifier));
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
		List<PlacementModifier> treePlacementModifiers = List.of(
				VegetationPlacedFeatures.NOT_IN_SURFACE_WATER_MODIFIER,
				PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP,
				BlockFilterPlacementModifier.of(BlockPredicate.wouldSurvive(SpectrumBlocks.RED_SAPLING.getDefaultState(), BlockPos.ORIGIN))
		);
		DataPool.Builder<PlacedFeature> placedTreeFeatureBuilder = DataPool.builder();
		placedTreeFeatureBuilder.add(new PlacedFeature((RegistryEntry) COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.BLUE), treePlacementModifiers), 25);
		placedTreeFeatureBuilder.add(new PlacedFeature((RegistryEntry) COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.CYAN), treePlacementModifiers), 75);
		placedTreeFeatureBuilder.add(new PlacedFeature((RegistryEntry) COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.GREEN), treePlacementModifiers), 25);
		placedTreeFeatureBuilder.add(new PlacedFeature((RegistryEntry) COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.LIGHT_BLUE), treePlacementModifiers), 25);
		placedTreeFeatureBuilder.add(new PlacedFeature((RegistryEntry) COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.LIME), treePlacementModifiers), 25);
		placedTreeFeatureBuilder.add(new PlacedFeature((RegistryEntry) COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.MAGENTA), treePlacementModifiers), 75);
		placedTreeFeatureBuilder.add(new PlacedFeature((RegistryEntry) COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.ORANGE), treePlacementModifiers), 25);
		placedTreeFeatureBuilder.add(new PlacedFeature((RegistryEntry) COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.PINK), treePlacementModifiers), 25);
		placedTreeFeatureBuilder.add(new PlacedFeature((RegistryEntry) COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.PURPLE), treePlacementModifiers), 25);
		placedTreeFeatureBuilder.add(new PlacedFeature((RegistryEntry) COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.RED), treePlacementModifiers), 25);
		placedTreeFeatureBuilder.add(new PlacedFeature((RegistryEntry) COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.YELLOW), treePlacementModifiers), 75);
		DataPool<PlacedFeature> placedTreeFeatures = placedTreeFeatureBuilder.build();

		Identifier randomColoredTreesFeatureIdentifier = SpectrumCommon.locate("random_colored_trees");
		// every x chunks
		RANDOM_COLORED_TREES_FEATURE = registerConfiguredAndPlacedFeature(
				randomColoredTreesFeatureIdentifier,
				SpectrumFeatures.WEIGHTED_RANDOM_FEATURE_PATCH,
				new WeightedRandomFeaturePatchConfig(5, 4, 3, new WeightedRandomFeatureConfig(placedTreeFeatures)),
				RarityFilterPlacementModifier.of(SpectrumCommon.CONFIG.ColoredTreePatchChanceChunk), // every x chunks
				HeightmapPlacementModifier.of(Heightmap.Type.WORLD_SURFACE_WG),
				BiomePlacementModifier.of(),
				SquarePlacementModifier.of()
		);
		
		BiomeModifications.addFeature(BiomeSelectors.tag(SpectrumBiomeTags.COLORED_TREES_GENERATING_IN), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, randomColoredTreesFeatureIdentifier));
	}
	
	private static void registerPlants() {
		// MERMAIDS BRUSH
		Identifier mermaidsBrushIdentifier = SpectrumCommon.locate("mermaids_brush");
		registerConfiguredAndPlacedFeature(
				mermaidsBrushIdentifier,
				Feature.SIMPLE_BLOCK,
				new SimpleBlockFeatureConfig(new RandomizedIntBlockStateProvider(SimpleBlockStateProvider.of(SpectrumBlocks.MERMAIDS_BRUSH), MermaidsBrushBlock.AGE, UniformIntProvider.create(5, 6))),
				RarityFilterPlacementModifier.of(SpectrumCommon.CONFIG.MermaidsBrushChanceChunk),
				CountPlacementModifier.of(UniformIntProvider.create(2, 3)),
				SquarePlacementModifier.of(),
				PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP,
				BiomePlacementModifier.of(),
				BlockFilterPlacementModifier.of(BlockPredicate.allOf(BlockPredicate.wouldSurvive(SpectrumBlocks.MERMAIDS_BRUSH.getDefaultState(), BlockPos.ORIGIN)))
		);
		
		BiomeModifications.addFeature(BiomeSelectors.tag(BiomeTags.IS_OCEAN), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, mermaidsBrushIdentifier));
		
		// QUITOXIC REEDS
		Identifier quitoxicReedsIdentifier = SpectrumCommon.locate("quitoxic_reeds");
		registerConfiguredAndPlacedFeature(quitoxicReedsIdentifier,
				Feature.BLOCK_COLUMN,
				BlockColumnFeatureConfig.create(BiasedToBottomIntProvider.create(2, 4), BlockStateProvider.of(SpectrumBlocks.QUITOXIC_REEDS)),
				CountPlacementModifier.of(SpectrumCommon.CONFIG.QuitoxicReedsCountPerChunk),
				SquarePlacementModifier.of(),
				PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP,
				BiomePlacementModifier.of(),
				BlockFilterPlacementModifier.of(BlockPredicate.allOf(BlockPredicate.wouldSurvive(SpectrumBlocks.QUITOXIC_REEDS.getDefaultState(), BlockPos.ORIGIN)))
		);
		
		BiomeModifications.addFeature(BiomeSelectors.tag(SpectrumBiomeTags.QUITOXIC_REEDS_GENERATING_IN), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, quitoxicReedsIdentifier));
		
		// CLOVER
		Identifier cloversIdentifier = SpectrumCommon.locate("clovers");
		DataPool<BlockState> cloverBlockDataPool = DataPool.<BlockState>builder().add(SpectrumBlocks.CLOVER.getDefaultState(), 9).add(SpectrumBlocks.FOUR_LEAF_CLOVER.getDefaultState(), 1).build();
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
	
}
