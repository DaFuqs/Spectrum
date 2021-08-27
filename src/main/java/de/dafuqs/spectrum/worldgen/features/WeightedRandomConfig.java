package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.List;
import java.util.Random;

public class WeightedRandomConfig {

	public final List<ConfiguredFeature<?, ?>> features;
	public final List<Integer> weights;
	public final int weightSum;

	public static final Codec<WeightedRandomConfig> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(
			Codecs.nonEmptyList(ConfiguredFeature.CODEC.listOf()).fieldOf("features").forGetter((weightedRandomConfig) -> {
			return weightedRandomConfig.features;
		}), Codecs.nonEmptyList(Codecs.POSITIVE_INT.listOf()).fieldOf("weights").forGetter((weightedRandomConfig) -> {
			return weightedRandomConfig.weights;
		})).apply(instance, WeightedRandomConfig::new);
	});

	public WeightedRandomConfig(List<ConfiguredFeature<?, ?>> features, List<Integer> weights) {
		this.features = features;
		this.weights = weights;
		int sum = 0;
		for(int i : weights) {
			sum += i;
		}
		this.weightSum = sum;
	}

	public ConfiguredFeature<?, ?> getEntryWithWeight(Random random) {
		int r = random.nextInt(weightSum);
		int i = 0;
		int currWeight = 0;

		while(true) {
			currWeight += weights.get(i);

			if(currWeight > r) {
				return features.get(i);
			}

			i++;
		}

	}

}