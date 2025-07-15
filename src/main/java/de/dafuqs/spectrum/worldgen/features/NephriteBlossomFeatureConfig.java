package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;

public record NephriteBlossomFeatureConfig(Boolean flowering) implements FeatureConfiguration {
	public static final Codec<NephriteBlossomFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			Codec.BOOL.fieldOf("flowering").forGetter(NephriteBlossomFeatureConfig::flowering)
	).apply(instance, NephriteBlossomFeatureConfig::new));
}
