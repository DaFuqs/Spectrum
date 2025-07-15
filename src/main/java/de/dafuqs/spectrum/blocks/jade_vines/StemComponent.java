package de.dafuqs.spectrum.blocks.jade_vines;

import net.minecraft.util.*;
import net.minecraft.world.level.block.state.properties.*;

public enum StemComponent implements StringRepresentable {
	BASE("base"),
	STEM("stem"),
	STEMALT("stemalt");
	
	
	public static final EnumProperty<StemComponent> PROPERTY = EnumProperty.create("part", StemComponent.class);
	
	public final String identifier;
	
	StemComponent(String identifier) {
		this.identifier = identifier;
	}
	
	@Override
	public String getSerializedName() {
		return identifier;
	}
	
	@Override
	public String toString() {
		return identifier;
	}
}
