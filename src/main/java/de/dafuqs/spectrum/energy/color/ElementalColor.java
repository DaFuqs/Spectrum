package de.dafuqs.spectrum.energy.color;

import net.minecraft.util.DyeColor;

import java.util.HashMap;

public class ElementalColor extends CMYKColor {
	
	protected HashMap<DyeColor, Float> mixedColors; // colors that can be mixed from this
	
	public ElementalColor(DyeColor dyeColor) {
		super(dyeColor);
		ELEMENTAL_COLORS.add(this);
	}
	
	public boolean isUsedForMixing(CompoundColor dyeColor) {
		return this.mixedColors.keySet().contains(dyeColor);
	}
	
	public boolean isMixedUsing(CompoundColor dyeColor) {
		return false;
	}
	
	public void addCompoundAmount(DyeColor dyeColor, float amount) {
		this.mixedColors.put(dyeColor, amount);
	}
	
}