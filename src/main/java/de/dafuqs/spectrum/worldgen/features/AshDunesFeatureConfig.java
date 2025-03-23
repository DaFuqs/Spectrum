package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.util.math.floatprovider.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.world.gen.feature.*;

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
