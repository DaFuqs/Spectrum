package de.dafuqs.spectrum.blocks.deeper_down.flora;

import de.dafuqs.spectrum.*;
import net.minecraft.block.sapling.*;
import net.minecraft.registry.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.gen.feature.*;
import org.jetbrains.annotations.*;

public class WeepingGalaSaplingGenerator extends SaplingGenerator {
	
	private final RegistryKey<ConfiguredFeature<?, ?>> treeFeature;
	
	public WeepingGalaSaplingGenerator() {
		this.treeFeature = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, SpectrumCommon.locate("weeping_gala"));
	}
	
	@Nullable
	@Override
	protected RegistryKey<ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
		return treeFeature;
	}
	
}
