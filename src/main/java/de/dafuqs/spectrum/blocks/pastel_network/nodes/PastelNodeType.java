package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import net.minecraft.text.*;

public enum PastelNodeType {
    /**
     * Node network:
     * - Need to be placed on inventories (sided?)
     * - All components can be dyed (pigment, crafting or right-clicking)
     * - A certain colored node can only interact with the nodes it composites into, but not vice-versa (blue => cyan, but not cyan => blue)
     * - Connected on sight, like CC nodes
     * - Use Mermaids gem on node to transform it into fluid node
     * <p>
     * CLEAR
     * Basic connection node, not interacting actively ("connectors")
     */
    CONNECTION("block.spectrum.connection_node.tooltip"),

    /**
     * TOPAZ
     * Storage (everything that has no target gets put here)
     */
    STORAGE("block.spectrum.storage_node.tooltip"),

    /**
     * AMETHYST
     * Passive Provider (can be requested from)
     */
    PROVIDER("block.spectrum.provider_node.tooltip"),

    /**
     * CITRINE
     * Active Provider (pushes items into network)
     */
    SENDER("block.spectrum.sender_node.tooltip"),

    /**
     * ONYX
     * Requester Nodes, requests on redstone (active>passive>storage)
     */
    GATHER("block.spectrum.gather_node.tooltip");

    private final MutableText tooltip;

    PastelNodeType(String tooltip) {
        this.tooltip = Text.translatable(tooltip);
    }

    public MutableText getTooltip() {
        return this.tooltip;
    }

}
