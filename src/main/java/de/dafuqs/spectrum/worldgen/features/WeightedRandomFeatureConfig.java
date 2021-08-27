package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FeatureConfig;

import java.util.stream.Stream;

public class WeightedRandomFeatureConfig implements FeatureConfig {

	public static final Codec<WeightedRandomFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(
			WeightedRandomConfig.CODEC.fieldOf("weighted_random_config").forGetter((weightedRandomFeatureConfig) -> {
			return weightedRandomFeatureConfig.weightedRandomConfig;
		})).apply(instance, WeightedRandomFeatureConfig::new);
	});

	public final WeightedRandomConfig weightedRandomConfig;

	public WeightedRandomFeatureConfig(WeightedRandomConfig weightedRandomConfig) {
		this.weightedRandomConfig = weightedRandomConfig;
	}

	public Stream<ConfiguredFeature<?, ?>> getDecoratedFeatures() {
		return this.weightedRandomConfig.features.stream();
	}

}