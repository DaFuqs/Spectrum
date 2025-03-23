package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.*;

public enum PastelNodeType {
	CONNECTION(List.of(Text.translatable("block.spectrum.connection_node.tooltip")), false, false),
	STORAGE(List.of(Text.translatable("block.spectrum.storage_node.tooltip").formatted(Formatting.WHITE), Text.translatable("block.spectrum.pastel_network_nodes.tooltip.placing").formatted(Formatting.GRAY)), true, true),
	BUFFER(List.of(Text.translatable("block.spectrum.buffer_node.tooltip").formatted(Formatting.WHITE), Text.translatable("block.spectrum.pastel_network_nodes.tooltip.placing").formatted(Formatting.GRAY)), true, true),
	PROVIDER(List.of(Text.translatable("block.spectrum.provider_node.tooltip").formatted(Formatting.WHITE), Text.translatable("block.spectrum.pastel_network_nodes.tooltip.placing").formatted(Formatting.GRAY)), false, true),
	SENDER(List.of(Text.translatable("block.spectrum.sender_node.tooltip").formatted(Formatting.WHITE), Text.translatable("block.spectrum.pastel_network_nodes.tooltip.placing").formatted(Formatting.GRAY)), false, true),
	GATHER(List.of(Text.translatable("block.spectrum.gather_node.tooltip").formatted(Formatting.WHITE), Text.translatable("block.spectrum.pastel_network_nodes.tooltip.placing").formatted(Formatting.GRAY)), true, false);
	
	private final List<Text> tooltips;
	private final boolean usesFilters, hasOuterRing;
	
	PastelNodeType(List<Text> tooltips, boolean usesFilters, boolean hasOuterRing) {
		this.tooltips = tooltips;
		this.usesFilters = usesFilters;
		this.hasOuterRing = hasOuterRing;
	}
	
	public List<Text> getTooltips() {
		return this.tooltips;
	}

	public boolean usesFilters() {
		return usesFilters;
	}

	public boolean hasOuterRing() {
		return hasOuterRing;
	}
}
