package de.dafuqs.spectrum.api.energy.color;

import net.minecraft.util.*;
import org.joml.*;

import java.util.*;

public class CompoundColor extends InkColor {
	
	protected final Map<ElementalColor, Float> compoundColors; // colors used to directly mix this
	
	public CompoundColor(DyeColor dyeColor, Vector3f color, Identifier requiredAdvancement, Map<ElementalColor, Float> compoundColors, boolean darkShade) {
		super(dyeColor, color, requiredAdvancement, darkShade);
		this.compoundColors = compoundColors;
		
		for (Map.Entry<ElementalColor, Float> entry : compoundColors.entrySet()) {
			entry.getKey().addCompoundAmount(this, entry.getValue());
		}
	}

	public CompoundColor(DyeColor dyeColor, Vector3f color, Identifier requiredAdvancement, Map<ElementalColor, Float> compoundColors) {
		this(dyeColor, color, requiredAdvancement, compoundColors, false);
	}
	
	public boolean isMixedUsing(ElementalColor elementalColor) {
		return this.compoundColors.containsKey(elementalColor);
	}
	
	public Map<ElementalColor, Float> getElementalColorsToMix() {
		return this.compoundColors;
	}
	
}