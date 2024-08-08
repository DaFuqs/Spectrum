package de.dafuqs.spectrum.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;

public record TriStateVineFeatureConfig(Block vineBlock, int cutoff, IntProvider minHeight, FloatProvider overgrowth, float berryChance) implements FeatureConfig {
	public static final Codec<TriStateVineFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			Registries.BLOCK.getCodec().fieldOf("vine_block").forGetter(TriStateVineFeatureConfig::vineBlock),
			Codec.INT.fieldOf("cutoff").forGetter(TriStateVineFeatureConfig::cutoff),
			IntProvider.POSITIVE_CODEC.fieldOf("minimum_height").forGetter(TriStateVineFeatureConfig::minHeight),
			FloatProvider.createValidatedCodec(0, 1).fieldOf("overgrowth").forGetter(TriStateVineFeatureConfig::overgrowth),
			Codec.FLOAT.fieldOf("berry_chance").forGetter(TriStateVineFeatureConfig::berryChance)
	).apply(instance, TriStateVineFeatureConfig::new));
}
