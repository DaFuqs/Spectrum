package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface PastelNetworkManager {
    
    //PastelNetwork createNetwork(World world, UUID uuid);
    
    PastelNetwork JoinOrCreateNetwork(PastelNodeBlockEntity node, @Nullable UUID uuid);

    void connectNodes(PastelNodeBlockEntity node, PastelNodeBlockEntity parent);
    
    //void removeEmptyNetwork(PastelNetwork network);

    void removeNode(PastelNodeBlockEntity node, NodeRemovalReason reason);
    
    
    Optional<? extends PastelNetwork> getNetwork(UUID uuid);

    default boolean tryRemoveEdge(PastelNodeBlockEntity node, PastelNodeBlockEntity otherNode) {
        if (node.getParentNetwork() == null) {
            throw new IllegalStateException("Attempted to remove an edge from a null network");
        }

        if (node.getParentNetwork() != otherNode.getParentNetwork()) {
            throw new IllegalArgumentException("Can't remove an edge between nodes in different networks - how did you even do this");
        }
        var network = node.getParentNetwork();
        if (!network.hasEdge(node, otherNode))
            return false;

        node.getParentNetwork().removeAndForgetEdge(node, otherNode);
        return true;
    }

    default boolean tryAddEdge(PastelNodeBlockEntity node, PastelNodeBlockEntity otherNode) {
        if (node.getParentNetwork() == null) {
            throw new IllegalStateException("Attempted to add an edge to a null network");
        }

        if (node.getParentNetwork() != otherNode.getParentNetwork()) {
            throw new IllegalArgumentException("Can't add an edge between nodes in different networks");
        }

        if (node == otherNode || node.getParentNetwork().hasEdge(node, otherNode))
            return false;

        node.getParentNetwork().addAndRememberEdge(node, otherNode);
        return true;
    }
    
}
