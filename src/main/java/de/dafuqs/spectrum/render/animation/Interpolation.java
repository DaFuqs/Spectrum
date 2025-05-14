package de.dafuqs.spectrum.render.animation;

import net.minecraft.util.*;

@FunctionalInterface
public interface Interpolation {
	Interpolation LINEAR = (start, end, delta) -> Mth.lerp(delta, start, end);
	Interpolation EASE_IN = (start, end, delta) -> Mth.lerp(Math.pow(delta, 2), start, end);
	Interpolation EASE_OUT = (start, end, delta) -> Mth.lerp(Math.pow(delta, 0.5), start, end);
	Interpolation CLAMPED = Mth::clampedLerp;
	// You know, it is kind of fucked up that clamped lerp and lerp don't have the same signature
	
	double apply(double start, double end, float delta);
}
