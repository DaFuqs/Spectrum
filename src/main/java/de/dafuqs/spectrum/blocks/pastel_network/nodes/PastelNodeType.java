package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import net.minecraft.text.*;

public enum PastelNodeType {
	CONNECTION("block.spectrum.connection_node.tooltip", false, false),
	STORAGE("block.spectrum.storage_node.tooltip", true, true),
	BUFFER("block.spectrum.buffer_node.tooltip", true, true),
	PROVIDER("block.spectrum.provider_node.tooltip", false, true),
	SENDER("block.spectrum.sender_node.tooltip", false, true),
	GATHER("block.spectrum.gather_node.tooltip", true, false);

	private final MutableText tooltip;
	private final boolean usesFilters, hasOuterRing;

	PastelNodeType(String tooltip, boolean usesFilters, boolean hasOuterRing) {
		this.tooltip = Text.translatable(tooltip);
		this.usesFilters = usesFilters;
		this.hasOuterRing = hasOuterRing;
	}

	public MutableText getTooltip() {
		return this.tooltip;
	}

	public boolean usesFilters() {
		return usesFilters;
	}

	public boolean hasOuterRing() {
		return hasOuterRing;
	}
}
