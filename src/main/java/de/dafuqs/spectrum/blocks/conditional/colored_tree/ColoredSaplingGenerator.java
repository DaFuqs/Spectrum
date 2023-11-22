package de.dafuqs.spectrum.blocks.conditional.colored_tree;

import de.dafuqs.spectrum.*;
import net.minecraft.block.sapling.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.gen.feature.*;
import org.jetbrains.annotations.*;

public class ColoredSaplingGenerator extends SaplingGenerator {
	
	private final RegistryKey<ConfiguredFeature<?, ?>> treeFeature;
	
	public ColoredSaplingGenerator(DyeColor dyeColor) {
		this.treeFeature = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, SpectrumCommon.locate("colored_trees/" + dyeColor.toString()));
	}
	
	@Nullable
	@Override
	protected RegistryKey<ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
		return treeFeature;
	}
	
}
