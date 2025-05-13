package de.dafuqs.spectrum.blocks.pastel_network.network;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.block.entity.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.graph.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class ServerPastelNetwork extends PastelNetwork<ServerWorld> {
	
	public static final Codec<ServerPastelNetwork> CODEC = RecordCodecBuilder.create(i -> i.group(
			World.CODEC.xmap(k -> SpectrumCommon.minecraftServer.getWorld(k), World::getRegistryKey).fieldOf("world").forGetter(b -> b.world),
			Uuids.CODEC.fieldOf("uuid").forGetter(ServerPastelNetwork::getUUID),
			Codec.INT.fieldOf("color").forGetter(ServerPastelNetwork::getColor),
			TickLooper.CODEC.fieldOf("looper").forGetter(b -> b.transferLooper)
	).apply(i, ServerPastelNetwork::new));
	
	protected final Map<PastelNodeType, Set<PastelNodeBlockEntity>> loadedNodes = new ConcurrentHashMap<>();
	protected final Set<PastelNodeBlockEntity> priorityNodes = new HashSet<>();
	protected final Set<PastelNodeBlockEntity> highPriorityNodes = new HashSet<>();

	// new transfers are checked for every 10 ticks
	private final TickLooper transferLooper;
	protected final SchedulerMap<PastelTransmission> transmissions;
	protected final PastelTransmissionLogic transmissionLogic;
	
	public ServerPastelNetwork(ServerWorld world, UUID uuid, int color) {
		this(world, uuid, color, new TickLooper(10));
	}
	
	public ServerPastelNetwork(ServerWorld world, PastelNodeBlockEntity initialNode) {
		this(world, initialNode.getNodeId(), initialNode.getPastelNetworkColor(), new TickLooper(10));
		addNode(initialNode);
	}
	
	public ServerPastelNetwork(ServerWorld world, UUID uuid, int color, TickLooper transferLoop) {
		super(world, uuid, color);
		this.transferLooper = transferLoop;
		this.transmissions = new SchedulerMap<>();
		this.transmissionLogic = new PastelTransmissionLogic(this);
		
		for (PastelNodeType type : PastelNodeType.values()) {
			this.loadedNodes.put(type, new HashSet<>());
		}
		for (var entry : transmissions) {
			entry.getKey().setNetwork(this);
		}
	}
	
	private boolean addLoadedNode(PastelNodeBlockEntity node) {
		return !this.loadedNodes.get(node.getNodeType()).add(node);
	}
	
	public void initializeNode(PastelNodeBlockEntity node) {
		var type = this.loadedNodes.get(node.getNodeType());
		if (!type.contains(node)) {
			type.add(node);
			addPriorityNode(node);
		}
	}
	
	private void addPriorityNode(PastelNodeBlockEntity node) {
		switch (node.getPriority()) {
			case MODERATE -> priorityNodes.add(node);
			case HIGH -> highPriorityNodes.add(node);
		}
	}
	
	public void updateNodePriority(PastelNodeBlockEntity node, NodePriority oldPriority) {
		removePriorityNode(node, oldPriority);
		addPriorityNode(node);
	}
	
	@Override
	public String getNodeDebugText() {
		return super.getNodeDebugText() +
				" - Prov: " +
				getLoadedNodes(PastelNodeType.PROVIDER).size() +
				" - Send: " +
				getLoadedNodes(PastelNodeType.SENDER).size() +
				" - Gath: " +
				getLoadedNodes(PastelNodeType.GATHER).size() +
				" - Stor: " +
				getLoadedNodes(PastelNodeType.STORAGE).size() +
				" - Buff: " +
				getLoadedNodes(PastelNodeType.BUFFER).size() +
				" - Conn: " +
				getLoadedNodes(PastelNodeType.CONNECTION).size();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(this.uuid.toString());
		for (PastelNodeType type : PastelNodeType.values()) {
			builder.append("-").append(getLoadedNodes(type).size());
		}
		return builder.toString();
	}
	
	protected @Nullable PastelNodeBlockEntity getLoadedNodeAt(BlockPos blockPos) {
		if (!this.graph.vertexSet().contains(blockPos)) {
			return null; // the network might have been disconnected while the transfer was underway
		}
		if (!this.getWorld().isChunkLoaded(blockPos)) {
			return null; // hmmmm
		}
		
		BlockEntity blockEntity = this.getWorld().getBlockEntity(blockPos);
		if (blockEntity instanceof PastelNodeBlockEntity pastelNodeBlockEntity) {
			return pastelNodeBlockEntity;
		}
		return null;
	}
	
	private void removePriorityNode(PastelNodeBlockEntity node, NodePriority priority) {
		switch (priority) {
			case MODERATE -> priorityNodes.remove(node);
			case HIGH -> highPriorityNodes.remove(node);
		}
	}
	
	public Set<PastelNodeBlockEntity> getLoadedNodes(PastelNodeType type) {
		return getLoadedNodes(type, NodePriority.GENERIC);
	}
	
	public Set<PastelNodeBlockEntity> getLoadedNodes(PastelNodeType type, NodePriority priority) {
		var nodeType = this.loadedNodes.get(type);
		
		if (priority == NodePriority.MODERATE) {
			return nodeType.stream().filter(priorityNodes::contains).collect(Collectors.toSet());
		}
		
		if (priority == NodePriority.HIGH) {
			return nodeType.stream().filter(highPriorityNodes::contains).collect(Collectors.toSet());
		}
		
		return nodeType;
	}
	
	protected void addNode(PastelNodeBlockEntity node) {
		//If this node already has a vertex, then all we are doing it is loading it
		if (graph.containsVertex(node.getPos())) {
			loadedNodes.get(node.getNodeType()).add(node);
			
		} else {
			if (addLoadedNode(node))
				return;
			
			this.graph.addVertex(node.getPos());
		}
		addPriorityNode(node);
		node.setNetworkUUID(this.getUUID());
	}
	
	/**
	 * Note: this does not check if the nodes can connect, that should be done before calling this method.
	 */
	protected void addNodeAndConnect(PastelNodeBlockEntity newNode, PastelNodeBlockEntity existing) {
		if (addLoadedNode(newNode))
			return;
		
		this.graph.addVertex(newNode.getPos());
		getGraph().addEdge(newNode.getPos(), existing.getPos());
		
		// check for priority
		addPriorityNode(newNode);
		
		newNode.setNetworkUUID(this.getUUID());
		PastelNetworkEdgeSyncPayload.send(this, newNode.getPos());
	}
	
	// check if a recently removed node split the network into subnetworks
	// or if the network is too small now (0 / 1 nodes) delete it
	private void checkForNetworkSplit(BlockPos sourcePos) {
		Set<BlockPos> vertices = this.graph.vertexSet();
		
		// if our network now has 1 or less nodes we don't really have a network anymore
		if (this.graph.vertexSet().size() < 2) {
			for (BlockPos vertex : vertices) {
				Optional<PastelNodeBlockEntity> be = world.getBlockEntity(vertex, SpectrumBlockEntities.PASTEL_NODE);
				be.ifPresent(pastelNodeBlockEntity -> pastelNodeBlockEntity.setNetworkUUID(null));
			}
			Pastel.getServerInstance().removeNetwork(this.getUUID());
		}
		
		// if our network is still big enough, check if all vertices are still interconnected
		// if not, create new networks for all split ones
		ConnectivityInspector<BlockPos, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(graph);
		List<Set<BlockPos>> connectedSets = connectivityInspector.connectedSets();
		
		if (connectedSets.size() == 1) {
			// all nodes still are connected in some way, awesome!
			return;
		}
		
		// we search the biggest set
		// that one will inherit the current network id
		// all other sets will be split into their own networks down the line
		int biggestSetSize = 0;
		Set<BlockPos> biggestSet = null;
		List<Set<BlockPos>> smallerSets = new ArrayList<>();
		for (Set<BlockPos> set : connectedSets) {
			int size = set.size();
			if (size > biggestSetSize) {
				biggestSetSize = size;
				if (biggestSet != null) {
					smallerSets.add(biggestSet);
				}
				biggestSet = set;
			} else {
				smallerSets.add(set);
			}
		}
		
		if (biggestSetSize == 1) {
			// the network was split into something you can't even call a network anymore
			// fast fail => remove the network entirely
			
			for (BlockPos pos : this.graph.vertexSet()) {
				Optional<PastelNodeBlockEntity> blockEntity = world.getBlockEntity(pos, SpectrumBlockEntities.PASTEL_NODE);
				blockEntity.ifPresent(pastelNodeBlockEntity -> pastelNodeBlockEntity.setNetworkUUID(null));
			}
			
			PastelNetworkRemovedPayload.send(this);
			return;
		}
		
		Set<PastelNodeBlockEntity> disconnectedBEs = new ObjectOpenHashSet<>();
		// we keep the first entry as our leftover network, hence we start at (1)
		for (Set<BlockPos> smallerSet : smallerSets) {
			// is it a single lone node?
			// => remove from network, do not create a new one just for that one
			boolean isSingleNode = smallerSet.size() == 1;
			if (isSingleNode) {
				Optional<PastelNodeBlockEntity> blockEntity = world.getBlockEntity(smallerSet.stream().findAny().get(), SpectrumBlockEntities.PASTEL_NODE);
				if (blockEntity.isPresent()) {
					blockEntity.get().setNetworkUUID(null);
					disconnectedBEs.add(blockEntity.get());
				}
				continue;
			}
			
			// this part of the network has at least 2 nodes
			// => collect all nodes and pick a network UUID
			PastelNodeBlockEntity initialNode = null;
			Set<PastelNodeBlockEntity> blockEntities = new ObjectArraySet<>();
			for (BlockPos pos : smallerSet) {
				Optional<PastelNodeBlockEntity> blockEntity = world.getBlockEntity(pos, SpectrumBlockEntities.PASTEL_NODE);
				if (blockEntity.isPresent()) {
					disconnectedBEs.add(blockEntity.get());
					blockEntities.add(blockEntity.get());
					if (initialNode == null) {
						initialNode = blockEntity.get();
					}
				}
			}
			
			// and create a network for that group
			ServerPastelNetwork newNetwork = Pastel.getServerInstance().createNetwork(world, initialNode);
			Map<BlockPos, BlockPos> edges = new Object2ObjectArrayMap<>();
			for (BlockPos disconnectedNode : smallerSet) {
				for (DefaultEdge edge : this.graph.edgesOf(disconnectedNode)) {
					edges.put(this.graph.getEdgeSource(edge), this.graph.getEdgeTarget(edge));
				}
				var couldBeANode = getWorld().getBlockEntity(disconnectedNode, SpectrumBlockEntities.PASTEL_NODE);
				if (couldBeANode.isPresent()) {
					PastelNodeBlockEntity pastelNode = couldBeANode.get();
					newNetwork.addNode(pastelNode);
					pastelNode.setNetworkUUID(newNetwork.getUUID());
				}
			}
			for (Map.Entry<BlockPos, BlockPos> edge : edges.entrySet()) {
				newNetwork.addEdge(edge.getKey(), edge.getValue());
			}
		}
		
		for (PastelNodeBlockEntity p : disconnectedBEs) {
			this.removeNode(p, NodeRemovalReason.REMOVED);
		}
		
		PastelNetworkEdgeSyncPayload.send(this, sourcePos);
		this.transmissionLogic.invalidateCache();
	}
	
	public void incorporate(ServerPastelNetwork networkToIncorporate, BlockPos trackingPos) {
		for (Map.Entry<PastelNodeType, Set<PastelNodeBlockEntity>> nodesToIncorporate : networkToIncorporate.loadedNodes.entrySet()) {
			for (PastelNodeBlockEntity nodeToIncorporate : nodesToIncorporate.getValue()) {
				addNode(nodeToIncorporate);
			}
		}
		networkToIncorporate.graph.vertexSet().forEach(pos -> {
			if (this.world.getBlockEntity(pos) instanceof PastelNodeBlockEntity switchNode) {
				switchNode.setNetworkUUID(this.uuid);
			}
			graph.addVertex(pos);
		});
		networkToIncorporate.graph.edgeSet().forEach(edge -> {
			graph.addEdge(networkToIncorporate.getGraph().getEdgeSource(edge), networkToIncorporate.getGraph().getEdgeTarget(edge));
		});
		
		Pastel.getServerInstance().removeNetwork(networkToIncorporate.getUUID());
		this.transmissionLogic.invalidateCache();
	}
	
	protected void removeNode(PastelNodeBlockEntity node, NodeRemovalReason reason) {
		if (!graph.containsVertex(node.getPos())) {
			return;
		}
		
		// delete the now removed node from this network graph - IF IT WASN'T UNLOADED
		if (reason != NodeRemovalReason.UNLOADED)
			graph.removeVertex(node.getPos());
		
		this.loadedNodes.get(node.getNodeType()).remove(node);
		removePriorityNode(node, node.getPriority());
		
		if (reason.checksForNetworkSplit) {
			checkForNetworkSplit(node.getPos());
			PastelNetworkEdgeSyncPayload.send(this, node.getPos());
		}
		if (reason != NodeRemovalReason.REMOVED)
			node.setNetworkUUID(null);
	}
	
	@Override
	public boolean addEdge(PastelNodeBlockEntity existingNode, PastelNodeBlockEntity newNode) {
		Optional<ServerPastelNetwork> network = existingNode.getServerNetwork();
		
		if (network.isEmpty()) {
			throw new IllegalStateException("Attempted to add an edge to a null network");
		}
		if (network.get() != this) {
			throw new IllegalStateException("Attempted to add an edge to a foreign network");
		}
		
		Optional<ServerPastelNetwork> otherNetwork = newNode.getServerNetwork();
		if (otherNetwork.isPresent() && !otherNetwork.equals(network)) {
			throw new IllegalArgumentException("Can't add an edge between nodes in different networks");
		}
		if (existingNode == newNode) {
			throw new IllegalStateException("Attempted to connect a node to itself");
		}
		
		if (network.get().hasEdge(existingNode.getPos(), newNode.getPos())) {
			throw new IllegalStateException("Attempted to add an edge that already exists");
		}
		
		addNode(newNode);
		
		return super.addEdge(existingNode, newNode);
	}
	
	public void markDirty(BlockPos syncPos) {
		this.transmissionLogic.invalidateCache();
		PastelNetworkEdgeSyncPayload.send(this, syncPos);
	}
	
	protected void tick() {
		this.transmissions.tick();
		var priority = NodePriority.GENERIC;

		if (transferLooper.getTick() % 5 == 0) {
			priority = NodePriority.MODERATE;
		}
		else if (transferLooper.getTick() % 2 == 0) {
			priority = NodePriority.HIGH;
		}

		this.transferLooper.tick();
		var cap = transferLooper.reachedCap();
		if (cap || priority != NodePriority.GENERIC) {
			if (cap) {
				this.transferLooper.reset();
			}
			try {
				this.transmissionLogic.tick(priority);
			} catch (Exception e) {
				// hmmmmmm. Block getting unloaded / new one placed while logic is running?
			}
		}
		tickNodeEffects();
	}
	
	public Map<PastelTransmission, Integer> getTransmissions() {
		return transmissions.getMap();
	}
	
	private void tickNodeEffects() {
		List<PastelNodeBlockEntity> nodeSync = new ArrayList<>();


		for (Map.Entry<PastelTransmission, Integer> transPair : transmissions) {
			var transmission = transPair.getKey();
			var remainingTravelTime = transPair.getValue();
			var nodes = transmission.getNodePositions();

			if (nodes.isEmpty())
				continue;

			var travelTime = transmission.getTransmissionDuration();
			double progress = travelTime - remainingTravelTime;

			if (progress != 0 && progress % transmission.getVertexTime() == 0) {
				var node = world.getBlockEntity(nodes.get((int) Math.round((nodes.size() - 1) * progress / travelTime)));

				if (!(node instanceof PastelNodeBlockEntity pastelNode))
					continue;

				nodeSync.add(pastelNode);
				if (pastelNode.isSensor())
					pastelNode.notifySensor();
			}
		}
		
		if (!nodeSync.isEmpty()) {
			PastelNodeStatusUpdatePayload.sendPastelNodeStatusUpdate(nodeSync, false);
		}
	}
	
	public void addTransmission(PastelTransmission transmission, int travelTime) {
		transmission.setNetwork(this);
		this.transmissions.put(transmission, travelTime);
	}
	
}
