package de.dafuqs.spectrum.explosion;

import net.minecraft.util.math.*;

public enum ExplosionShape {
	DEFAULT,
	SQUARE;
	
	public boolean isAffected(BlockPos center, BlockPos p) {
		switch (this) {
			case SQUARE -> {
				return true;
			}
			default -> {
				return Math.pow(p.getX() - center.getX(), 2) + Math.pow(p.getY() - center.getY(), 2) + Math.pow(p.getZ() - center.getZ(), 2) < 4 * 4;
			}
		}
	}
	
}
