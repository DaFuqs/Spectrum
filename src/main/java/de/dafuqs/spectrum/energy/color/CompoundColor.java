package de.dafuqs.spectrum.energy.color;

import net.minecraft.util.*;
import org.joml.*;

import java.util.*;

public class CompoundColor extends InkColor {
	
	protected final Map<ElementalColor, Float> compoundColors; // colors used to directly mix this
	
	public CompoundColor(DyeColor dyeColor, Vector3f color, Identifier requiredAdvancement, Map<ElementalColor, Float> compoundColors) {
		super(dyeColor, color, requiredAdvancement);
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