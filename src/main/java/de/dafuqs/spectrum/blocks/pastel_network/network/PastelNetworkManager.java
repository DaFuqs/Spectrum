package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.graph.*;

import java.util.*;

public interface PastelNetworkManager {
    
    //PastelNetwork createNetwork(World world, UUID uuid);
	
	PastelNetwork joinOrCreateNetwork(PastelNodeBlockEntity node, @Nullable UUID uuid);

    void connectNodes(PastelNodeBlockEntity node, PastelNodeBlockEntity parent, @NotNull UUID id);
	
	default void connectNodes(PastelNodeBlockEntity node, PastelNodeBlockEntity parent) {
		connectNodes(node, parent, UUID.randomUUID());
	}
    
    //void removeEmptyNetwork(PastelNetwork network);

    void removeNode(PastelNodeBlockEntity node, NodeRemovalReason reason);
	
	PastelNetwork createNetwork(World world, UUID uuid);
	
    Optional<? extends PastelNetwork> getNetwork(UUID uuid);

    default boolean tryAddEdge(PastelNodeBlockEntity node, PastelNodeBlockEntity otherNode) {
        if (node.getParentNetwork() == null) {
            throw new IllegalStateException("Attempted to add an edge to a null network");
        }

        if (node.getParentNetwork() != otherNode.getParentNetwork()) {
            throw new IllegalArgumentException("Can't add an edge between nodes in different networks");
        }

        if (node == otherNode || node.getParentNetwork().hasEdge(node, otherNode))
            return false;

        node.getParentNetwork().addEdge(node, otherNode);
        return true;
    }

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

        node.getParentNetwork().removeEdge(node, otherNode);
		checkForNetworkSplit(node.getParentNetwork());
        return true;
    }
	
	default void checkForNetworkSplit(PastelNetwork network) {
		ConnectivityInspector<BlockPos, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(network.getGraph());
		List<Set<BlockPos>> connectedSets = connectivityInspector.connectedSets();
		if (connectedSets.size() != 1) {
			for (int i = 1; i < connectedSets.size(); i++) {
				Set<BlockPos> disconnectedNodes = connectedSets.get(i);
				Map<BlockPos, List<BlockPos>> transitiveEdges = new HashMap<>();
				PastelNetwork newNetwork = createNetwork(network.world, ((PastelNodeBlockEntity) (network.world.getBlockEntity(disconnectedNodes.iterator().next()))).getInitialID());
				for (BlockPos disconnectedNode : disconnectedNodes) {
					for (DefaultEdge switchedEdge : network.getGraph().edgesOf(disconnectedNode)) {
						var edgeList = transitiveEdges.computeIfAbsent(disconnectedNode, p -> new ArrayList<>());
						var target = network.graph.getEdgeTarget(switchedEdge);
						if (!target.equals(disconnectedNode) && !edgeList.contains(target) && disconnectedNodes.contains(target))
							edgeList.add(target);
					}
				}
				for (BlockPos disconnectedNode : disconnectedNodes) {
					var switchedNode = network.getWorld().getBlockEntity(disconnectedNode);
					if (switchedNode instanceof PastelNodeBlockEntity pastelNode) {
						network.removeNode(pastelNode, NodeRemovalReason.DISCONNECT);
						newNetwork.addNode(pastelNode);
						pastelNode.setParentNetwork(newNetwork);
					}
				}
				for (BlockPos node : transitiveEdges.keySet()) {
					for (BlockPos target : transitiveEdges.get(node)) {
						if (!newNetwork.graph.containsEdge(node, target))
							newNetwork.graph.addEdge(node, target);
					}
				}
			}
		}
	}
}
