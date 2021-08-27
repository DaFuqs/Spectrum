package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.stream.Stream;

public class WeightedRandomFeaturePatchConfig extends WeightedRandomFeatureConfig {

	public static final Codec<WeightedRandomFeaturePatchConfig> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(
			WeightedRandomConfig.CODEC.fieldOf("weighted_random_config").forGetter((weightedRandomFeatureConfig) -> {
			return weightedRandomFeatureConfig.weightedRandomConfig;
		}),
			Codecs.POSITIVE_INT.fieldOf("tries").forGetter((weightedRandomFeatureConfig) -> {
			return weightedRandomFeatureConfig.tries;
		}),
		Codecs.POSITIVE_INT.fieldOf("xspread").forGetter((weightedRandomFeatureConfig) -> {
			return weightedRandomFeatureConfig.spreadX;
		}),
		Codecs.POSITIVE_INT.fieldOf("yspread").forGetter((weightedRandomFeatureConfig) -> {
			return weightedRandomFeatureConfig.spreadY;
		}),
		Codecs.POSITIVE_INT.fieldOf("zspread").forGetter((weightedRandomFeatureConfig) -> {
			return weightedRandomFeatureConfig.spreadZ;
		})
		).apply(instance, WeightedRandomFeaturePatchConfig::new);
	});

	public final int tries;
	public final int spreadX;
	public final int spreadY;
	public final int spreadZ;

	public WeightedRandomFeaturePatchConfig(WeightedRandomConfig weightedRandomConfig, int tries, int spreadX, int spreadY, int spreadZ) {
		super(weightedRandomConfig);
		this.tries = tries;
		this.spreadX = spreadX;
		this.spreadY = spreadY;
		this.spreadZ = spreadZ;
	}

	public Stream<ConfiguredFeature<?, ?>> getDecoratedFeatures() {
		return this.weightedRandomConfig.features.stream();
	}

}