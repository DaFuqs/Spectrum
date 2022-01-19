package de.dafuqs.spectrum.worldgen;

import com.google.common.collect.ImmutableList;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.conditional.MermaidsBrushBlock;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.worldgen.features.WeightedRandomFeatureConfig;
import de.dafuqs.spectrum.worldgen.features.WeightedRandomFeaturePatchConfig;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.BiasedToBottomIntProvider;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.decorator.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.RandomizedIntBlockStateProvider;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class SpectrumConfiguredFeatures {

	public static ConfiguredFeature<?, ?> CITRINE_GEODE;
	public static ConfiguredFeature<?, ?> TOPAZ_GEODE;
	public static ConfiguredFeature<?, ?> MOONSTONE_GEODE;

	// COLORED TREES
	public static HashMap<DyeColor, ConfiguredFeature<TreeFeatureConfig, ?>> COLORED_TREE_CONFIGURED_FEATURES = new HashMap<>(); // FOR SAPLINGS
	public static PlacedFeature RANDOM_COLORED_TREES_FEATURE; // FOR WORLD GEN
	
	public static void register() {
		registerGeodes();
		registerOres();
		registerColoredTrees();
		registerPlants();
	}

	private static <FC extends FeatureConfig> ConfiguredFeature<?, ?> registerConfiguredFeature(Identifier identifier, ConfiguredFeature<?, ?> configuredFeature) {
		return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, identifier, configuredFeature);
	}
	
	private static PlacedFeature registerPlacedFeature(Identifier identifier, PlacedFeature placedFeature) {
		return Registry.register(BuiltinRegistries.PLACED_FEATURE, identifier, placedFeature);
	}

	private static PlacedFeature registerConfiguredAndPlacedFeature(Identifier identifier, ConfiguredFeature<?, ?> configuredFeature, List<PlacementModifier> placementModifiers) {
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, identifier, configuredFeature);
		
		PlacedFeature placedFeature = configuredFeature.withPlacement(placementModifiers);
		Registry.register(BuiltinRegistries.PLACED_FEATURE, identifier, placedFeature);
		return placedFeature;
	}
	
	private static void registerOres() {
		BlockState sparklestoneOre = SpectrumBlocks.SPARKLESTONE_ORE.getDefaultState();
		BlockState deepslateSparklestoneOre = SpectrumBlocks.DEEPSLATE_SPARKLESTONE_ORE.getDefaultState();
		BlockState azuriteOre = SpectrumBlocks.AZURITE_ORE.getDefaultState();
		BlockState deepslateAzuriteOre = SpectrumBlocks.DEEPSLATE_AZURITE_ORE.getDefaultState();
		BlockState scarletOre = SpectrumBlocks.SCARLET_ORE.getDefaultState();
		BlockState paleturOre = SpectrumBlocks.PALETUR_ORE.getDefaultState();

		Identifier sparklestoneOreIdentifier = new Identifier(SpectrumCommon.MOD_ID, "sparklestone_ore");
		Identifier azuriteOreIdentifier = new Identifier(SpectrumCommon.MOD_ID, "azurite_ore");
		Identifier scarletOreIdentifier = new Identifier(SpectrumCommon.MOD_ID, "scarlet_ore");
		Identifier paleturOreIdentifier = new Identifier(SpectrumCommon.MOD_ID, "paletur_ore");
		
		ImmutableList<OreFeatureConfig.Target> SPARKLESTONE_ORE_TARGETS = ImmutableList.of(OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, sparklestoneOre), OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateSparklestoneOre));
		ImmutableList<OreFeatureConfig.Target> AZURITE_ORE_TARGETS = ImmutableList.of(OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, azuriteOre), OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateAzuriteOre));
		
		registerConfiguredAndPlacedFeature(
			sparklestoneOreIdentifier,
			Feature.ORE.configure(new OreFeatureConfig(SPARKLESTONE_ORE_TARGETS, 8)), List.of(
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(48), YOffset.fixed(128)), // min and max height
				CountPlacementModifier.of(9) // number of veins per chunk
		));
		
		registerConfiguredAndPlacedFeature(
			azuriteOreIdentifier,
			Feature.ORE.configure(new OreFeatureConfig(AZURITE_ORE_TARGETS, 5, 0.5F)), List.of(
				HeightRangePlacementModifier.trapezoid(YOffset.getBottom(), YOffset.aboveBottom(32)), // min and max height
				CountPlacementModifier.of(6) // number of veins per chunk
		));
		
		registerConfiguredAndPlacedFeature(
			scarletOreIdentifier,
			Feature.ORE.configure(new OreFeatureConfig(OreConfiguredFeatures.BASE_STONE_NETHER, scarletOre, 6)), List.of(
				HeightRangePlacementModifier.uniform(YOffset.aboveBottom(10), YOffset.belowTop(64)), // min and max height
				CountPlacementModifier.of(8) // number of veins per chunk
		));
		
		registerConfiguredAndPlacedFeature(
			paleturOreIdentifier,
			Feature.ORE.configure(new OreFeatureConfig(Rules.END_STONE, paleturOre, 4, 0.3F)), List.of(
				HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.getTop()), // min and max height
				CountPlacementModifier.of(6) // number of veins per chunk
		));
		
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, sparklestoneOreIdentifier));
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, azuriteOreIdentifier));
		BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, scarletOreIdentifier));
		BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, paleturOreIdentifier));
	}

	private static void registerColoredTree(@NotNull DyeColor dyeColor) {
		String identifierString = dyeColor + "_tree";
		RegistryKey<ConfiguredFeature<?, ?>> configuredFeatureRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier(SpectrumCommon.MOD_ID, identifierString));

		// how the colored tree will look when generated
		ConfiguredFeature<TreeFeatureConfig, ?> configuredFeature = Feature.TREE.configure(
				(new TreeFeatureConfig.Builder(
						BlockStateProvider.of(SpectrumBlocks.getColoredLogBlock(dyeColor).getDefaultState()),
						new StraightTrunkPlacer(4, 2, 2), // 4-8 height
						BlockStateProvider.of(SpectrumBlocks.getColoredLeavesBlock(dyeColor).getDefaultState()),
						new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
						new TwoLayersFeatureSize(1, 0, 1))
				).ignoreVines().build()
		);

		COLORED_TREE_CONFIGURED_FEATURES.put(dyeColor, Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, configuredFeatureRegistryKey.getValue(), configuredFeature));
	}

	private static void registerColoredTrees() {

		for (DyeColor dyeColor : DyeColor.values()) {
			registerColoredTree(dyeColor);
		}

		// Black/White and brown variants are not found in the wild and have to be created by the player
		List<ConfiguredFeature<?, ?>> treeList = new ArrayList<>();
		//treeList.add(COLORED_TREE_FEATURES.get(DyeColor.BLACK));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.BLUE));
		//treeList.add(COLORED_TREE_FEATURES.get(DyeColor.BROWN));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.CYAN));
		//treeList.add(COLORED_TREE_FEATURES.get(DyeColor.GRAY));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.GREEN));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.LIGHT_BLUE));
		//treeList.add(COLORED_TREE_FEATURES.get(DyeColor.LIGHT_GRAY));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.LIME));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.MAGENTA));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.ORANGE));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.PINK));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.PURPLE));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.RED));
		//treeList.add(COLORED_TREE_FEATURES.get(DyeColor.WHITE));
		treeList.add(COLORED_TREE_CONFIGURED_FEATURES.get(DyeColor.YELLOW));

		List<Integer> weightList = new ArrayList<>();
		//weightList.add(1);
		weightList.add(25);
		//weightList.add(25);
		weightList.add(75);
		//weightList.add(1);
		weightList.add(25);
		weightList.add(25);
		//weightList.add(1);
		weightList.add(25);
		weightList.add(75);
		weightList.add(25);
		weightList.add(25);
		weightList.add(25);
		weightList.add(25);
		//weightList.add(1);
		weightList.add(75);
		
		List<PlacementModifier> treePlacementModifiers = List.of(
				VegetationPlacedFeatures.NOT_IN_SURFACE_WATER_MODIFIER,
				(PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP),
				(BlockFilterPlacementModifier.of(BlockPredicate.wouldSurvive(SpectrumBlocks.RED_SAPLING.getDefaultState(), BlockPos.ORIGIN)))
		);
		List<PlacedFeature> placedTreeFeatures = new ArrayList<>();
		for(ConfiguredFeature<?, ?> configuredFeature : treeList) {
			placedTreeFeatures.add(configuredFeature.withPlacement(treePlacementModifiers));
		}
		
		Identifier randomColoredTreesFeatureIdentifier = new Identifier(SpectrumCommon.MOD_ID, "random_colored_trees");
		// every x chunks
		RANDOM_COLORED_TREES_FEATURE = registerConfiguredAndPlacedFeature(
			randomColoredTreesFeatureIdentifier,
			SpectrumFeatures.WEIGHTED_RANDOM_FEATURE_PATCH.configure(
					new WeightedRandomFeaturePatchConfig(5, 4, 3, new WeightedRandomFeatureConfig(placedTreeFeatures, weightList))
			),
			List.of(
				RarityFilterPlacementModifier.of(SpectrumCommon.CONFIG.ColoredTreePatchChanceChunk), // every x chunks
				HeightmapPlacementModifier.of(Heightmap.Type.WORLD_SURFACE_WG),
				BiomePlacementModifier.of()
			)
		);
		
		Predicate<BiomeSelectionContext> treeBiomes = BiomeSelectors.categories(
				Biome.Category.PLAINS,
				Biome.Category.EXTREME_HILLS,
				Biome.Category.JUNGLE,
				Biome.Category.FOREST,
				Biome.Category.SWAMP,
				Biome.Category.MESA,
				Biome.Category.MOUNTAIN,
				Biome.Category.DESERT,
				Biome.Category.ICY,
				Biome.Category.SAVANNA,
				Biome.Category.TAIGA);
		BiomeModifications.addFeature(treeBiomes, GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, randomColoredTreesFeatureIdentifier));
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

		BlockState MOONSTONE_BLOCK = SpectrumBlocks.MOONSTONE_BLOCK.getDefaultState();
		BlockState BUDDING_MOONSTONE = SpectrumBlocks.BUDDING_MOONSTONE.getDefaultState();
		BlockState MOONSTONE_CLUSTER = SpectrumBlocks.MOONSTONE_CLUSTER.getDefaultState();
		
		Identifier citrineGeodeFeatureIdentifier = new Identifier(SpectrumCommon.MOD_ID, "citrine_geode");
		Identifier topazGeodeFeatureIdentifier = new Identifier(SpectrumCommon.MOD_ID, "topaz_geode");
		Identifier moonstoneGeodeFeatureIdentifier = new Identifier(SpectrumCommon.MOD_ID, "moonstone_geode");
		
		CITRINE_GEODE = Feature.GEODE.configure(new GeodeFeatureConfig(
				new GeodeLayerConfig(
						BlockStateProvider.of(AIR),
						BlockStateProvider.of(CITRINE_BLOCK),
						BlockStateProvider.of(BUDDING_CITRINE),
						BlockStateProvider.of(CALCITE),
						BlockStateProvider.of(SMOOTH_BASALT),
						ImmutableList.of(SMALL_CITRINE_BUD, MEDIUM_CITRINE_BUD, LARGE_CITRINE_BUD, CITRINE_CLUSTER),
						BlockTags.FEATURES_CANNOT_REPLACE.getId(),
						BlockTags.GEODE_INVALID_BLOCKS.getId()),
				new GeodeLayerThicknessConfig(1.3D, 1.7D, 2.5D, 3.1),
				new GeodeCrackConfig(0.98D, 2.0D, 2),
				0.35D, 0.093D, true,
				UniformIntProvider.create(4, 6),
				UniformIntProvider.create(3, 4),
				UniformIntProvider.create(1, 2),
				-16, 16, 0.05D, 0)
		);
		
		TOPAZ_GEODE = Feature.GEODE.configure(new GeodeFeatureConfig(
				new GeodeLayerConfig(
						BlockStateProvider.of(AIR),
						BlockStateProvider.of(TOPAZ_BLOCK),
						BlockStateProvider.of(BUDDING_TOPAZ),
						BlockStateProvider.of(CALCITE),
						BlockStateProvider.of(SMOOTH_BASALT),
						ImmutableList.of(SMALL_TOPAZ_BUD, MEDIUM_TOPAZ_BUD, LARGE_TOPAZ_BUD, TOPAZ_CLUSTER),
						BlockTags.FEATURES_CANNOT_REPLACE.getId(),
						BlockTags.GEODE_INVALID_BLOCKS.getId()),
				new GeodeLayerThicknessConfig(1.9D, 2.5D, 3.9D, 5.0D),
				new GeodeCrackConfig(0.6D, 2.0D, 2),
				0.35D, 0.073D, true,
				UniformIntProvider.create(4, 6),
				UniformIntProvider.create(3, 4),
				UniformIntProvider.create(1, 2),
				-16, 16, 0.05D, 1)
		);
		
		MOONSTONE_GEODE = Feature.GEODE.configure(new GeodeFeatureConfig(
				new GeodeLayerConfig(
						BlockStateProvider.of(AIR),
						BlockStateProvider.of(MOONSTONE_BLOCK),
						BlockStateProvider.of(BUDDING_MOONSTONE),
						BlockStateProvider.of(CALCITE),
						BlockStateProvider.of(SMOOTH_BASALT),
						ImmutableList.of(MOONSTONE_CLUSTER), // forever untouched by man: generate with clusters only
						BlockTags.FEATURES_CANNOT_REPLACE.getId(),
						BlockTags.GEODE_INVALID_BLOCKS.getId()),
				new GeodeLayerThicknessConfig(4.0D, 5.0D, 6.0D, 7.5D),
				new GeodeCrackConfig(0.95D, 4.0D, 4),
				1.0D, 0.1D, true,
				UniformIntProvider.create(4, 6),
				UniformIntProvider.create(3, 4),
				UniformIntProvider.create(1, 2),
				-16, 16, 0.05D, 1)
		);
		
		registerConfiguredAndPlacedFeature(citrineGeodeFeatureIdentifier, CITRINE_GEODE, List.of(
			RarityFilterPlacementModifier.of(SpectrumCommon.CONFIG.CitrineGeodeChunkChance),
			SquarePlacementModifier.of(),
			HeightRangePlacementModifier.uniform(YOffset.aboveBottom(SpectrumCommon.CONFIG.CitrineGeodeMinAboveBottomGenerationHeight), YOffset.fixed(SpectrumCommon.CONFIG.CitrineGeodeFixedMaxGenerationHeight)),
			BiomePlacementModifier.of())
		);
		
		registerConfiguredAndPlacedFeature(topazGeodeFeatureIdentifier, TOPAZ_GEODE, List.of(
			RarityFilterPlacementModifier.of(SpectrumCommon.CONFIG.TopazGeodeChunkChance),
			SquarePlacementModifier.of(),
			HeightRangePlacementModifier.uniform(YOffset.fixed(SpectrumCommon.CONFIG.TopazGeodeMinFixedGenerationHeight), YOffset.belowTop(SpectrumCommon.CONFIG.TopazGeodeMaxBelowTopGenerationHeight)),
			BiomePlacementModifier.of()
		));
		
		registerConfiguredAndPlacedFeature(moonstoneGeodeFeatureIdentifier, MOONSTONE_GEODE, List.of(
			RarityFilterPlacementModifier.of(SpectrumCommon.CONFIG.MoonstoneGeodeChunkChance),
			SquarePlacementModifier.of(),
			HeightRangePlacementModifier.uniform(YOffset.aboveBottom(16), YOffset.belowTop(128)),
			BiomePlacementModifier.of()
		));
		
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_STRUCTURES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, citrineGeodeFeatureIdentifier));
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_STRUCTURES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, topazGeodeFeatureIdentifier));
	}

	private static void registerPlants() {
		// MERMAIDS BRUSH
		Identifier mermaidsBrushIdentifier = new Identifier(SpectrumCommon.MOD_ID, "mermaids_brush");
		registerConfiguredAndPlacedFeature(
			mermaidsBrushIdentifier,
				Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new RandomizedIntBlockStateProvider(SimpleBlockStateProvider.of(SpectrumBlocks.MERMAIDS_BRUSH), MermaidsBrushBlock.AGE, UniformIntProvider.create(5, 6)))),
			List.of(
				BiomePlacementModifier.of(),
				RarityFilterPlacementModifier.of(16),
				CountPlacementModifier.of(UniformIntProvider.create(2, 3)),
				SquarePlacementModifier.of(),
				PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP,
				BlockFilterPlacementModifier.of(BlockPredicate.allOf(BlockPredicate.wouldSurvive(SpectrumBlocks.MERMAIDS_BRUSH.getDefaultState(), BlockPos.ORIGIN)))
			)
		);

		Collection<RegistryKey<Biome>> oceans = new ArrayList<>();
		for(String biomeString : SpectrumCommon.CONFIG.MermaidsBrushGenerationBiomes) {
			RegistryKey<Biome> biomeKey = RegistryKey.of(Registry.BIOME_KEY, new Identifier(biomeString));
			if(biomeKey == null) {
				SpectrumCommon.log(Level.ERROR, "Mermaids Brush is configured to spawn in biome "+ biomeString + ", but that does not exist!");
			} else {
				oceans.add(biomeKey);
			}
		}
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(oceans), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, mermaidsBrushIdentifier));
		
		// QUITOXIC REED
		Identifier quitoxicReedsIdentifier = new Identifier(SpectrumCommon.MOD_ID, "quitoxic_reeds");
		registerConfiguredAndPlacedFeature(quitoxicReedsIdentifier,
				Feature.BLOCK_COLUMN.configure(BlockColumnFeatureConfig.create(
					BiasedToBottomIntProvider.create(2, 4),
					BlockStateProvider.of(SpectrumBlocks.QUITOXIC_REEDS))
				),
				List.of(
					BiomePlacementModifier.of(),
					CountPlacementModifier.of(25),
					SquarePlacementModifier.of(),
					PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP,
					BlockFilterPlacementModifier.of(BlockPredicate.allOf(BlockPredicate.wouldSurvive(SpectrumBlocks.QUITOXIC_REEDS.getDefaultState(), BlockPos.ORIGIN)))
				)
		);

		Collection<RegistryKey<Biome>> swamps = new ArrayList<>();
		for(String biomeString : SpectrumCommon.CONFIG.QuitoxicReedsGenerationBiomes) {
			RegistryKey<Biome> biomeKey = RegistryKey.of(Registry.BIOME_KEY, new Identifier(biomeString));
			if(biomeKey == null) {
				SpectrumCommon.log(Level.ERROR, "Quitoxic Reeds are configured to spawn in biome "+ biomeString + " but that does not exist!");
			} else {
				swamps.add(biomeKey);
			}
		}
		BiomeModifications.addFeature(BiomeSelectors.includeByKey(swamps), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, quitoxicReedsIdentifier));

		// CLOVER
		Identifier cloversIdentifier = new Identifier(SpectrumCommon.MOD_ID, "clovers");
		DataPool cloverBlockDataPool = DataPool.builder().add(SpectrumBlocks.CLOVER.getDefaultState(), 9).add(SpectrumBlocks.FOUR_LEAF_CLOVER.getDefaultState(), 1).build();
		registerConfiguredAndPlacedFeature(
				cloversIdentifier,
				Feature.RANDOM_PATCH.configure(ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK.configure(new SimpleBlockFeatureConfig(new WeightedBlockStateProvider(cloverBlockDataPool))), List.of(Blocks.GRASS_BLOCK), 4)),
				List.of(
						SquarePlacementModifier.of(),
						PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
						BiomePlacementModifier.of()
				)
		);
		BiomeModifications.addFeature(BiomeSelectors.categories(Biome.Category.PLAINS), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, cloversIdentifier));
	}

	public static final class Rules {
		public static final RuleTest END_STONE;

		static {
			END_STONE = new BlockMatchRuleTest(Blocks.END_STONE);
		}
	}

}
