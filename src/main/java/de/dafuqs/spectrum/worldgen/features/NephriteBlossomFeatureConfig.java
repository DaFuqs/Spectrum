package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureConfig;

public record NephriteBlossomFeatureConfig(boolean flowering) implements FeatureConfig {
    public static final Codec<NephriteBlossomFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.BOOL.fieldOf("flowering").forGetter(NephriteBlossomFeatureConfig::flowering)
    ).apply(instance, NephriteBlossomFeatureConfig::new));
}
