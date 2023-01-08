package de.dafuqs.spectrum.blocks.pastel_network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public class PastelNetwork {

	protected static final List<String> networkNames = List.of(
			"Alpha",
			"Beta",
			"Gamma",
			"Delta",
			"Epsilon",
			"Zeta",
			"Eta",
			"Theta",
			"Iota",
			"Kappa",
			"Lambda",
			"My",
			"Ny",
			"Xi",
			"Omikron",
			"Pi",
			"Rho",
			"Sigma",
			"Tau",
			"Ypsilon",
			"Phi",
			"Chi",
			"Psi",
			"Omeg"
	);

	/**
	 * MOONSTONE
	 * Giant node. Getting close to it lets the player get items from the network
	 */
	//protected HashSet<InteractionNode> interactionNodes = new ArrayList<>();

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
	protected HashSet<PastelConnectionNodeBlockEntity> connectionNodes = new HashSet<>();

	/**
	 * TOPAZ
	 * Storage (everything that has no target gets put here)
	 */
	protected HashSet<PastelStorageNodeBlockEntity> pastelNetworkStorageNodes = new HashSet<>();

	/**
	 * AMETHYST
	 * Passive Provider (can be requested from)
	 */
	protected HashSet<PastelProviderNodeBlockEntity> pastelNetworkProviderNodeBlockEntities = new HashSet<>();

	/**
	 * CITRINE
	 * Active Provider (pushes items into network)
	 */
	protected HashSet<PastelPusherNodeBlockEntity> pastelNetworkPusherNodes = new HashSet<>();

	/**
	 * ONYX
	 * Requester Nodes, requests on redstone (active>passive>storage)
	 */
	protected HashSet<PastelPullerNodeBlockEntity> pastelNetworkPullerNodes = new HashSet<>();

	protected World world;
	protected String name;

	public PastelNetwork(World world) {
		this.world = world;
		this.name = networkNames.get(world.random.nextInt(networkNames.size()));
		PastelNetworkManager.add(this);
	}

	public void removeNode(PastelNodeBlockEntity pastelNodeBlockEntity) {
		if (pastelNodeBlockEntity instanceof PastelConnectionNodeBlockEntity) {
			connectionNodes.remove(pastelNodeBlockEntity);
		} else if (pastelNodeBlockEntity instanceof PastelProviderNodeBlockEntity) {
			pastelNetworkProviderNodeBlockEntities.remove(pastelNodeBlockEntity);
		} else if (pastelNodeBlockEntity instanceof PastelPullerNodeBlockEntity) {
			pastelNetworkPullerNodes.remove(pastelNodeBlockEntity);
		} else if (pastelNodeBlockEntity instanceof PastelPusherNodeBlockEntity) {
			pastelNetworkPusherNodes.remove(pastelNodeBlockEntity);
		} else if (pastelNodeBlockEntity instanceof PastelStorageNodeBlockEntity) {
			pastelNetworkStorageNodes.remove(pastelNodeBlockEntity);
		}
	}

	public boolean canConnect(PastelNodeBlockEntity newNode) {
		if (newNode.getWorld() != this.world) {
			return false;
		}

		List<PastelNodeBlockEntity> allNodes = new ArrayList<>();
		allNodes.addAll(this.connectionNodes);
		allNodes.addAll(this.pastelNetworkProviderNodeBlockEntities);
		allNodes.addAll(this.pastelNetworkPullerNodes);
		allNodes.addAll(this.pastelNetworkPusherNodes);
		allNodes.addAll(this.pastelNetworkStorageNodes);

		for (PastelNodeBlockEntity currentNode : allNodes) {
			if (currentNode.canSee(newNode)) {
				return true;
			}
		}
		return false;
	}

	public void join(PastelNetwork network) {
		this.connectionNodes.addAll(network.connectionNodes);
		this.pastelNetworkProviderNodeBlockEntities.addAll(network.pastelNetworkProviderNodeBlockEntities);
		this.pastelNetworkPullerNodes.addAll(network.pastelNetworkPullerNodes);
		this.pastelNetworkPusherNodes.addAll(network.pastelNetworkPusherNodes);
		this.pastelNetworkStorageNodes.addAll(network.pastelNetworkStorageNodes);
		PastelNetworkManager.remove(network);
	}

	public List<PastelNodeBlockEntity> getAllNodes() {
		List<PastelNodeBlockEntity> nodes = new ArrayList<>();
		nodes.addAll(connectionNodes);
		nodes.addAll(pastelNetworkProviderNodeBlockEntities);
		nodes.addAll(pastelNetworkPullerNodes);
		nodes.addAll(pastelNetworkPusherNodes);
		nodes.addAll(pastelNetworkStorageNodes);
		return nodes;
	}

	public void split() {
		//TODO
	}

	public void tick() {

	}

	private final SchedulerMap<BlockPos> particleCooldowns = new SchedulerMap<>();

	protected final boolean queueParticle(BlockPos blockPos) {
		if (!particleCooldowns.containsKey(blockPos)) {
			particleCooldowns.put(blockPos, 3);
			return true;
		}
		return false;
	}

	public String getName() {
		return this.name;
	}

}
