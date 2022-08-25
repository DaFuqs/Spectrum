package de.dafuqs.spectrum.worldgen;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class OminousSaplingGenerator extends SaplingGenerator {
	
	@Override
	protected RegistryEntry<? extends ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
		return null;
	}
	
}
