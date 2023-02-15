package de.dafuqs.spectrum.deeper_down;

import com.google.common.collect.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.worldgen.*;
import de.dafuqs.spectrum.worldgen.features.*;
import net.minecraft.block.*;
import net.minecraft.structure.rule.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.blockpredicate.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.heightprovider.*;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.*;

import static de.dafuqs.spectrum.helpers.WorldgenHelper.*;
import static net.minecraft.world.gen.feature.OreConfiguredFeatures.*;

public class DDPlacedFeatures {

    public static final RuleTest BLACKSLAG_ORE_REPLACEABLE_TEST = new TagMatchRuleTest(SpectrumBlockTags.BLACKSLAG_ORE_REPLACEABLES);

    public static void register() {
        registerSpectrumOres();
        registerVanillaOres();
        registerGeodes();
        registerDecorators();
        registerNoxshrooms();
    }

    public static void registerAndConfigureOreFeature(String identifier, OreFeatureConfig oreFeatureConfig, PlacementModifier... placementModifiers) {
        Identifier id = SpectrumCommon.locate(identifier);
        registerConfiguredAndPlacedFeature(id, Feature.ORE, oreFeatureConfig, placementModifiers);
    }

    private static void registerNoxshrooms() {
        RegistryEntry chestnut = BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("chestnut_noxfungus"), DDConfiguredFeatures.CHESTNUT_NOXFUNGUS);
        RegistryEntry ebony = BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("ebony_noxfungus"), DDConfiguredFeatures.EBONY_NOXFUNGUS);
        RegistryEntry ivory = BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("ivory_noxfungus"), DDConfiguredFeatures.IVORY_NOXFUNGUS);
        RegistryEntry slate = BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("slate_noxfungus"), DDConfiguredFeatures.SLATE_NOXFUNGUS);

        // Black/White and brown variants are not found in the wild and have to be created by the player
        List<RegistryEntry> treeList = new ArrayList<>();
        treeList.add(chestnut);
        treeList.add(ebony);
        treeList.add(ivory);
        treeList.add(slate);

        List<Integer> weightList = new ArrayList<>();
        weightList.add(1);
        weightList.add(25);
        weightList.add(25);
        weightList.add(25);

        List<PlacementModifier> placementModifiers = List.of(
                VegetationPlacedFeatures.NOT_IN_SURFACE_WATER_MODIFIER,
                PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP,
                BlockFilterPlacementModifier.of(BlockPredicate.wouldSurvive(SpectrumBlocks.RED_SAPLING.getDefaultState(), BlockPos.ORIGIN))
        );

        List<PlacedFeature> placedFeatures = new ArrayList<>();
        for (RegistryEntry configuredFeature : treeList) {
            placedFeatures.add(new PlacedFeature(configuredFeature, placementModifiers));
        }

        // every x chunks
        registerConfiguredAndPlacedFeature(
                SpectrumCommon.locate("mushroom_forest_mushrooms"),
                SpectrumFeatures.WEIGHTED_RANDOM_FEATURE_PATCH,
                new WeightedRandomFeaturePatchConfig(5, 4, 3, new WeightedRandomFeatureConfig(placedFeatures, weightList)),
                CountMultilayerPlacementModifier.of(6),
                BiomePlacementModifier.of()
        );
    }

    public static void registerSpectrumOres() {
        registerAndConfigureOreFeature("dd_malachite_ore",
                new OreFeatureConfig(ImmutableList.of(
                        OreFeatureConfig.createTarget(BLACKSLAG_ORE_REPLACEABLE_TEST, SpectrumBlocks.BLACKSLAG_MALACHITE_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(STONE_ORE_REPLACEABLES, SpectrumBlocks.MALACHITE_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(DEEPSLATE_ORE_REPLACEABLES, SpectrumBlocks.DEEPSLATE_MALACHITE_ORE.getDefaultState())
                ), 7, 0.75F),
                HeightRangePlacementModifier.uniform(YOffset.aboveBottom(4), YOffset.aboveBottom(64)),
                HeightRangePlacementModifier.uniform(YOffset.aboveBottom(32), YOffset.belowTop(256)),
                CountPlacementModifier.of(40));

        registerAndConfigureOreFeature("dd_shimmerstone_ore",
                new OreFeatureConfig(ImmutableList.of(
                        OreFeatureConfig.createTarget(BLACKSLAG_ORE_REPLACEABLE_TEST, SpectrumBlocks.BLACKSLAG_SHIMMERSTONE_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(STONE_ORE_REPLACEABLES, SpectrumBlocks.SHIMMERSTONE_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(DEEPSLATE_ORE_REPLACEABLES, SpectrumBlocks.DEEPSLATE_SHIMMERSTONE_ORE.getDefaultState())
                ), 7),
                HeightRangePlacementModifier.uniform(YOffset.aboveBottom(16), YOffset.belowTop(128)),
                CountPlacementModifier.of(24));
    }

    public static void registerVanillaOres() {
        registerAndConfigureOreFeature("dd_iron_ore",
                new OreFeatureConfig(ImmutableList.of(
                        OreFeatureConfig.createTarget(BLACKSLAG_ORE_REPLACEABLE_TEST, SpectrumBlocks.BLACKSLAG_IRON_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(STONE_ORE_REPLACEABLES, Blocks.IRON_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_IRON_ORE.getDefaultState())
                ), 25),
                HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(192), YOffset.aboveBottom(384)),
                CountPlacementModifier.of(20));

        registerAndConfigureOreFeature("dd_iron_ore2",
                new OreFeatureConfig(ImmutableList.of(
                        OreFeatureConfig.createTarget(BLACKSLAG_ORE_REPLACEABLE_TEST, SpectrumBlocks.BLACKSLAG_IRON_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(STONE_ORE_REPLACEABLES, Blocks.IRON_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_IRON_ORE.getDefaultState())
                ), 25),
                HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(4), YOffset.belowTop(4)),
                CountPlacementModifier.of(10));

        registerAndConfigureOreFeature("dd_gold_ore",
                new OreFeatureConfig(ImmutableList.of(
                        OreFeatureConfig.createTarget(BLACKSLAG_ORE_REPLACEABLE_TEST, SpectrumBlocks.BLACKSLAG_GOLD_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(STONE_ORE_REPLACEABLES, Blocks.GOLD_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_GOLD_ORE.getDefaultState())
                ), 15),
                HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(128), YOffset.aboveBottom(256)),
                CountPlacementModifier.of(20));

        registerAndConfigureOreFeature("dd_gold_ore2",
                new OreFeatureConfig(ImmutableList.of(
                        OreFeatureConfig.createTarget(BLACKSLAG_ORE_REPLACEABLE_TEST, SpectrumBlocks.BLACKSLAG_GOLD_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(STONE_ORE_REPLACEABLES, Blocks.GOLD_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_GOLD_ORE.getDefaultState())
                ), 15),
                HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(4), YOffset.belowTop(4)),
                CountPlacementModifier.of(8));

        registerAndConfigureOreFeature("dd_diamond_ore",
                new OreFeatureConfig(ImmutableList.of(
                        OreFeatureConfig.createTarget(BLACKSLAG_ORE_REPLACEABLE_TEST, SpectrumBlocks.BLACKSLAG_DIAMOND_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(STONE_ORE_REPLACEABLES, Blocks.DIAMOND_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_DIAMOND_ORE.getDefaultState())
                ), 9),
                HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(0), YOffset.aboveBottom(256)),
                CountPlacementModifier.of(3));

        registerAndConfigureOreFeature("dd_emerald_ore",
                new OreFeatureConfig(ImmutableList.of(
                        OreFeatureConfig.createTarget(DEEPSLATE_ORE_REPLACEABLES, Blocks.DEEPSLATE_EMERALD_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(BLACKSLAG_ORE_REPLACEABLE_TEST, SpectrumBlocks.BLACKSLAG_EMERALD_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(STONE_ORE_REPLACEABLES, Blocks.EMERALD_ORE.getDefaultState())
                ), 18),
                HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(4), YOffset.aboveBottom(128)),
                RarityFilterPlacementModifier.of(3));
    }

    public static void registerGeodes() {
        Identifier MOONSTONE_GEODE_IDENTIFIER = SpectrumCommon.locate("moonstone_geode");
        registerConfiguredAndPlacedFeature(
                MOONSTONE_GEODE_IDENTIFIER,
                DDConfiguredFeatures.MOONSTONE_GEODE,
                RarityFilterPlacementModifier.of(SpectrumCommon.CONFIG.MoonstoneGeodeChunkChance),
                HeightRangePlacementModifier.uniform(YOffset.aboveBottom(16), YOffset.aboveBottom(128)),
                SquarePlacementModifier.of(),
                BiomePlacementModifier.of()
        );
    }

    public static void registerDecorators() {
        Identifier id = SpectrumCommon.locate("dd_tuff");
        registerConfiguredAndPlacedFeature(id, DDConfiguredFeatures.TUFF_DISK, HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.getTop()), CountPlacementModifier.of(32));

        id = SpectrumCommon.locate("dd_gravel");
        registerConfiguredAndPlacedFeature(id, DDConfiguredFeatures.GRAVEL_DISK, HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.getTop()), CountPlacementModifier.of(32));

        id = SpectrumCommon.locate("dd_infested");
        registerPlacedFeature(id, OreConfiguredFeatures.ORE_INFESTED, HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.getTop()), CountPlacementModifier.of(32));

        id = SpectrumCommon.locate("dd_granite");
        registerConfiguredAndPlacedFeature(id, DDConfiguredFeatures.GRANITE_DISK, HeightRangePlacementModifier.uniform(YOffset.belowTop(192), YOffset.belowTop(64)), CountPlacementModifier.of(8));

        id = SpectrumCommon.locate("dd_diorite");
        registerConfiguredAndPlacedFeature(id, DDConfiguredFeatures.DIORITE_DISK, HeightRangePlacementModifier.uniform(YOffset.belowTop(128), YOffset.getTop()), CountPlacementModifier.of(8));

        id = SpectrumCommon.locate("dd_andesite");
        registerConfiguredAndPlacedFeature(id, DDConfiguredFeatures.ANDESITE_DISK, HeightRangePlacementModifier.uniform(YOffset.belowTop(128), YOffset.getTop()), CountPlacementModifier.of(8));

        id = SpectrumCommon.locate("dd_downstone_disk");
        registerConfiguredAndPlacedFeature(id, DDConfiguredFeatures.DOWNSTONE_DISK, HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.aboveBottom(92)), CountPlacementModifier.of(16));

        id = SpectrumCommon.locate("dd_downstone_sloped");
        registerConfiguredAndPlacedFeature(id, DDConfiguredFeatures.DOWNSTONE_DISK_SLOPED, HeightRangePlacementModifier.of(VeryBiasedToBottomHeightProvider.create(YOffset.getBottom(), YOffset.aboveBottom(50), 24)), CountPlacementModifier.of(16));

        id = SpectrumCommon.locate("dd_stone");
        registerConfiguredAndPlacedFeature(id, DDConfiguredFeatures.STONE_DISK, HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.getTop()), CountPlacementModifier.of(32));

        id = SpectrumCommon.locate("dd_springs");
        registerConfiguredAndPlacedFeature(id, DDConfiguredFeatures.WATER_SPRING, HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.getTop()), CountPlacementModifier.of(256));

        id = SpectrumCommon.locate("dd_lichen");
        registerConfiguredAndPlacedFeature(id, DDConfiguredFeatures.GLOW_LICHEN, HeightRangePlacementModifier.uniform(YOffset.getBottom(), YOffset.getTop()), CountPlacementModifier.of(UniformIntProvider.create(148, 240)), SquarePlacementModifier.of(), SurfaceThresholdFilterPlacementModifier.of(Heightmap.Type.OCEAN_FLOOR_WG, -2147483648, -64));

        id = SpectrumCommon.locate("dd_bismuth");
        registerConfiguredAndPlacedFeature(id, DDConfiguredFeatures.BISMUTH_BUDS, HeightRangePlacementModifier.uniform(YOffset.aboveBottom(32), YOffset.aboveBottom(128)), CountPlacementModifier.of(UniformIntProvider.create(64, 96)), SquarePlacementModifier.of());
    }

}
