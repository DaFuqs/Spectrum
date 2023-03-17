package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureConfig;

public record JadeiteLotusFeatureConfig(boolean inverted) implements FeatureConfig {
    public static final Codec<JadeiteLotusFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.BOOL.fieldOf("inverted").forGetter(JadeiteLotusFeatureConfig::inverted)
    ).apply(instance, JadeiteLotusFeatureConfig::new));
}
