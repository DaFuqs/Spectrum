package de.dafuqs.spectrum.energy.color;

import net.minecraft.util.DyeColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class CMYKColor {
	
	protected static final Map<DyeColor, CMYKColor> COLORS = new HashMap<>();
	protected static final Collection<ElementalColor> ELEMENTAL_COLORS = new ArrayList<>();
	
	protected final DyeColor dyeColor;
	
	protected CMYKColor(DyeColor dyeColor) {
		this.dyeColor = dyeColor;
		COLORS.put(dyeColor, this);
	}
	
	public static CMYKColor of(DyeColor dyeColor) {
		return COLORS.get(dyeColor);
	}
	
	public static Collection<CMYKColor> all() {
		return COLORS.values();
	}
	
	public static Collection<ElementalColor> elementals() {
		return ELEMENTAL_COLORS;
	}
	
	public DyeColor getDyeColor() {
		return this.dyeColor;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CMYKColor that = (CMYKColor) o;
		return this.dyeColor.equals(that.dyeColor);
	}

	@Override
	public int hashCode() {
		return dyeColor.ordinal();
	}

}



