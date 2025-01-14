package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.block.entity.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.graph.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class ServerPastelNetwork extends PastelNetwork {
	
	protected final Map<PastelNodeType, Set<PastelNodeBlockEntity>> loadedNodes = new ConcurrentHashMap<>();
	protected final Set<PastelNodeBlockEntity> priorityNodes = new HashSet<>();
	protected final Set<PastelNodeBlockEntity> highPriorityNodes = new HashSet<>();

	// new transfers are checked for every 10 ticks
	private final TickLooper transferLooper = new TickLooper(10);

	protected final SchedulerMap<PastelTransmission> transmissions = new SchedulerMap<>();
	protected final PastelTransmissionLogic transmissionLogic;

	public ServerPastelNetwork(World world, @Nullable UUID uuid) {
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
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(this.uuid.toString());
		for (PastelNodeType type : PastelNodeType.values()) {
			builder.append("-").append(getNodes(type).size());
		}
		return builder.toString();
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
	
	private void removePriorityNode(PastelNodeBlockEntity node, NodePriority priority) {
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
		return getNodes(type, NodePriority.GENERIC);
	}
	
	public Set<PastelNodeBlockEntity> getNodes(PastelNodeType type, NodePriority priority) {
		var nodeType = this.loadedNodes.get(type);
		
		if (priority == NodePriority.MODERATE) {
			return nodeType.stream().filter(priorityNodes::contains).collect(Collectors.toSet());
		}
		
		if (priority == NodePriority.HIGH) {
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
	
	public void addNode(PastelNodeBlockEntity node) {
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
	public void addNodeAndConnect(PastelNodeBlockEntity newNode, PastelNodeBlockEntity parent) {
		if (addNodeOrReturn(newNode))
			return;
		
		this.graph.addVertex(newNode.getPos());
		getGraph().addEdge(newNode.getPos(), parent.getPos());
		
		// check for priority
		addPriorityNode(newNode);
	}
	
	void checkForNetworkSplit() {
		ConnectivityInspector<BlockPos, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(getGraph());
		List<Set<BlockPos>> connectedSets = connectivityInspector.connectedSets();
		if (connectedSets.size() != 1) {
			for (int i = 1; i < connectedSets.size(); i++) {
				Set<BlockPos> disconnectedNodes = connectedSets.get(i);
				Map<BlockPos, List<BlockPos>> transitiveEdges = new HashMap<>();
				ServerPastelNetwork newNetwork = Pastel.getServerInstance().createNetwork(world, ((PastelNodeBlockEntity) (world.getBlockEntity(disconnectedNodes.iterator().next()))).getNodeId());
				for (BlockPos disconnectedNode : disconnectedNodes) {
					for (DefaultEdge switchedEdge : getGraph().edgesOf(disconnectedNode)) {
						var edgeList = transitiveEdges.computeIfAbsent(disconnectedNode, p -> new ArrayList<>());
						var target = graph.getEdgeTarget(switchedEdge);
						if (!target.equals(disconnectedNode) && !edgeList.contains(target) && disconnectedNodes.contains(target))
							edgeList.add(target);
					}
				}
				for (BlockPos disconnectedNode : disconnectedNodes) {
					var switchedNode = getWorld().getBlockEntity(disconnectedNode);
					if (switchedNode instanceof PastelNodeBlockEntity pastelNode) {
						removeNode(pastelNode, NodeRemovalReason.DISCONNECT);
						newNetwork.addNode(pastelNode);
						pastelNode.setNetworkUUID(newNetwork.getUUID());
					}
				}
				for (BlockPos node : transitiveEdges.keySet()) {
					for (BlockPos target : transitiveEdges.get(node)) {
						if (!newNetwork.graph.containsEdge(node, target))
							newNetwork.addEdge(node, target);
					}
				}
			}
		}
	}
	
	public void incorporate(ServerPastelNetwork networkToIncorporate, PastelNodeBlockEntity node, PastelNodeBlockEntity otherNode) {
		for (Map.Entry<PastelNodeType, Set<PastelNodeBlockEntity>> nodesToIncorporate : networkToIncorporate.getLoadedNodes().entrySet()) {
			PastelNodeType type = nodesToIncorporate.getKey();
			for (PastelNodeBlockEntity nodeToIncorporate : nodesToIncorporate.getValue()) {
				this.loadedNodes.get(type).add(nodeToIncorporate);
				updateNodePriority(nodeToIncorporate, nodeToIncorporate.getPriority());
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
		addEdge(node, otherNode);
		
		this.transmissionLogic.invalidateCache();
	}
	
	public boolean removeNode(PastelNodeBlockEntity node, NodeRemovalReason reason) {
		if (!graph.containsVertex(node.getPos())) {
			return false;
		}
		
		// delete the now removed node from this networks graph - IF IT WASN'T UNLOADED
		if (reason != NodeRemovalReason.UNLOADED)
			graph.removeVertex(node.getPos());
		
		this.loadedNodes.get(node.getNodeType()).remove(node);
		removePriorityNode(node, node.getPriority());
		
		this.transmissionLogic.invalidateCache();
		SpectrumS2CPacketSender.syncPastelNetworkEdges(this, node.getPos());
		
		return true;
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
			checkForNetworkSplit();
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
	
	public void tick() {
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
	
	public static ServerPastelNetwork fromNbt(NbtCompound nbt) {
		UUID uuid = nbt.getUuid("UUID");
		World world = SpectrumCommon.minecraftServer.getWorld(RegistryKey.of(RegistryKeys.WORLD, Identifier.tryParse(nbt.getString("World"))));
		ServerPastelNetwork network = new ServerPastelNetwork(world, uuid);
		network.graph = graphFromNbt(nbt.getCompound("Graph"));
		
		if (nbt.contains("Looper", NbtElement.COMPOUND_TYPE)) {
			network.transferLooper.readNbt(nbt.getCompound("Looper"));
		}
		
		for (NbtElement e : nbt.getList("Transmissions", NbtElement.COMPOUND_TYPE)) {
			NbtCompound t = (NbtCompound) e;
			int delay = t.getInt("Delay");
			PastelTransmission transmission = PastelTransmission.fromNbt(t.getCompound("Transmission"));
			network.addTransmission(transmission, delay);
		}
		
		return network;
	}
}
