package de.dafuqs.spectrum.energy.color;

import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

import java.util.*;

public abstract class InkColor {
	
	protected static final Map<DyeColor, InkColor> DYE_TO_COLOR = new HashMap<>();
	protected static final List<InkColor> ALL_COLORS = new ArrayList<>();
	protected static final List<ElementalColor> ELEMENTAL_COLORS = new ArrayList<>();
	
	protected final DyeColor dyeColor;
	protected final Vec3f color;

    protected final Identifier requiredAdvancement;
	protected InkColor(DyeColor dyeColor, Vec3f color, Identifier requiredAdvancement) {
		this.dyeColor = dyeColor;
		this.color = color;
        this.requiredAdvancement = requiredAdvancement;

        ALL_COLORS.add(this);
        
		DYE_TO_COLOR.put(dyeColor, this);
	}
	
	public static InkColor of(DyeColor dyeColor) {
		return DYE_TO_COLOR.get(dyeColor);
	}
	
	public static InkColor of(String colorString) {
		return DYE_TO_COLOR.get(DyeColor.valueOf(colorString.toUpperCase(Locale.ROOT)));
	}
	
	public static List<InkColor> all() {
		return ALL_COLORS;
	}
	
	public static List<ElementalColor> elementals() {
		return ELEMENTAL_COLORS;
	}
	
	public DyeColor getDyeColor() {
		return this.dyeColor;
	}
	
	@Override
	public String toString() {
		return this.dyeColor.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		InkColor that = (InkColor) o;
		return this.dyeColor.equals(that.dyeColor);
	}
	
	// hash table lookup go wheeeeee!
	@Override
	public int hashCode() {
		return dyeColor.getId();
	}
	
	public TranslatableText getName() {
		return new TranslatableText("spectrum.ink.color." + this);
	}
	
	public Vec3f getColor() {
		return this.color;
	}


    public Identifier getRequiredAdvancement() {
        return requiredAdvancement;
    }
}



