package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.world.gen.feature.*;

public record NephriteBlossomFeatureConfig(Boolean flowering) implements FeatureConfig {
	public static final Codec<NephriteBlossomFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			Codec.BOOL.fieldOf("flowering").forGetter(NephriteBlossomFeatureConfig::flowering)
	).apply(instance, NephriteBlossomFeatureConfig::new));
}
