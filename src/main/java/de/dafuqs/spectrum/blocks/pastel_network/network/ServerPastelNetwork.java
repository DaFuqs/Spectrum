package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.block.entity.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.graph.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class ServerPastelNetwork extends PastelNetwork<ServerWorld> {
	
	protected final Map<PastelNodeType, Set<PastelNodeBlockEntity>> loadedNodes = new ConcurrentHashMap<>();
	protected final Set<PastelNodeBlockEntity> priorityNodes = new HashSet<>();
	protected final Set<PastelNodeBlockEntity> highPriorityNodes = new HashSet<>();

	// new transfers are checked for every 10 ticks
	private final TickLooper transferLooper = new TickLooper(10);

	protected final SchedulerMap<PastelTransmission> transmissions = new SchedulerMap<>();
	protected final PastelTransmissionLogic transmissionLogic;
	
	public ServerPastelNetwork(ServerWorld world, @Nullable UUID uuid) {
		super(world, uuid);
		for (PastelNodeType type : PastelNodeType.values()) {
			this.loadedNodes.put(type, new HashSet<>());
		}
		this.transmissionLogic = new PastelTransmissionLogic(this);
	}
	
	private boolean addNodeOrReturn(PastelNodeBlockEntity node) {
		return !this.loadedNodes.get(node.getNodeType()).add(node);
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
			if (addNodeOrReturn(node))
				return;
			
			this.graph.addVertex(node.getPos());
		}
		addPriorityNode(node);
	}
	
	/**
	 * Note: this does not check if the nodes can connect, that should be done before calling this method.
	 */
	protected void addNodeAndConnect(PastelNodeBlockEntity newNode, PastelNodeBlockEntity existing) {
		if (addNodeOrReturn(newNode))
			return;
		
		this.graph.addVertex(newNode.getPos());
		getGraph().addEdge(newNode.getPos(), existing.getPos());
		
		// check for priority
		addPriorityNode(newNode);
		
		newNode.setNetworkUUID(this.getUUID());
		SpectrumS2CPacketSender.syncPastelNetworkEdges(this, newNode.getPos());
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
			
			SpectrumS2CPacketSender.syncPastelNetworkRemoved(this);
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
			UUID newNetworkUUID = null;
			Set<PastelNodeBlockEntity> blockEntities = new ObjectArraySet<>();
			for (BlockPos pos : smallerSet) {
				Optional<PastelNodeBlockEntity> blockEntity = world.getBlockEntity(pos, SpectrumBlockEntities.PASTEL_NODE);
				if (blockEntity.isPresent()) {
					disconnectedBEs.add(blockEntity.get());
					blockEntities.add(blockEntity.get());
					if (newNetworkUUID == null) {
						newNetworkUUID = blockEntity.get().getNodeId();
					}
				}
			}
			
			// and create a network for that group
			ServerPastelNetwork newNetwork = Pastel.getServerInstance().createNetwork(world, newNetworkUUID);
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
		
		SpectrumS2CPacketSender.syncPastelNetworkEdges(this, sourcePos);
		this.transmissionLogic.invalidateCache();
	}
	
	protected void incorporate(ServerPastelNetwork networkToIncorporate, BlockPos trackingPos) {
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
		SpectrumS2CPacketSender.syncPastelNetworkEdges(this, trackingPos);
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
			SpectrumS2CPacketSender.syncPastelNetworkEdges(this, node.getPos());
		}
		
	}

	@Override
	public boolean removeEdge(PastelNodeBlockEntity node, PastelNodeBlockEntity otherNode) {
		Optional<ServerPastelNetwork> network = node.getServerNetwork();
		if (network.isEmpty()) {
			throw new IllegalStateException("Attempted to remove an edge from a null network");
		}
		
		Optional<ServerPastelNetwork> otherNetwork = otherNode.getServerNetwork();
		if (otherNetwork.isEmpty() || !network.get().equals(otherNetwork.get())) {
			throw new IllegalArgumentException("Can't remove an edge between nodes in different networks - how did you even do this");
		}
		
		boolean success = super.removeEdge(node, otherNode);
		if (success) {
			checkForNetworkSplit(node.getPos());
			this.transmissionLogic.invalidateCache();
			SpectrumS2CPacketSender.syncPastelNetworkEdges(this, node.getPos());
		}
		
		return success;
	}
	
	@Override
	public boolean addEdge(PastelNodeBlockEntity node, PastelNodeBlockEntity otherNode) {
		Optional<ServerPastelNetwork> network = node.getServerNetwork();
		
		if (network.isEmpty()) {
			throw new IllegalStateException("Attempted to add an edge to a null network");
		}
		
		Optional<ServerPastelNetwork> otherNetwork = otherNode.getServerNetwork();
		if (otherNetwork.isEmpty() || !network.get().equals(otherNetwork.get())) {
			throw new IllegalArgumentException("Can't add an edge between nodes in different networks");
		}
		
		if (node == otherNode || network.get().hasEdge(node.getPos(), otherNode.getPos()))
			return false;
		
		boolean success = super.addEdge(node, otherNode);
		if (success) {
			this.transmissionLogic.invalidateCache();
			SpectrumS2CPacketSender.syncPastelNetworkEdges(this, node.getPos());
		}
		return success;
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

		if (!nodeSync.isEmpty())
			SpectrumS2CPacketSender.sendPastelNodeStatusUpdate(nodeSync, false);
	}
	
	public void addTransmission(PastelTransmission transmission, int travelTime) {
		transmission.setNetwork(this);
		this.transmissions.put(transmission, travelTime);
	}
	
	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putUuid("UUID", this.uuid);
		nbt.putString("World", this.getWorld().getRegistryKey().getValue().toString());
		nbt.put("Graph", graphToNbt());
		nbt.put("Looper", this.transferLooper.toNbt());
		
		NbtList transmissionList = new NbtList();
		for (Map.Entry<PastelTransmission, Integer> transmission : this.transmissions) {
			NbtCompound transmissionCompound = new NbtCompound();
			transmissionCompound.putInt("Delay", transmission.getValue());
			transmissionCompound.put("Transmission", transmission.getKey().toNbt());
			transmissionList.add(transmissionCompound);
		}
		nbt.put("Transmissions", transmissionList);
		return nbt;
	}
	
	public static Optional<ServerPastelNetwork> fromNbt(NbtCompound nbt) {
		UUID uuid = nbt.getUuid("UUID");
		ServerWorld world = SpectrumCommon.minecraftServer.getWorld(RegistryKey.of(RegistryKeys.WORLD, Identifier.tryParse(nbt.getString("World"))));
		ServerPastelNetwork network = new ServerPastelNetwork(world, uuid);
		network.graph = graphFromNbt(nbt.getCompound("Graph"));
		
		if (network.graph.edgeSet().isEmpty()) {
			SpectrumCommon.logError("Tried to load a Pastel Network without any edges");
			return Optional.empty();
		}
		if (network.graph.vertexSet().isEmpty()) {
			SpectrumCommon.logError("Tried to load a Pastel Network without any vertices");
			return Optional.empty();
		}
		
		if (nbt.contains("Looper", NbtElement.COMPOUND_TYPE)) {
			network.transferLooper.readNbt(nbt.getCompound("Looper"));
		}
		
		for (NbtElement e : nbt.getList("Transmissions", NbtElement.COMPOUND_TYPE)) {
			NbtCompound t = (NbtCompound) e;
			int delay = t.getInt("Delay");
			PastelTransmission transmission = PastelTransmission.fromNbt(t.getCompound("Transmission"));
			network.addTransmission(transmission, delay);
		}
		
		return Optional.of(network);
	}
}
