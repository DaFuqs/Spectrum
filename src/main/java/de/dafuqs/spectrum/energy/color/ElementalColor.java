package de.dafuqs.spectrum.energy.color;

import net.minecraft.util.*;
import org.joml.*;

import java.util.*;

public class ElementalColor extends InkColor {
	
	protected final Map<CompoundColor, Float> mixedColors = new HashMap<>(); // colors that can be mixed from this
	
	public ElementalColor(DyeColor dyeColor, Vector3f color, Identifier requiredAdvancement) {
		super(dyeColor, color, requiredAdvancement);
		ELEMENTAL_COLORS.add(this);
	}
	
	public boolean isUsedForMixing(CompoundColor compoundColor) {
		return this.mixedColors.containsKey(compoundColor);
	}
	
	public void addCompoundAmount(CompoundColor compoundColor, float amount) {
		this.mixedColors.put(compoundColor, amount);
	}
	
}