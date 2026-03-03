package de.dafuqs.spectrum.render.biome_rendering;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.config.*;
import net.minecraft.util.*;

public record EnvironmentalData(float environmentalLightingMultiplier, float fogBrightnessMultiplier, float nearFogDistanceMultiplier, float farFogDistanceMultiplier) {
	
	public static final EnvironmentalData NOOP = new EnvironmentalData(1F, 1F, 1F, 1F);
	
	public static final Codec<EnvironmentalData> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.FLOAT.fieldOf("environmental_lighting_multiplier").forGetter(EnvironmentalData::environmentalLightingMultiplier),
			ExtraCodecs.POSITIVE_FLOAT.fieldOf("fog_brightness_multiplier").forGetter(EnvironmentalData::fogBrightnessMultiplier),
			Codec.FLOAT.fieldOf("fog_near_multiplier").forGetter(EnvironmentalData::nearFogDistanceMultiplier),
			Codec.FLOAT.fieldOf("fog_far_multiplier").forGetter(EnvironmentalData::farFogDistanceMultiplier)
	).apply(i, EnvironmentalData::new));
	
	public static EnvironmentalData fromArray(float[] data) {
		return new EnvironmentalData(data[0], data[1], data[2], data[3]);
	}
	
	public float[] asArray() {
		return new float[]{environmentalLightingMultiplier, fogBrightnessMultiplier, nearFogDistanceMultiplier, farFogDistanceMultiplier};
	}
	
	public float environmentalLightingMultiplier() {
		 return (float) Mth.lerp(SpectrumConfig.CONFIG.DimensionBrightnessMod.get(), this.environmentalLightingMultiplier, 1.0);
	}
	
}