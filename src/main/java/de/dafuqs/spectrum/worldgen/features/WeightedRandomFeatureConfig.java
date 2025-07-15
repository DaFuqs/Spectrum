package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.util.random.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.placement.*;

public record WeightedRandomFeatureConfig(SimpleWeightedRandomList<PlacedFeature> features) implements FeatureConfiguration {
	
	public static final Codec<WeightedRandomFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			SimpleWeightedRandomList.wrappedCodec(PlacedFeature.DIRECT_CODEC).fieldOf("features").forGetter((weightedRandomFeatureConfig) -> weightedRandomFeatureConfig.features)
	).apply(instance, WeightedRandomFeatureConfig::new));
	
}