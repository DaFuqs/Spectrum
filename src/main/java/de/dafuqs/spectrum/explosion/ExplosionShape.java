package de.dafuqs.spectrum.explosion;

import net.minecraft.core.*;

public enum ExplosionShape {
	DEFAULT,
	SQUARE;
	
	public boolean isAffected(BlockPos center, BlockPos p) {
		if (this == ExplosionShape.SQUARE) {
			return true;
		}
		return Math.pow(p.getX() - center.getX(), 2) + Math.pow(p.getY() - center.getY(), 2) + Math.pow(p.getZ() - center.getZ(), 2) < 4 * 4;
	}
	
}
