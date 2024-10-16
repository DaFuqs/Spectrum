package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.block.*;
import net.minecraft.registry.*;
import net.minecraft.util.math.floatprovider.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.world.gen.feature.*;

public record TriStateVineFeatureConfig(Block vineBlock, int cutoff, IntProvider minHeight, FloatProvider overgrowth, float berryChance) implements FeatureConfig {
	public static final Codec<TriStateVineFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			Registries.BLOCK.getCodec().fieldOf("vine_block").forGetter(TriStateVineFeatureConfig::vineBlock),
			Codec.INT.fieldOf("cutoff").forGetter(TriStateVineFeatureConfig::cutoff),
			IntProvider.POSITIVE_CODEC.fieldOf("minimum_height").forGetter(TriStateVineFeatureConfig::minHeight),
			FloatProvider.createValidatedCodec(0, 1).fieldOf("overgrowth").forGetter(TriStateVineFeatureConfig::overgrowth),
			Codec.FLOAT.fieldOf("berry_chance").forGetter(TriStateVineFeatureConfig::berryChance)
	).apply(instance, TriStateVineFeatureConfig::new));
}
