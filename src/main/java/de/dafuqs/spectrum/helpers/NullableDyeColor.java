package de.dafuqs.spectrum.helpers;

import net.minecraft.util.*;
import org.jetbrains.annotations.*;

import java.util.*;

public enum NullableDyeColor implements StringIdentifiable {
	
	WHITE(0, "white", DyeColor.WHITE),
	ORANGE(1, "orange", DyeColor.ORANGE),
	MAGENTA(2, "magenta", DyeColor.MAGENTA),
	LIGHT_BLUE(3, "light_blue", DyeColor.LIGHT_BLUE),
	YELLOW(4, "yellow", DyeColor.YELLOW),
	LIME(5, "lime", DyeColor.LIME),
	PINK(6, "pink", DyeColor.PINK),
	GRAY(7, "gray", DyeColor.GRAY),
	LIGHT_GRAY(8, "light_gray", DyeColor.LIGHT_GRAY),
	CYAN(9, "cyan", DyeColor.CYAN),
	PURPLE(10, "purple", DyeColor.PURPLE),
	BLUE(11, "blue", DyeColor.BLUE),
	BROWN(12, "brown", DyeColor.BROWN),
	GREEN(13, "green", DyeColor.GREEN),
	RED(14, "red", DyeColor.RED),
	BLACK(15, "black", DyeColor.BLACK),
	NONE(16, "none", null);
	
	private static final NullableDyeColor[] VALUES = Arrays.stream(values()).sorted(Comparator.comparingInt(NullableDyeColor::getId)).toArray(NullableDyeColor[]::new);
	private final int id;
	private final String name;
	private final @Nullable DyeColor dyeColor;
	
	NullableDyeColor(int id, String name, @Nullable DyeColor dyeColor) {
		this.id = id;
		this.name = name;
		this.dyeColor = dyeColor;
	}
	
	public static NullableDyeColor get(@Nullable DyeColor dyeColor) {
		if (dyeColor == null) {
			return NONE;
		}
		return byId(dyeColor.getId());
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public @Nullable DyeColor getDyeColor() {
		return this.dyeColor;
	}
	
	public static NullableDyeColor byId(int id) {
		if (id < 0 || id >= VALUES.length) {
			id = 0;
		}
		
		return VALUES[id];
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	@Override
	public String asString() {
		return this.name;
	}
	
}