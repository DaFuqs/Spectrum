package de.dafuqs.spectrum.render.biome_rendering;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.*;

public record EnvironmentalData(float darkening, float brightnessMultiplier, float nearFogDistanceMultiplier, float farFogMultiplier) {
	
	public static final EnvironmentalData NOOP = new EnvironmentalData(0F, 1F, 1F, 1F);
	
	public static final Codec<EnvironmentalData> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.FLOAT.fieldOf("darkening").forGetter(EnvironmentalData::darkening),
			ExtraCodecs.POSITIVE_FLOAT.fieldOf("brightness_multiplier").forGetter(EnvironmentalData::brightnessMultiplier),
			Codec.FLOAT.fieldOf("fog_near_multiplier").forGetter(EnvironmentalData::nearFogDistanceMultiplier),
			Codec.FLOAT.fieldOf("fog_far_multiplier").forGetter(EnvironmentalData::farFogMultiplier)
	).apply(i, EnvironmentalData::new));
	
	public static EnvironmentalData fromArray(float[] data) {
		return new EnvironmentalData(data[0], data[1], data[2], data[3]);
	}
	
	public float[] asArray() {
		return new float[]{darkening, brightnessMultiplier, nearFogDistanceMultiplier, farFogMultiplier};
	}
}