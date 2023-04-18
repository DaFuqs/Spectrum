package de.dafuqs.spectrum.worldgen;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.worldgen.features.*;
import net.fabricmc.fabric.api.biome.v1.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
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
		// Geodes
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_OVERWORLD), GenerationStep.Feature.UNDERGROUND_STRUCTURES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, SpectrumCommon.locate("citrine_geode")));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_OVERWORLD), GenerationStep.Feature.UNDERGROUND_STRUCTURES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, SpectrumCommon.locate("topaz_geode")));
		
		// Ores
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_OVERWORLD), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, SpectrumCommon.locate("shimmerstone_ore")));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_OVERWORLD), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, SpectrumCommon.locate("azurite_ore")));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_NETHER), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, SpectrumCommon.locate("stratine_ore")));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_THE_END), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, SpectrumCommon.locate("paltaeria_ore")));
		
		// Colored Trees
		registerColoredTrees();
		
		// Plants
		BiomeModifications.addFeature(BiomeSelectors.tag(BiomeTags.IS_OCEAN), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, SpectrumCommon.locate("mermaids_brushes")));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.SWAMP), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, SpectrumCommon.locate("quitoxic_reeds")));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.PLAINS), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, SpectrumCommon.locate("clover_patch")));
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
	}
	
}
