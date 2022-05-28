package de.dafuqs.spectrum.particle;

import de.dafuqs.spectrum.helpers.Support;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public enum ParticlePattern {
	
	FOUR(Support.VECTORS_4),
	EIGHT(Support.VECTORS_8),
	EIGHT_OFFSET(Support.VECTORS_8_OFFSET),
	SIXTEEN(Support.VECTORS_16);
	
	private final List<Vec3d> v;
	
	ParticlePattern(List<Vec3d> vectors) {
		v = vectors;
	}
	
	public List<Vec3d> getVectors() {
		return v;
	}
}
