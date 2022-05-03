package de.dafuqs.spectrum.energy.color;

import net.minecraft.util.DyeColor;

import java.util.Map;

public class CompoundColor extends CMYKColor {
	
	protected final Map<ElementalColor, Float> compoundColors; // colors used to directly mix this

	public CompoundColor(DyeColor dyeColor, Map<ElementalColor, Float> compoundColors) {
		super(dyeColor);
		this.compoundColors = compoundColors;
		
		for(Map.Entry<ElementalColor, Float> entry : compoundColors.entrySet()) {
			entry.getKey().addCompoundAmount(dyeColor, entry.getValue());
		}
	}
	
	public boolean isUsedForMixing(CompoundColor dyeColor) {
		return false;
	}
	
	public boolean isMixedUsing(CompoundColor dyeColor) {
		return this.compoundColors.containsKey(this.dyeColor);
	}
	
	public Map<ElementalColor, Float> getElementalColorsToMix() {
		return this.compoundColors;
	}
	
}