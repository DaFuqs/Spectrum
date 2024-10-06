package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.block.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class PastelNetwork {
    
    protected final Map<PastelNodeType, Set<PastelNodeBlockEntity>> nodes = new ConcurrentHashMap<>();
    protected final Set<PastelNodeBlockEntity> priorityNodes = new HashSet<>();
    protected final Set<PastelNodeBlockEntity> highPriorityNodes = new HashSet<>();
    protected @Nullable Graph<PastelNodeBlockEntity, DefaultEdge> graph;
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
			this.nodes.put(type, new HashSet<>());
		}
	}

    public void incorporate(PastelNetwork networkToIncorporate, PastelNodeBlockEntity node, PastelNodeBlockEntity otherNode) {
        for (Map.Entry<PastelNodeType, Set<PastelNodeBlockEntity>> nodesToIncorporate : networkToIncorporate.getNodes().entrySet()) {
            PastelNodeType type = nodesToIncorporate.getKey();
            for (PastelNodeBlockEntity nodeToIncorporate : nodesToIncorporate.getValue()) {
                this.nodes.get(type).add(nodeToIncorporate);
                nodeToIncorporate.setParentNetwork(this);
                updateNodePriority(nodeToIncorporate, nodeToIncorporate.getPriority());
            }
        }

        node.remember(otherNode);
        otherNode.remember(node);
        this.graph = buildGraph(this);
    }

    public World getWorld() {
        return this.world;
    }

    public Graph<PastelNodeBlockEntity, DefaultEdge> getGraph() {
        if (this.graph == null) {
            this.graph = buildGraph(this);
        }
        return this.graph;
    }

    private static @NotNull SimpleGraph<PastelNodeBlockEntity, DefaultEdge> buildGraph(@NotNull PastelNetwork network) {
        SimpleGraph<PastelNodeBlockEntity, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        var world = network.world;

        for (PastelNodeBlockEntity node : network.getAllNodes()) {
            g.addVertex(node);
        }

        for (PastelNodeBlockEntity node : network.getAllNodes()) {
            var memory = node.getRememberedConnections();

            for (BlockPos pos : memory) {
                if (!world.isPosLoaded(pos.getX(), pos.getZ()))
                    continue;

                var rememberedNode = network.getNodeAt(pos);

                if (rememberedNode == null || !network.getAllNodes().contains(rememberedNode))
                    continue;

                g.addEdge(node, rememberedNode);
            }
        }

        return g;
    }

    public void addNode(PastelNodeBlockEntity node) {
        if (addNodeOrReturn(node))
            return;

        this.graph.addVertex(node);
        addPriorityNode(node);
    }

    public void addNodeAndLoadMemory(PastelNodeBlockEntity node) {
        if (addNodeOrReturn(node))
            return;

        // calculate connections for new node
        this.graph.addVertex(node);
        for (BlockPos memory : node.getRememberedConnections()) {
            if (!world.isPosLoaded(memory.getX(), memory.getZ()))
                continue;

            var rememberedNode = getNodeAt(memory);

            if (rememberedNode == null || !getAllNodes().contains(rememberedNode))
                continue;

            this.graph.addEdge(node, rememberedNode);
        }

        // check for priority
        addPriorityNode(node);
    }

    /**
     * Note: this does not check if the nodes can connect, that should be done before calling this method.
     */
    public void addNodeAndConnect(PastelNodeBlockEntity newNode, PastelNodeBlockEntity parent) {
        if (addNodeOrReturn(newNode, true))
            return;

        this.graph.addVertex(newNode);
        addAndRememberEdge(newNode, parent);

        // check for priority
        addPriorityNode(newNode);
    }

    public void addAndRememberEdge(PastelNodeBlockEntity newNode, PastelNodeBlockEntity parent) {
        getGraph().addEdge(newNode, parent);
        newNode.remember(parent);
        parent.remember(newNode);
    }

    public void removeAndForgetEdge(PastelNodeBlockEntity node, PastelNodeBlockEntity parent) {
        if (graph != null) {
            graph.removeEdge(node, parent);
        }

        node.forget(parent);
        parent.forget(node);
    }

    public boolean hasEdge(PastelNodeBlockEntity node, PastelNodeBlockEntity otherNode) {
        if (this.graph == null)
            return false;

        if (!graph.containsVertex(node) || !graph.containsVertex(otherNode))
            return false;

        return graph.containsEdge(node, otherNode);
    }

    private boolean addNodeOrReturn(PastelNodeBlockEntity node, boolean allowGraphCreation) {
        if (!this.nodes.get(node.getNodeType()).add(node)) {
            return true;
        }

        if (graph == null && allowGraphCreation) {
            this.graph = buildGraph(this);
            return false;
        }

        return this.graph == null;
    }

    private boolean addNodeOrReturn(PastelNodeBlockEntity node) {
        return addNodeOrReturn(node, false);
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
        boolean hadNode = this.nodes.get(node.getNodeType()).remove(node);
        if (!hadNode) {
            return false;
        }

        if (this.graph != null) {
            // delete the now removed node from this networks graph
            removeAndForget(node);
        }

        node.forgetAll();
        removePriorityNode(node, node.getPriority());

        return true;
    }

    private void removeAndForget(PastelNodeBlockEntity node) {
        assert graph != null;
        for (DefaultEdge edge : this.graph.edgesOf(node)) {
            var target = graph.getEdgeSource(edge);

            if (target == node)
                target = graph.getEdgeTarget(edge);

            target.forget(node);
        }
        this.graph.removeVertex(node);
    }

    private void removePriorityNode(PastelNodeBlockEntity node, Priority priority) {
        switch (priority) {
            case MODERATE -> priorityNodes.remove(node);
            case HIGH -> highPriorityNodes.remove(node);
        }
    }

    public boolean hasNodes() {
        for (Set<PastelNodeBlockEntity> nodeList : this.nodes.values()) {
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
        var nodeType = this.nodes.get(type);

        if (priority == Priority.MODERATE) {
            return nodeType.stream().filter(priorityNodes::contains).collect(Collectors.toSet());
        }

        if (priority == Priority.HIGH) {
            return nodeType.stream().filter(highPriorityNodes::contains).collect(Collectors.toSet());
        }

        return nodeType;
    }

    public Map<PastelNodeType, Set<PastelNodeBlockEntity>> getNodes() {
        return this.nodes;
    }

    public int getNodeCount() {
        int nodes = 0;
        for (Set<PastelNodeBlockEntity> nodeList : this.nodes.values()) {
            nodes += nodeList.size();
        }
        return nodes;
    }

    public List<PastelNodeBlockEntity> getAllNodes() {
        List<PastelNodeBlockEntity> nodes = new ArrayList<>();
        for (Map.Entry<PastelNodeType, Set<PastelNodeBlockEntity>> nodeList : this.nodes.entrySet()) {
            nodes.addAll(this.nodes.get(nodeList.getKey()));
        }
        return nodes;
    }

    public boolean canConnect(PastelNodeBlockEntity newNode) {
        if (newNode.getWorld() != this.getWorld()) {
            return false;
        }

        for (Set<PastelNodeBlockEntity> nodeList : this.nodes.values()) {
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

    public PastelNodeBlockEntity getNodeAt(BlockPos blockPos) {
        BlockEntity blockEntity = this.getWorld().getBlockEntity(blockPos);
        if (blockEntity instanceof PastelNodeBlockEntity pastelNodeBlockEntity) {
            return pastelNodeBlockEntity;
        }
        return null;
    }

}
