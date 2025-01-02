package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.block.entity.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class PastelNetwork {
	
	protected final Map<PastelNodeType, Set<PastelNodeBlockEntity>> loadedNodes = new ConcurrentHashMap<>();
    protected final Set<PastelNodeBlockEntity> priorityNodes = new HashSet<>();
    protected final Set<PastelNodeBlockEntity> highPriorityNodes = new HashSet<>();
    protected @NotNull Graph<BlockPos, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
	protected final World world;
	protected final UUID uuid;
	protected final SchedulerMap<PastelTransmission> transmissions = new SchedulerMap<>();

    public enum Priority {
        GENERIC,
        MODERATE,
        HIGH
    }
	
	public PastelNetwork(World world, @Nullable UUID uuid) {
		this.world = world;
		this.uuid = uuid == null ? UUID.randomUUID() : uuid;
		for (PastelNodeType type : PastelNodeType.values()) {
			this.loadedNodes.put(type, new HashSet<>());
		}
	}

    public void incorporate(PastelNetwork networkToIncorporate, PastelNodeBlockEntity node, PastelNodeBlockEntity otherNode) {
        for (Map.Entry<PastelNodeType, Set<PastelNodeBlockEntity>> nodesToIncorporate : networkToIncorporate.getLoadedNodes().entrySet()) {
            PastelNodeType type = nodesToIncorporate.getKey();
            for (PastelNodeBlockEntity nodeToIncorporate : nodesToIncorporate.getValue()) {
                this.loadedNodes.get(type).add(nodeToIncorporate);
                nodeToIncorporate.setParentNetwork(this);
                updateNodePriority(nodeToIncorporate, nodeToIncorporate.getPriority());
            }
        }
		networkToIncorporate.graph.vertexSet().forEach(graph::addVertex);
		networkToIncorporate.graph.edgeSet().forEach(edge -> {
			graph.addEdge(networkToIncorporate.getGraph().getEdgeSource(edge), networkToIncorporate.getGraph().getEdgeTarget(edge));
		});
    }

    public World getWorld() {
        return this.world;
    }

    public @NotNull Graph<BlockPos, DefaultEdge> getGraph() {
        return this.graph;
    }

    public void addNode(PastelNodeBlockEntity node) {
		//If this node already has a vertex, then all we are doing it is loading it
		if (graph.containsVertex(node.getPos())) {
			loadedNodes.get(node.getNodeType()).add(node);
			
		}
		else {
			if (addNodeOrReturn(node))
				return;
			
			this.graph.addVertex(node.getPos());
		}
        addPriorityNode(node);
    }

    /**
     * Note: this does not check if the nodes can connect, that should be done before calling this method.
     */
    public void addNodeAndConnect(PastelNodeBlockEntity newNode, PastelNodeBlockEntity parent) {
        if (addNodeOrReturn(newNode))
            return;

        this.graph.addVertex(newNode.getPos());
		getGraph().addEdge(newNode.getPos(), parent.getPos());

        // check for priority
        addPriorityNode(newNode);
    }
	
	public void addEdge(PastelNodeBlockEntity node, PastelNodeBlockEntity parent) {
		if (!hasEdge(node, parent))
			graph.addEdge(node.getPos(), parent.getPos());
	}

    public void removeEdge(PastelNodeBlockEntity node, PastelNodeBlockEntity parent) {
		graph.removeEdge(node.getPos(), parent.getPos());
    }

    public boolean hasEdge(PastelNodeBlockEntity node, PastelNodeBlockEntity otherNode) {
        if (!graph.containsVertex(node.getPos()) || !graph.containsVertex(otherNode.getPos()))
            return false;

        return graph.containsEdge(node.getPos(), otherNode.getPos());
    }
	
	/**
	 * @return True = return
	 */
	
    private boolean addNodeOrReturn(PastelNodeBlockEntity node) {
		return !this.loadedNodes.get(node.getNodeType()).add(node);
    }

    private void addPriorityNode(PastelNodeBlockEntity node) {
        switch (node.getPriority()) {
            case MODERATE -> priorityNodes.add(node);
            case HIGH -> highPriorityNodes.add(node);
        }
    }

    public void updateNodePriority(PastelNodeBlockEntity node, Priority oldPriority) {
        removePriorityNode(node, oldPriority);
        addPriorityNode(node);
    }

    protected boolean removeNode(PastelNodeBlockEntity node, NodeRemovalReason reason) {
        boolean hadNode = this.loadedNodes.get(node.getNodeType()).remove(node);
        if (!hadNode) {
            return false;
        }

		// delete the now removed node from this networks graph - IF IT WASN'T UNLOADED
		if (reason != NodeRemovalReason.UNLOADED)
			graph.removeVertex(node.getPos());

        removePriorityNode(node, node.getPriority());

        return true;
    }

    private void removePriorityNode(PastelNodeBlockEntity node, Priority priority) {
        switch (priority) {
            case MODERATE -> priorityNodes.remove(node);
            case HIGH -> highPriorityNodes.remove(node);
        }
    }

    public boolean hasNodes() {
        for (Set<PastelNodeBlockEntity> nodeList : this.loadedNodes.values()) {
            if (!nodeList.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public Set<PastelNodeBlockEntity> getNodes(PastelNodeType type) {
        return getNodes(type, Priority.GENERIC);
    }

    public Set<PastelNodeBlockEntity> getNodes(PastelNodeType type, Priority priority) {
        var nodeType = this.loadedNodes.get(type);

        if (priority == Priority.MODERATE) {
            return nodeType.stream().filter(priorityNodes::contains).collect(Collectors.toSet());
        }

        if (priority == Priority.HIGH) {
            return nodeType.stream().filter(highPriorityNodes::contains).collect(Collectors.toSet());
        }

        return nodeType;
    }

    public Map<PastelNodeType, Set<PastelNodeBlockEntity>> getLoadedNodes() {
        return this.loadedNodes;
    }

    public int getNodeCount() {
        int nodes = 0;
        for (Set<PastelNodeBlockEntity> nodeList : this.loadedNodes.values()) {
            nodes += nodeList.size();
        }
        return nodes;
    }

    public List<PastelNodeBlockEntity> getAllNodes() {
        List<PastelNodeBlockEntity> nodes = new ArrayList<>();
        for (Map.Entry<PastelNodeType, Set<PastelNodeBlockEntity>> nodeList : this.loadedNodes.entrySet()) {
            nodes.addAll(this.loadedNodes.get(nodeList.getKey()));
        }
        return nodes;
    }

    public boolean canConnect(PastelNodeBlockEntity newNode) {
        if (newNode.getWorld() != this.getWorld()) {
            return false;
        }

        for (Set<PastelNodeBlockEntity> nodeList : this.loadedNodes.values()) {
            for (PastelNodeBlockEntity currentNode : nodeList) {
                if (currentNode.canConnect(newNode)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void tick() {
        transmissions.tick();
    }



    public UUID getUUID() {
        return this.uuid;
    }

    public void addTransmission(PastelTransmission transmission, int travelTime) {
        transmission.setNetwork(this);
        this.transmissions.put(transmission, travelTime);
    }

    public int getColor() {
        return ColorHelper.getRandomColor(this.uuid.hashCode());
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof PastelNetwork p) {
            return this.uuid.equals(p.uuid);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.uuid.toString());
        for (PastelNodeType type : PastelNodeType.values()) {
            builder.append("-").append(getNodes(type).size());
        }
        return builder.toString();
    }

    public String getNodeDebugText() {
        return "Prov: " +
                getNodes(PastelNodeType.PROVIDER).size() +
                " - Send: " +
                getNodes(PastelNodeType.SENDER).size() +
                " - Gath: " +
                getNodes(PastelNodeType.GATHER).size() +
                " - Stor: " +
                getNodes(PastelNodeType.STORAGE).size() +
                " - Buff: " +
                getNodes(PastelNodeType.BUFFER).size() +
                " - Conn: " +
                getNodes(PastelNodeType.CONNECTION).size();
    }
	
	public NbtCompound toNbt() {
		NbtCompound compound = new NbtCompound();
		compound.putUuid("UUID", this.uuid);
		compound.putString("World", this.getWorld().getRegistryKey().getValue().toString());
		
		var vertices = new ArrayList<>(graph.vertexSet());
		var graphStorage = new NbtCompound();
		graphStorage.putInt("Size", vertices.size());
		for (int i = 0; i < vertices.size(); i++) {
			var vertex = vertices.get(i);
			
			// Store the Vertex
			graphStorage.putLong("Vertex" + i, vertex.asLong());
			
			// Save the edges
			var edgeIndexes = graph.edgesOf(vertex)
					.stream()
					.map((edge) -> {
						var target = graph.getEdgeTarget(edge);
						var source = graph.getEdgeSource(edge);
						return target.equals(vertex) ? source : target;
					})
					.mapToInt(vertices::indexOf)
					.boxed()
					.collect(Collectors.toList());
			edgeIndexes.add(0, vertices.indexOf(vertex));
			
			graphStorage.putIntArray("EdgeIndexes" + i, edgeIndexes);
		}
		
		compound.put("Graph", graphStorage);
		
		NbtList transmissionList = new NbtList();
		for (Map.Entry<PastelTransmission, Integer> transmission : this.transmissions) {
			NbtCompound transmissionCompound = new NbtCompound();
			transmissionCompound.putInt("Delay", transmission.getValue());
			transmissionCompound.put("Transmission", transmission.getKey().toNbt());
			transmissionList.add(transmissionCompound);
		}
		compound.put("Transmissions", transmissionList);
		
		return compound;
	}
	
	public void fromNbt(NbtCompound compound) {
		if (compound.contains("Graph")) {
			var graphStorage = compound.getCompound("Graph");
			var size = graphStorage.getInt("Size");
			var vertices = new ArrayList<BlockPos>();
			for (int i = 0; i < size; i++) {
				var vertex = BlockPos.fromLong(graphStorage.getLong("Vertex" + i));
				vertices.add(vertex);
				graph.addVertex(vertex);
			}
			
			for (int i = 0; i < size; i++) {
				var edgeIndexes = graphStorage.getIntArray("EdgeIndexes" + i);
				var source = vertices.get(edgeIndexes[0]);
				for (int targetIndex = 1; targetIndex < edgeIndexes.length; targetIndex++) {
					var target = vertices.get(targetIndex);
					if (!graph.containsEdge(source, target) && !source.equals(target))
						graph.addEdge(source, target);
				}
			}
		}
	}

    public PastelNodeBlockEntity getNodeAt(BlockPos blockPos) {
		if (!this.getWorld().isChunkLoaded(blockPos)) {
			return null; // hmmmmm
		}
		
        BlockEntity blockEntity = this.getWorld().getBlockEntity(blockPos);
        if (blockEntity instanceof PastelNodeBlockEntity pastelNodeBlockEntity) {
            return pastelNodeBlockEntity;
        }
        return null;
    }

}
