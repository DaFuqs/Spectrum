package de.dafuqs.spectrum.api.energy.color;

import net.minecraft.util.*;

import java.util.*;

public class CompoundColor extends InkColor {
	
	protected final Map<ElementalColor, Float> compoundColors; // colors used to directly mix this
	
	public CompoundColor(DyeColor dyeColor, int color, Identifier requiredAdvancement, Map<ElementalColor, Float> compoundColors) {
		this(dyeColor, color, color, requiredAdvancement, compoundColors, false);
	}
	
	public CompoundColor(DyeColor dyeColor, int color, int textColor, Identifier requiredAdvancement, Map<ElementalColor, Float> compoundColors, boolean darkShade) {
		super(dyeColor, color, textColor, requiredAdvancement, darkShade);
		this.compoundColors = compoundColors;
		
		for (Map.Entry<ElementalColor, Float> entry : compoundColors.entrySet()) {
			entry.getKey().addCompoundAmount(this, entry.getValue());
		}
	}
	
	public boolean isMixedUsing(ElementalColor elementalColor) {
		return this.compoundColors.containsKey(elementalColor);
	}
	
	public Map<ElementalColor, Float> getElementalColorsToMix() {
		return this.compoundColors;
	}
	
}