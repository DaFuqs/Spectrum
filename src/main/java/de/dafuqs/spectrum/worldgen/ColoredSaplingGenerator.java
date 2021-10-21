package de.dafuqs.spectrum.worldgen;

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
	
	private ConfiguredFeature<TreeFeatureConfig, ?> getConfiguredFeature(DyeColor dyeColor) {
		return SpectrumConfiguredFeatures.COLORED_TREE_FEATURES.get(dyeColor);
	}

	@Nullable
	@Override
	protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean bees) {
		return getConfiguredFeature(this.dyeColor);
	}
}
