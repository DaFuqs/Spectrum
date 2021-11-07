package de.dafuqs.spectrum.enums;

import net.minecraft.util.DyeColor;

public enum GemstoneColor {
	CYAN,
	MAGENTA,
	YELLOW,
	BLACK,
	WHITE;
	
	public DyeColor getDyeColor() {
		switch (this) {
			case CYAN -> {
				return DyeColor.CYAN;
			}
			case MAGENTA -> {
				return DyeColor.MAGENTA;
			}
			case YELLOW -> {
				return DyeColor.YELLOW;
			}
			case BLACK -> {
				return DyeColor.BLACK;
			}
			default -> {
				return DyeColor.WHITE;
			}
		}
	}
	
}