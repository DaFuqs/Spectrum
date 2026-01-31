package de.dafuqs.spectrum.render.animation;

import net.minecraft.util.*;

import java.util.function.*;

@FunctionalInterface
public interface Interpolation {
	Interpolation LINEAR = (start, end, delta) -> Mth.lerp(delta, start, end);
	Interpolation EASE_IN = (start, end, delta) -> Mth.lerp(Math.pow(delta, 2), start, end);
	Interpolation EASE_OUT = (start, end, delta) -> Mth.lerp(Math.pow(delta, 0.5), start, end);
	Interpolation CLAMPED = Mth::clampedLerp;
	Interpolation CUBIC_IN = normalize(delta -> Math.pow(delta, 3));
	Interpolation CUBIC_OUT = normalize(delta -> 1 - Math.pow(1 - delta, 3));
	
	static Interpolation normalize(Function<Float, Number> rawDelta) {
		return (start, end, delta) -> Mth.lerp(rawDelta.apply(delta).floatValue(), start, end);
	}
	
	double apply(double start, double end, float delta);
}
