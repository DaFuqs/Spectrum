package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.conditional.colored_tree.ColoredLeavesBlock;
import de.dafuqs.spectrum.blocks.conditional.colored_tree.ColoredLogBlock;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.HashMap;
import java.util.Map;

public class SpectrumWorldgen {

    public static Map<DyeColor, RegistryKey<ConfiguredFeature<?, ?>>> CONFIGURED_FEATURE_KEYS = new HashMap<>();
    public static Map<DyeColor, RegistryKey<PlacedFeature>> PLACED_FEATURE_KEYS = new HashMap<>();

    public static void init() {

    }

    public static void bootstrapConfiguredFeatures(Registerable<ConfiguredFeature<?, ?>> featureRegisterable) {
        for (DyeColor dyeColor : DyeColor.values()) {
            Identifier identifier = SpectrumCommon.locate("colored_trees/" + dyeColor + "_tree");

            TreeFeatureConfig treeFeatureConfig = new TreeFeatureConfig.Builder(
                    BlockStateProvider.of(ColoredLogBlock.byColor(dyeColor).getDefaultState()),
                    new StraightTrunkPlacer(4, 2, 2), // 4-8 height
                    BlockStateProvider.of(ColoredLeavesBlock.byColor(dyeColor).getDefaultState()),
                    new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
                    new TwoLayersFeatureSize(1, 0, 1)
            ).ignoreVines().build();

            ConfiguredFeatures.register(
                    featureRegisterable,
                    RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, identifier),
                    Feature.TREE,
                    treeFeatureConfig
            );
            CONFIGURED_FEATURE_KEYS.put(dyeColor, RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, identifier));
        }


    }

    public static void bootstrapPlacedFeatures(Registerable<PlacedFeature> featureRegisterable) {
        RegistryEntryLookup<ConfiguredFeature<?, ?>> featureLookup = featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        for (DyeColor dyeColor : DyeColor.values()) {
            Identifier identifier = SpectrumCommon.locate("colored_trees/" + dyeColor + "_tree");

            PlacedFeatures.register(
                    featureRegisterable,
                    RegistryKey.of(RegistryKeys.PLACED_FEATURE, identifier),
                    featureLookup.getOrThrow(RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, identifier)),
                    BiomePlacementModifier.of()
            );
            PLACED_FEATURE_KEYS.put(dyeColor, RegistryKey.of(RegistryKeys.PLACED_FEATURE, identifier));
        }
    }
}
