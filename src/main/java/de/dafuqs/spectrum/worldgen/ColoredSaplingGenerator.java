package de.dafuqs.spectrum.worldgen;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.DyeColor;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ColoredSaplingGenerator extends SaplingGenerator {
	
	private final DyeColor dyeColor;
	
	public ColoredSaplingGenerator(DyeColor dyeColor) {
		this.dyeColor = dyeColor;
	}
	
	@Nullable
	@Override
	protected RegistryEntry<? extends ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
		return SpectrumConfiguredFeatures.COLORED_TREE_CONFIGURED_FEATURES.get(dyeColor);
	}
	
}
