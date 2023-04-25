package de.dafuqs.spectrum.particle;

import net.minecraft.util.math.*;

import java.util.*;

public enum VectorPattern {
	
	FOUR(List.of(
			new Vec3d(1.0D, 0, 0.0D),
			new Vec3d(0.0D, 0, 1.0D),
			new Vec3d(-1.0, 0, 0.0D),
			new Vec3d(0.0D, 0, -1.0D)
	)),
	EIGHT(List.of(
			new Vec3d(1.0D, 0, 0.0D),
			new Vec3d(0.7D, 0, 0.7D),
			new Vec3d(0.0D, 0, 1.0D),
			new Vec3d(-0.7D, 0, 0.7D),
			new Vec3d(-1.0D, 0, 0.0D),
			new Vec3d(-0.7D, 0, -0.7D),
			new Vec3d(0.0D, 0, -1.0D),
			new Vec3d(0.7D, 0, -0.7D)
	)),
	EIGHT_OFFSET(List.of( // Like eight, just turned clockwise
			new Vec3d(0.75D, 0, 0.5D),
			new Vec3d(0.5D, 0, 0.75D),
			new Vec3d(-0.5D, 0, 0.75D),
			new Vec3d(-0.75D, 0, 0.5D),
			new Vec3d(-0.75D, 0, 0.5D),
			new Vec3d(-0.5D, 0, -0.75D),
			new Vec3d(0.5D, 0, -0.75D),
			new Vec3d(0.75D, 0, -0.5D)
	)),
	SIXTEEN(List.of(
			new Vec3d(1.0D, 0, 0.0D),
			new Vec3d(0.75D, 0, 0.5D),
			new Vec3d(0.7D, 0, 0.7D),
			new Vec3d(0.5D, 0, 0.75D),
			new Vec3d(0.0D, 0, 1.0D),
			new Vec3d(-0.5D, 0, 0.75D),
			new Vec3d(-0.7D, 0, 0.7D),
			new Vec3d(-0.75D, 0, 0.5D),
			new Vec3d(-1.0D, 0, 0.0D),
			new Vec3d(-0.75D, 0, 0.5D),
			new Vec3d(-0.7D, 0, -0.7D),
			new Vec3d(-0.5D, 0, -0.75D),
			new Vec3d(0.0D, 0, -1.0D),
			new Vec3d(0.5D, 0, -0.75D),
			new Vec3d(0.7D, 0, -0.7D),
			new Vec3d(0.75D, 0, -0.5D)
	));
	
	private final List<Vec3d> v;
	
	VectorPattern(List<Vec3d> vectors) {
		v = vectors;
	}
	
	public List<Vec3d> getVectors() {
		return v;
	}
	
}
