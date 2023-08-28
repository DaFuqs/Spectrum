package de.dafuqs.spectrum.structures;

import net.minecraft.util.*;

public enum StructurePlacementType implements StringIdentifiable {
	ON_SOLID_GROUND("on_solid_ground"), // on solid ground
	PARTLY_BURIED("partly_buried"), // partly buried in solid ground
	ON_GROUND_WATER("on_solid_ground_water"); // on solid ground, may be in water
	
	public static final StringIdentifiable.Codec<StructurePlacementType> CODEC = StringIdentifiable.createCodec(StructurePlacementType::values);
	
	private final String id;
	
	StructurePlacementType(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
	@Override
	public String asString() {
		return this.id;
	}
	
}
