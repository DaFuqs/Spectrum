package de.dafuqs.spectrum.colored_trees;

import de.dafuqs.spectrum.worldgen.SpectrumFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.DyeColor;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ColoredSaplingGenerator extends SaplingGenerator {

    DyeColor dyeColor;
    
    public ColoredSaplingGenerator(DyeColor dyeColor) {
        this.dyeColor = dyeColor;
    }

    @Nullable
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bl) {
        return getConfiguredFeature(this.dyeColor);
    }
    
    private ConfiguredFeature<TreeFeatureConfig, ?> getConfiguredFeature(DyeColor dyeColor) {
        switch (dyeColor) {
            case RED:
                return SpectrumFeatures.RED_TREE;
            case BROWN:
                return SpectrumFeatures.BROWN_TREE;
            case CYAN:
                return SpectrumFeatures.CYAN_TREE;
            case GRAY:
                return SpectrumFeatures.GRAY_TREE;
            case GREEN:
                return SpectrumFeatures.GREEN_TREE;
            case LIGHT_BLUE:
                return SpectrumFeatures.LIGHT_BLUE_TREE;
            case LIGHT_GRAY:
                return SpectrumFeatures.LIGHT_GRAY_TREE;
            case BLUE:
                return SpectrumFeatures.BLUE_TREE;
            case LIME:
                return SpectrumFeatures.LIME_TREE;
            case ORANGE:
                return SpectrumFeatures.ORANGE_TREE;
            case PINK:
                return SpectrumFeatures.PINK_TREE;
            case PURPLE:
                return SpectrumFeatures.PURPLE_TREE;
            case WHITE:
                return SpectrumFeatures.WHITE_TREE;
            case YELLOW:
                return SpectrumFeatures.YELLOW_TREE;
            case BLACK:
                return SpectrumFeatures.BLACK_TREE;
            case MAGENTA:
                return SpectrumFeatures.MAGENTA_TREE;
            default:
                return null;
        }
    }

}
