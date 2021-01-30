package de.dafuqs.spectrum.worldgen;

import com.google.common.collect.ImmutableList;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.decorator.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.HashMap;

import static net.minecraft.world.gen.feature.ConfiguredFeatures.OAK;

public class SpectrumFeatures {

    public static ConfiguredFeature<?, ?> CITRINE_GEODE;
    public static ConfiguredFeature<?, ?> TOPAZ_GEODE;
    public static ConfiguredFeature<?, ?> MOONSTONE_GEODE;

    // COLORED TREES
    public static HashMap<DyeColor, ConfiguredFeature<TreeFeatureConfig, ?>> COLORED_TREE_FEATURES = new HashMap<>(); // FOR SAPLINGS
    //public static HashMap<DyeColor, ConfiguredFeature<?, ?>> DECORATED_TREE_FEATURES = new HashMap<>(); // WOR WORLD GEN

    public static ConfiguredFeature<?, ?> DECORATED_TREES; // WOR WORLD GEN

    public static void register() {
        registerGeodes();
        registerColoredTrees();
    }

    private static void registerColoredTree(DyeColor dyeColor) {
        String identifierString = dyeColor.toString() + "_tree";
        RegistryKey<ConfiguredFeature<?, ?>> configuredFeatureRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(SpectrumCommon.MOD_ID, identifierString));
        //RegistryKey<ConfiguredFeature<?, ?>> decoratedFeatureRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(SpectrumCommon.MOD_ID, identifierString + "_decorated"));

        // TODO: add custom tree feature config (currently generates exactly like oak)
        // how the colored tree will look when generated
        ConfiguredFeature<TreeFeatureConfig, ?> configuredFeature = Feature.TREE.configure(
                (new TreeFeatureConfig.Builder(
                        new SimpleBlockStateProvider(SpectrumBlocks.getColoredLog(dyeColor).getDefaultState()),
                        new SimpleBlockStateProvider(SpectrumBlocks.getColoredLeaves(dyeColor).getDefaultState()),
                        new BlobFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(0), 3),
                        new StraightTrunkPlacer(4, 2, 0),
                        new TwoLayersFeatureSize(1, 0, 1)))
                        .ignoreVines().build());

        // how often the tree will generate naturally
        // generate ~every 100 chunks
        //ChanceDecoratorConfig chanceDecoratorConfig = new ChanceDecoratorConfig(20 + dyeColor.getId());
        //ConfiguredFeature<?, ?> decoratedFeatureConfig = configuredFeature.decorate(Decorator.CHANCE.configure(chanceDecoratorConfig));
        // In which biomes the tree will generate
        //BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.VEGETAL_DECORATION, decoratedFeatureRegistryKey);

        COLORED_TREE_FEATURES.put(dyeColor, Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, configuredFeatureRegistryKey.getValue(), configuredFeature));
        //DECORATED_TREE_FEATURES.put(dyeColor, Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, decoratedFeatureRegistryKey.getValue(), decoratedFeatureConfig));
    }

    /*private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> registerFeature(String id, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, configuredFeature);
    }*/

    private static void registerColoredTrees() {
        for(DyeColor dyeColor : DyeColor.values()) {
            registerColoredTree(dyeColor);
        }

        ConfiguredFeature<?, ?> configuredFeature = Feature.RANDOM_SELECTOR.configure(
                new RandomFeatureConfig(ImmutableList.of(
                        COLORED_TREE_FEATURES.get(DyeColor.BLACK).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.BLUE).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.BROWN).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.CYAN).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.GRAY).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.GREEN).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.LIGHT_BLUE).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.LIGHT_GRAY).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.LIME).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.MAGENTA).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.ORANGE).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.PINK).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.PURPLE).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.RED).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.WHITE).withChance(0.025F),
                        COLORED_TREE_FEATURES.get(DyeColor.YELLOW).withChance(0.025F)
                        ), OAK)).decorate(Decorator.DARK_OAK_TREE.configure(DecoratorConfig.DEFAULT).applyChance(20));

        RegistryKey<ConfiguredFeature<?, ?>> decoratedFeatureRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(SpectrumCommon.MOD_ID, "random_colored_trees"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, decoratedFeatureRegistryKey.getValue(), configuredFeature);

        // Add generation to world
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.VEGETAL_DECORATION, decoratedFeatureRegistryKey);
    }

    private static void registerGeodes() {
        BlockState AIR = Blocks.AIR.getDefaultState();
        BlockState CALCITE = Blocks.CALCITE.getDefaultState();
        BlockState TUFF = Blocks.TUFF.getDefaultState();

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

        CITRINE_GEODE = (Feature.GEODE.configure(new GeodeFeatureConfig(
                new GeodeLayerConfig(
                        new SimpleBlockStateProvider(AIR),
                        new SimpleBlockStateProvider(CITRINE_BLOCK),
                        new SimpleBlockStateProvider(BUDDING_CITRINE),
                        new SimpleBlockStateProvider(CALCITE),
                        new SimpleBlockStateProvider(TUFF),
                        ImmutableList.of(SMALL_CITRINE_BUD, MEDIUM_CITRINE_BUD, LARGE_CITRINE_BUD, CITRINE_CLUSTER)),
                new GeodeLayerThicknessConfig(1.7D, 2.2D, 3.2D, 4.2D),
                new GeodeCrackConfig(0.95D, 2.0D, 2), 0.35D, 0.083D, true, 4, 7, 3, 5, 1, 3, -16, 16, 0.05D))
                .decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(6, 0, 47))).spreadHorizontally()).applyChance(48);

        TOPAZ_GEODE = (Feature.GEODE.configure(new GeodeFeatureConfig(
                new GeodeLayerConfig(
                        new SimpleBlockStateProvider(AIR),
                        new SimpleBlockStateProvider(TOPAZ_BLOCK),
                        new SimpleBlockStateProvider(BUDDING_TOPAZ),
                        new SimpleBlockStateProvider(CALCITE),
                        new SimpleBlockStateProvider(TUFF),
                        ImmutableList.of(SMALL_TOPAZ_BUD, MEDIUM_TOPAZ_BUD, LARGE_TOPAZ_BUD, TOPAZ_CLUSTER)),
                new GeodeLayerThicknessConfig(1.7D, 2.2D, 3.2D, 4.2D),
                new GeodeCrackConfig(0.95D, 2.0D, 2), 0.35D, 0.083D, true, 4, 7, 3, 5, 1, 3, -16, 16, 0.05D))
                .decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(6, 0, 47))).spreadHorizontally()).applyChance(48);

        MOONSTONE_GEODE = (Feature.GEODE.configure(new GeodeFeatureConfig(
                new GeodeLayerConfig(
                        new SimpleBlockStateProvider(AIR),
                        new SimpleBlockStateProvider(MOONSTONE_BLOCK),
                        new SimpleBlockStateProvider(BUDDING_MOONSTONE),
                        new SimpleBlockStateProvider(CALCITE),
                        new SimpleBlockStateProvider(TUFF),
                        ImmutableList.of(SMALL_MOONSTONE_BUD, MEDIUM_MOONSTONE_BUD, LARGE_MOONSTONE_BUD, MOONSTONE_CLUSTER)),
                new GeodeLayerThicknessConfig(1.7D, 2.2D, 3.2D, 4.2D),
                new GeodeCrackConfig(0.95D, 2.0D, 2), 0.35D, 0.083D, true, 4, 7, 3, 5, 1, 3, -16, 16, 0.05D))
                .decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(6, 0, 47))).spreadHorizontally()).applyChance(48);

        RegistryKey<ConfiguredFeature<?, ?>> CITRINE_GEODE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(SpectrumCommon.MOD_ID, "citrine_geode"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, CITRINE_GEODE_KEY.getValue(), CITRINE_GEODE);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_STRUCTURES, CITRINE_GEODE_KEY);

        RegistryKey<ConfiguredFeature<?, ?>> TOPAZ_GEODE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(SpectrumCommon.MOD_ID, "topaz_geode"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, TOPAZ_GEODE_KEY.getValue(), TOPAZ_GEODE);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_STRUCTURES, TOPAZ_GEODE_KEY);

        RegistryKey<ConfiguredFeature<?, ?>> MOONSTONE_GEODE_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(SpectrumCommon.MOD_ID, "moonstone_geode"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, MOONSTONE_GEODE_KEY.getValue(), MOONSTONE_GEODE);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_STRUCTURES, MOONSTONE_GEODE_KEY);
    }


}
