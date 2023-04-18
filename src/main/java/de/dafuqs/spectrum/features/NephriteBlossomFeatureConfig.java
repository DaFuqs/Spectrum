package de.dafuqs.spectrum.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.world.gen.feature.*;

public record NephriteBlossomFeatureConfig(boolean flowering) implements FeatureConfig {
    public static final Codec<NephriteBlossomFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.BOOL.fieldOf("flowering").forGetter(NephriteBlossomFeatureConfig::flowering)
    ).apply(instance, NephriteBlossomFeatureConfig::new));
}
