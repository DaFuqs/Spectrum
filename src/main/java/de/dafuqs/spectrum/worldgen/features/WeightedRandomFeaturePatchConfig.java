package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.gen.feature.FeatureConfig;

public record WeightedRandomFeaturePatchConfig(int tries, int xzSpread, int ySpread,
                                               WeightedRandomFeatureConfig weightedRandomFeatureConfig) implements FeatureConfig {
	
	public static final Codec<WeightedRandomFeaturePatchConfig> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(
				Codecs.POSITIVE_INT.fieldOf("tries").forGetter((weightedRandomFeatureConfig) -> {
					return weightedRandomFeatureConfig.tries;
				}),
				Codecs.POSITIVE_INT.fieldOf("xzspread").forGetter((weightedRandomFeatureConfig) -> {
					return weightedRandomFeatureConfig.xzSpread;
				}),
				Codecs.POSITIVE_INT.fieldOf("yspread").forGetter((weightedRandomFeatureConfig) -> {
					return weightedRandomFeatureConfig.ySpread;
				}),
				WeightedRandomFeatureConfig.CODEC.fieldOf("feature").forGetter(WeightedRandomFeaturePatchConfig::featureConfig)
		).apply(instance, WeightedRandomFeaturePatchConfig::new);
	});
	
	public WeightedRandomFeaturePatchConfig(int tries, int xzSpread, int ySpread, WeightedRandomFeatureConfig weightedRandomFeatureConfig) {
		this.tries = tries;
		this.xzSpread = xzSpread;
		this.ySpread = ySpread;
		this.weightedRandomFeatureConfig = weightedRandomFeatureConfig;
	}
	
	public WeightedRandomFeatureConfig featureConfig() {
		return this.weightedRandomFeatureConfig;
	}
	
}