package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.List;
import java.util.Random;

public class WeightedRandomFeatureConfig implements FeatureConfig {
	
	public static final Codec<WeightedRandomFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(
				Codecs.nonEmptyList(PlacedFeature.CODEC.listOf()).fieldOf("features").forGetter((weightedRandomFeatureConfig) -> {
					return weightedRandomFeatureConfig.features;
				}), Codecs.nonEmptyList(Codecs.POSITIVE_INT.listOf()).fieldOf("weights").forGetter((weightedRandomFeatureConfig) -> {
					return weightedRandomFeatureConfig.weights;
				})).apply(instance, WeightedRandomFeatureConfig::new);
	});
	public final List<PlacedFeature> features;
	public final List<Integer> weights;
	public final int weightSum;
	
	public WeightedRandomFeatureConfig(List<PlacedFeature> features, List<Integer> weights) {
		this.features = features;
		this.weights = weights;
		int sum = 0;
		for (int i : weights) {
			sum += i;
		}
		this.weightSum = sum;
	}
	
	public PlacedFeature getWeightedRandomFeature(Random random) {
		int r = random.nextInt(weightSum);
		int i = 0;
		int currWeight = 0;
		
		while (true) {
			currWeight += weights.get(i);
			
			if (currWeight > r) {
				return features.get(i);
			}
			
			i++;
		}
		
	}
	
}