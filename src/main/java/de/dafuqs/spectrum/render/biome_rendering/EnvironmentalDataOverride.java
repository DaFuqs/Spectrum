package de.dafuqs.spectrum.render.biome_rendering;

import de.dafuqs.spectrum.helpers.*;
import net.minecraft.world.entity.*;
import org.apache.commons.lang3.*;
import org.joml.*;

import java.util.*;
import java.util.function.*;

public record EnvironmentalDataOverride(Predicate<Entity> predicate, ColorData color, EnvironmentalData dataOverride, int priority) {
	
	public record ColorData(Vector3f colorMod, float blend) {
		
		public ColorData(int colorMod, float blend) {
			this(SpectrumColorHelper.colorIntToVec(colorMod), blend);
		}
		
		public boolean isBlank() {
			return this == BLANK;
		}
	}
	
	private static final List<EnvironmentalDataOverride> OVERRIDES = new ArrayList<>();
	public static final ColorData BLANK = new ColorData(new Vector3f(), 0);
	public static final EnvironmentalDataOverride INACTIVE = new EnvironmentalDataOverride(null, BLANK, EnvironmentalData.NOOP, -999);
	
	public static final EnvironmentalData NOOP = new EnvironmentalData(0F, 0, 0, 0);
	
	public static EnvironmentalDataOverride fromArray(float[] override) {
		return new EnvironmentalDataOverride(null,
				new ColorData(new Vector3f(override[1], override[2], override[3]), override[0]),
				new EnvironmentalData(override[4], override[5], override[6], override[7]),
				-999
		);
	}
	
	public float[] asArray() {
		Vector3f color = color().colorMod;
		return ArrayUtils.addAll(new float[]{color().blend, color.x, color.y, color.z}, dataOverride.asArray());
	}
	
	public static void register(EnvironmentalDataOverride override) {
		OVERRIDES.add(override);
	}
	
	public static EnvironmentalDataOverride get(Entity camera) {
		EnvironmentalDataOverride effect = INACTIVE;
		
		for (EnvironmentalDataOverride override : OVERRIDES) {
			if (!override.predicate.test(camera))
				continue;
			
			if (effect == INACTIVE) {
				effect = override;
				continue;
			}
			
			if (override.priority > effect.priority)
				effect = override;
		}
		
		return effect;
	}
	
}