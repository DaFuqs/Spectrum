package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.world.gen.feature.*;

public record JadeiteLotusFeatureConfig(Boolean inverted) implements FeatureConfig {
	public static final Codec<JadeiteLotusFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			Codec.BOOL.fieldOf("inverted").forGetter(JadeiteLotusFeatureConfig::inverted)
	).apply(instance, JadeiteLotusFeatureConfig::new));
}
