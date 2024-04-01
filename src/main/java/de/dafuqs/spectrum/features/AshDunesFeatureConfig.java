package de.dafuqs.spectrum.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;

public record AshDunesFeatureConfig(
        IntProvider nodeSpread,
        IntProvider nodeQuantity,
        IntProvider cutoutQuantity,
        FloatProvider emitterStrength,
        float emitterDecayModifier,
        float emitterCutoutModifier) implements FeatureConfig {

    public static final Codec<AshDunesFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            IntProvider.POSITIVE_CODEC.fieldOf("node_spread").forGetter((AshDunesFeatureConfig::nodeSpread)),
            IntProvider.POSITIVE_CODEC.fieldOf("node_quantity").forGetter(AshDunesFeatureConfig::nodeQuantity),
            IntProvider.POSITIVE_CODEC.fieldOf("cutout_quantity").forGetter(AshDunesFeatureConfig::cutoutQuantity),
            FloatProvider.createValidatedCodec(0F, 256F).fieldOf("emitter_strength").forGetter(AshDunesFeatureConfig::emitterStrength),
            Codecs.POSITIVE_FLOAT.fieldOf("emitter_decay_modifier").forGetter(AshDunesFeatureConfig::emitterDecayModifier),
            Codecs.POSITIVE_FLOAT.fieldOf("emitter_cutout_modifier").forGetter(AshDunesFeatureConfig::emitterCutoutModifier)
    ).apply(instance, AshDunesFeatureConfig::new));
}
