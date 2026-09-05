package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import net.minecraft.util.*;

public enum PastelNodeType implements StringRepresentable {
	CONNECTION(false, false),
	STORAGE(true, true),
	PROVIDER(false, true),
	SENDER(false, true),
	GATHER(true, true);
	
	private final boolean usesFilters, hasOuterRing;
	
	PastelNodeType(boolean usesFilters, boolean hasOuterRing) {
		this.usesFilters = usesFilters;
		this.hasOuterRing = hasOuterRing;
	}
	
	public boolean usesFilters() {
		return usesFilters;
	}
	
	public boolean hasOuterRing() {
		return hasOuterRing;
	}
	
	@Override
	public String getSerializedName() {
		return name();
	}
}
