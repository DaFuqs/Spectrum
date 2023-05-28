package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.sapling.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.gen.feature.*;
import org.jetbrains.annotations.*;

public class ColoredSaplingGenerator extends SaplingGenerator {
	
	private final DyeColor dyeColor;
	
	public ColoredSaplingGenerator(DyeColor dyeColor) {
		this.dyeColor = dyeColor;
	}
	
	@Nullable
	@Override
	protected RegistryKey<ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
		return SpectrumWorldgen.CONFIGURED_FEATURE_KEYS.get(dyeColor);
	}
	
}
