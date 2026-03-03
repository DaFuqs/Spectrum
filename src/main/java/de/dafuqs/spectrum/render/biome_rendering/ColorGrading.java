package de.dafuqs.spectrum.render.biome_rendering;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.*;

import java.util.*;

import static net.minecraft.util.Mth.lerp;

public record ColorGrading(float saturation, float rubedo, float colorTemperature, float threshold, float bloom) {
	
	public static final float[] GRADING_OUT = new float[5];
	
	public static ColorGrading DEFAULT = new ColorGrading(1.0F, 0.0F, 65, 0.85F, 0.35F);
	
	public static final Codec<ColorGrading> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.FLOAT.fieldOf("saturation").forGetter(ColorGrading::saturation),
			Codec.FLOAT.fieldOf("rubedo").forGetter(ColorGrading::rubedo),
			Codec.FLOAT.fieldOf("temperature").forGetter(ColorGrading::colorTemperature),
			Codec.FLOAT.fieldOf("threshold").forGetter(ColorGrading::threshold),
			Codec.FLOAT.fieldOf("bloom").forGetter(ColorGrading::bloom)
	).apply(i, ColorGrading::new));
	
	public static void update(float[] old, float[] current, float delta) {
		for (int i = 0; i < 5; i++) {
			GRADING_OUT[i] = lerp(delta, old[i], current[i]);
		}
	}
	
	public float[] asArray() {
		return new float[]{saturation, rubedo, colorTemperature, threshold, bloom};
	}
	
}