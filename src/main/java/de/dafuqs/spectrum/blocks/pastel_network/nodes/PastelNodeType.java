package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import net.minecraft.text.*;

public enum PastelNodeType {
    CONNECTION("block.spectrum.connection_node.tooltip", false, false, false),
    STORAGE("block.spectrum.storage_node.tooltip", true, true, true),
    PROVIDER("block.spectrum.provider_node.tooltip", false, true, false),
    SENDER("block.spectrum.sender_node.tooltip", false, true, false),
    GATHER("block.spectrum.gather_node.tooltip", true, false, true);

    private final MutableText tooltip;
    private final boolean usesFilters;
    private final boolean supportsExtracting;
    private final boolean supportsInserting;

    PastelNodeType(String tooltip, boolean usesFilters, boolean supportsExtracting, boolean supportsInserting) {
        this.tooltip = Text.translatable(tooltip);
        this.usesFilters = usesFilters;
        this.supportsExtracting = supportsExtracting;
        this.supportsInserting = supportsInserting;
    }

    public MutableText getTooltip() {
        return this.tooltip;
    }

    public boolean usesFilters() {
        return usesFilters;
    }

    public boolean isSupportsExtracting() {
        return supportsExtracting;
    }

    public boolean isSupportsInserting() {
        return supportsInserting;
    }

}
