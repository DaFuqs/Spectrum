package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import net.minecraft.text.*;

public enum PastelNodeType {
    CONNECTION("block.spectrum.connection_node.tooltip", false),
    STORAGE("block.spectrum.storage_node.tooltip", true),
    PROVIDER("block.spectrum.provider_node.tooltip", false),
    SENDER("block.spectrum.sender_node.tooltip", false),
    GATHER("block.spectrum.gather_node.tooltip", true);

    private final MutableText tooltip;
    private final boolean usesFilters;

    PastelNodeType(String tooltip, boolean usesFilters) {
        this.tooltip = Text.translatable(tooltip);
        this.usesFilters = usesFilters;
    }

    public MutableText getTooltip() {
        return this.tooltip;
    }

    public boolean usesFilters() {
        return usesFilters;
    }

}
