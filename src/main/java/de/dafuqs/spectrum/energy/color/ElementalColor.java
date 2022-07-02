package de.dafuqs.spectrum.energy.color;

import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

import java.util.HashMap;
import java.util.Map;

public class ElementalColor extends InkColor {
	
	protected Map<CompoundColor, Float> mixedColors = new HashMap<>(); // colors that can be mixed from this
  
	public ElementalColor(DyeColor dyeColor, Vec3f color, Identifier requiredAdvancement) {
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