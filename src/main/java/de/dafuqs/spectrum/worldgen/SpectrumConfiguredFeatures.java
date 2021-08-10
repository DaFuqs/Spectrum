package de.dafuqs.spectrum.worldgen;

import com.google.common.collect.ImmutableList;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class SpectrumConfiguredFeatures extends ConfiguredFeatures {

    public static ConfiguredFeature<?, ?> CITRINE_GEODE;
    public static ConfiguredFeature<?, ?> TOPAZ_GEODE;
    public static ConfiguredFeature<?, ?> MOONSTONE_GEODE;

    // COLORED TREES
    public static HashMap<DyeColor, ConfiguredFeature<TreeFeatureConfig, ?>> COLORED_TREE_FEATURES = new HashMap<>(); // FOR SAPLINGS
    public static ConfiguredFeature<?, ?> RANDOM_COLORED_TREES_FEATURE; // FOR WORLD GEN


    private static ImmutableList<OreFeatureConfig.Target> SPARKLESTONE_ORE_TARGETS;
    private static ImmutableList<OreFeatureConfig.Target> AZURITE_ORE_TARGETS;

    private static ConfiguredFeature<?, ?> SPARKLESTONE_ORE;
    private static ConfiguredFeature<?, ?> AZURITE_ORE;
    private static ConfiguredFeature<?, ?> PALETUR_ORE;
    private static ConfiguredFeature<?, ?> SCARLET_ORE;

    private static ConfiguredFeature<?, ?> QUITOXIC_REEDS;
    private static ConfiguredFeature<?, ?> MERMAIDS_BRUSH;

    public static void register() {
        registerGeodes();
        registerOres();
        registerColoredTrees();
        registerPlants();
    }

    private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> registerConfiguredFeature(Identifier identifier, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, identifier, configuredFeature);
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

        SPARKLESTONE_ORE_TARGETS = ImmutableList.of(OreFeatureConfig.createTarget(OreFeatureConfig.Rules.STONE_ORE_REPLACEABLES, sparklestoneOre), OreFeatureConfig.createTarget(OreFeatureConfig.Rules.DEEPSLATE_ORE_REPLACEABLES, deepslateSparklestoneOre));
        AZURITE_ORE_TARGETS = ImmutableList.of(OreFeatureConfig.createTarget(OreFeatureConfig.Rules.STONE_ORE_REPLACEABLES, azuriteOre), OreFeatureConfig.createTarget(OreFeatureConfig.Rules.DEEPSLATE_ORE_REPLACEABLES, deepslateAzuriteOre));

        SPARKLESTONE_ORE = registerConfiguredFeature(sparklestoneOreIdentifier,
                Feature.ORE.configure(new OreFeatureConfig(SPARKLESTONE_ORE_TARGETS, 17)) // vein size
                        .uniformRange(YOffset.fixed(80), YOffset.belowTop(0)) // min and max height
                        .spreadHorizontally()
                        .repeat(4)); // number of veins per chunk

        AZURITE_ORE = registerConfiguredFeature(azuriteOreIdentifier,
                Feature.ORE.configure(new OreFeatureConfig(AZURITE_ORE_TARGETS, 5)) // vein size
                        .uniformRange(YOffset.getBottom(), YOffset.aboveBottom(64)) // min and max height
                        .spreadHorizontally()
                        .repeat(4)); // number of veins per chunk

        SCARLET_ORE = registerConfiguredFeature(scarletOreIdentifier,
                Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_NETHER, scarletOre, 6)) // vein size
                        .uniformRange(YOffset.aboveBottom(10), YOffset.belowTop(10)) // min and max height
                        .spreadHorizontally()
                        .repeat(8)); // number of veins per chunk

        PALETUR_ORE = registerConfiguredFeature(paleturOreIdentifier,
                Feature.ORE.configure(new OreFeatureConfig(Rules.END_STONE, paleturOre, 4, 0.3F)) // vein size + discard on air exposure
                        .uniformRange(YOffset.getBottom(), YOffset.aboveBottom(80)) // min and max height
                        .spreadHorizontally()
                        .repeat(6)); // number of veins per chunk

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, sparklestoneOreIdentifier));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, azuriteOreIdentifier));
        BiomeModifications.addFeature(BiomeSelectors.foundInTheNether(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, scarletOreIdentifier));
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, paleturOreIdentifier));
    }

    private static void registerColoredTree(DyeColor dyeColor) {
        String identifierString = dyeColor.toString() + "_tree";
        RegistryKey<ConfiguredFeature<?, ?>> configuredFeatureRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier(SpectrumCommon.MOD_ID, identifierString));

        // how the colored tree will look when generated
        ConfiguredFeature<TreeFeatureConfig, ?> configuredFeature = Feature.TREE.configure(
                (new TreeFeatureConfig.Builder(
                        new SimpleBlockStateProvider(SpectrumBlocks.getColoredLogBlock(dyeColor).getDefaultState()),
                        new StraightTrunkPlacer(4, 2, 2), // 4-8 height
                        new SimpleBlockStateProvider(SpectrumBlocks.getColoredLeavesBlock(dyeColor).getDefaultState()),
                        new SimpleBlockStateProvider(SpectrumBlocks.getColoredSaplingBlock(dyeColor).getDefaultState()),
                        new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
                        new TwoLayersFeatureSize(1, 0, 1))
                ).ignoreVines().build()
        );

        COLORED_TREE_FEATURES.put(dyeColor, Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, configuredFeatureRegistryKey.getValue(), configuredFeature));
    }

    private static void registerColoredTrees() {

        for (DyeColor dyeColor : DyeColor.values()) {
            registerColoredTree(dyeColor);
        }

        RANDOM_COLORED_TREES_FEATURE = Feature.RANDOM_SELECTOR.configure(
                new RandomFeatureConfig(ImmutableList.of(
                        COLORED_TREE_FEATURES.get(DyeColor.BLACK).withChance(0.01F),
                        COLORED_TREE_FEATURES.get(DyeColor.BLUE).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.BROWN).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.CYAN).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.GRAY).withChance(0.01F),
                        COLORED_TREE_FEATURES.get(DyeColor.GREEN).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.LIGHT_BLUE).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.LIGHT_GRAY).withChance(0.01F),
                        COLORED_TREE_FEATURES.get(DyeColor.LIME).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.MAGENTA).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.ORANGE).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.PINK).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.PURPLE).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.RED).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.WHITE).withChance(0.001F),
                        COLORED_TREE_FEATURES.get(DyeColor.YELLOW).withChance(0.025F)
                ), ConfiguredFeatures.OAK
                )
        ).applyChance(20).decorate(Decorators.HEIGHTMAP_WORLD_SURFACE);

        RegistryKey<ConfiguredFeature<?, ?>> RANDOM_COLORED_TREES_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier(SpectrumCommon.MOD_ID, "random_colored_trees"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, RANDOM_COLORED_TREES_KEY.getValue(), RANDOM_COLORED_TREES_FEATURE);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.VEGETAL_DECORATION, RANDOM_COLORED_TREES_KEY);
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
        BlockState SMALL_MOONSTONE_BUD = SpectrumBlocks.SMALL_MOONSTONE_BUD.getDefaultState();
        BlockState MEDIUM_MOONSTONE_BUD = SpectrumBlocks.MEDIUM_MOONSTONE_BUD.getDefaultState();
        BlockState LARGE_MOONSTONE_BUD = SpectrumBlocks.LARGE_MOONSTONE_BUD.getDefaultState();
        BlockState MOONSTONE_CLUSTER = SpectrumBlocks.MOONSTONE_CLUSTER.getDefaultState();

        CITRINE_GEODE = ((SpectrumFeatures.SOLID_BLOCKS_GEODE.configure(new GeodeFeatureConfig(
                new GeodeLayerConfig(
                        new SimpleBlockStateProvider(AIR),
                        new SimpleBlockStateProvider(CITRINE_BLOCK),
                        new SimpleBlockStateProvider(BUDDING_CITRINE),
                        new SimpleBlockStateProvider(CALCITE),
                        new SimpleBlockStateProvider(SMOOTH_BASALT),
                        ImmutableList.of(SMALL_CITRINE_BUD, MEDIUM_CITRINE_BUD, LARGE_CITRINE_BUD, CITRINE_CLUSTER),
                        BlockTags.FEATURES_CANNOT_REPLACE.getId(),
                        BlockTags.GEODE_INVALID_BLOCKS.getId()),
                new GeodeLayerThicknessConfig(1.3D, 1.7D, 2.5D, 3.1),
                new GeodeCrackConfig(0.98D, 2.0D, 2),
                0.35D, 0.093D, true,
                UniformIntProvider.create(4, 6),
                UniformIntProvider.create(3, 4),
                UniformIntProvider.create(1, 2),
                -16, 16, 0.05D, 1)
        ).uniformRange(YOffset.aboveBottom(SpectrumCommon.CONFIG.CitrineGeodeMinAboveBottomGenerationHeight), YOffset.fixed(SpectrumCommon.CONFIG.CitrineGeodeFixedMaxGenerationHeight))
        ).spreadHorizontally()).applyChance(50);

        TOPAZ_GEODE = ((SpectrumFeatures.SOLID_BLOCKS_GEODE.configure(new GeodeFeatureConfig(
                new GeodeLayerConfig(
                        new SimpleBlockStateProvider(AIR),
                        new SimpleBlockStateProvider(TOPAZ_BLOCK),
                        new SimpleBlockStateProvider(BUDDING_TOPAZ),
                        new SimpleBlockStateProvider(CALCITE),
                        new SimpleBlockStateProvider(SMOOTH_BASALT),
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
        ).uniformRange(YOffset.fixed(SpectrumCommon.CONFIG.TopazGeodeMinFixedGenerationHeight), YOffset.belowTop(SpectrumCommon.CONFIG.TopazGeodeMaxBelowTopGenerationHeight))
        ).spreadHorizontally()).applyChance(10);

        MOONSTONE_GEODE = ((SpectrumFeatures.SOLID_BLOCKS_GEODE.configure(new GeodeFeatureConfig(
                new GeodeLayerConfig(
                        new SimpleBlockStateProvider(AIR),
                        new SimpleBlockStateProvider(MOONSTONE_BLOCK),
                        new SimpleBlockStateProvider(BUDDING_MOONSTONE),
                        new SimpleBlockStateProvider(CALCITE),
                        new SimpleBlockStateProvider(SMOOTH_BASALT),
                        ImmutableList.of(SMALL_MOONSTONE_BUD, MEDIUM_MOONSTONE_BUD, LARGE_MOONSTONE_BUD, MOONSTONE_CLUSTER),
                        BlockTags.FEATURES_CANNOT_REPLACE.getId(),
                        BlockTags.GEODE_INVALID_BLOCKS.getId()),
                new GeodeLayerThicknessConfig(1.7D, 2.2D, 3.2D, 4.2D),
                new GeodeCrackConfig(0.95D, 2.0D, 2),
                0.35D, 0.083D, true,
                UniformIntProvider.create(4, 6),
                UniformIntProvider.create(3, 4),
                UniformIntProvider.create(1, 2),
                -16, 16, 0.05D, 1)
        ).uniformRange(YOffset.aboveBottom(10), YOffset.belowTop(10))
        ).spreadHorizontally()).applyChance(40);

        RegistryKey<ConfiguredFeature<?, ?>> CITRINE_GEODE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier(SpectrumCommon.MOD_ID, "citrine_geode"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, CITRINE_GEODE_KEY.getValue(), CITRINE_GEODE);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_STRUCTURES, CITRINE_GEODE_KEY);

        RegistryKey<ConfiguredFeature<?, ?>> TOPAZ_GEODE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier(SpectrumCommon.MOD_ID, "topaz_geode"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, TOPAZ_GEODE_KEY.getValue(), TOPAZ_GEODE);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_STRUCTURES, TOPAZ_GEODE_KEY);

        RegistryKey<ConfiguredFeature<?, ?>> MOONSTONE_GEODE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, new Identifier(SpectrumCommon.MOD_ID, "moonstone_geode"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, MOONSTONE_GEODE_KEY.getValue(), MOONSTONE_GEODE);
    }

    private static void registerPlants() {

        // MERMAIDS BRUSH
        Identifier mermaidsBrushIdentifier = new Identifier(SpectrumCommon.MOD_ID, "mermaids_brush");

        MERMAIDS_BRUSH = registerConfiguredFeature(mermaidsBrushIdentifier,
                Feature.RANDOM_PATCH.configure((
                        new RandomPatchFeatureConfig.Builder(
                                new SimpleBlockStateProvider(SpectrumBlocks.MERMAIDS_BRUSH.getDefaultState()),
                                SimpleBlockPlacer.INSTANCE))
                        .tries(2)
                        .cannotProject()
                        .canReplace()
                        .build()
                )
                        .repeat(1)
                        .decorate(Decorators.HEIGHTMAP_OCEAN_FLOOR)
        );

        Collection<RegistryKey<Biome>> deepOceans = new ArrayList<>();
        for(String biomeString : SpectrumCommon.CONFIG.MermaidsBrushGenerationBiomes) {
            RegistryKey<Biome> biomeKey = RegistryKey.of(Registry.BIOME_KEY, new Identifier(biomeString));
            if(biomeKey == null) {
                SpectrumCommon.log(Level.ERROR, "Mermaids Brush is configured to spawn in biome "+ biomeString + " but that does not exist!");
            } else {
                deepOceans.add(biomeKey);
            }
        }
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(deepOceans), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, mermaidsBrushIdentifier));


        // QUITOXIC REEDS
        Identifier quitoxicReedsIdentifier = new Identifier(SpectrumCommon.MOD_ID, "quitoxic_reeds");
        HashSet<Block> quitoxicReedsWhiteList = new HashSet<>();
        quitoxicReedsWhiteList.add(Blocks.WATER);
        quitoxicReedsWhiteList.add(Blocks.CLAY);


        QUITOXIC_REEDS = registerConfiguredFeature(quitoxicReedsIdentifier,
                Feature.RANDOM_PATCH.configure((
                        new RandomPatchFeatureConfig.Builder(
                                new SimpleBlockStateProvider(SpectrumBlocks.QUITOXIC_REEDS.getDefaultState()),
                                new QuitoxicReedsColumnPlacer(UniformIntProvider.create(2, 4))))
                        .tries(10).spreadX(4).spreadY(0).spreadZ(4).canReplace().cannotProject().whitelist(quitoxicReedsWhiteList).build()
                )
                        .decorate(Decorators.HEIGHTMAP_OCEAN_FLOOR)
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
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(swamps), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, quitoxicReedsIdentifier));
    }

    public static final class Rules {
        public static final RuleTest END_STONE;

        static {
            END_STONE = new BlockMatchRuleTest(Blocks.END_STONE);
        }
    }

}
