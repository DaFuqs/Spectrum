package de.dafuqs.spectrum.api.energy.color;

import net.minecraft.util.*;

import java.util.*;

public class ElementalColor extends InkColor {
	
	protected final Map<CompoundColor, Float> mixedColors = new HashMap<>(); // colors that can be mixed from this
	
	public ElementalColor(DyeColor dyeColor, int color, Identifier requiredAdvancement) {
		this(dyeColor, color, color, requiredAdvancement, false);
	}
	
	public ElementalColor(DyeColor dyeColor, int color, int textColor, Identifier requiredAdvancement, boolean darkShade) {
		super(dyeColor, color, textColor, requiredAdvancement, darkShade);
		ELEMENTAL_COLORS.add(this);
	}
	
	public boolean isUsedForMixing(CompoundColor compoundColor) {
		return this.mixedColors.containsKey(compoundColor);
	}
	
	public void addCompoundAmount(CompoundColor compoundColor, float amount) {
		this.mixedColors.put(compoundColor, amount);
	}
	
}