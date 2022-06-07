package de.dafuqs.spectrum.blocks.pastel_network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PastelNetwork {
	
	/**
	 * MOONSTONE
	 * Giant node. Getting close to it lets the player get items from the network
	 */
	//protected HashSet<InteractionNode> interactionNodes = new ArrayList<>();
	
	protected static List<PastelNetwork> networks = new ArrayList<>();
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
	protected HashSet<PastelNetworkConnectionNode> pastelNetworkConnectionNodes = new HashSet<>();
	
	/**
	 * TOPAZ
	 * Storage (everything that has no target gets put here)
	 */
	protected HashSet<PastelNetworkStorageNodeBlockEntity> pastelNetworkStorageNodes = new HashSet<>();
	
	/**
	 * AMETHYST
	 * Passive Provider (can be requested from)
	 */
	protected HashSet<PastelNetworkProviderNodeBlockEntity> pastelNetworkProviderNodeBlockEntities = new HashSet<>();
	
	/**
	 * CITRINE
	 * Active Provider (pushes items into network)
	 */
	protected HashSet<PastelNetworkPusherNodeBlockEntity> pastelNetworkPusherNodes = new HashSet<>();
	
	/**
	 * ONYX
	 * Requester Nodes, requests on redstone (active>passive>storage)
	 */
	protected HashSet<PastelNetworkPullerNodeBlockEntity> pastelNetworkPullerNodes = new HashSet<>();
	protected String name;
	List<String> networkNames = List.of(
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
	
	public PastelNetwork(World world) {
		this.name = networkNames.get(world.random.nextInt(networkNames.size()));
	}
	
	public static PastelNetwork getNetworkForNewNode(PastelNetworkNodeBlockEntity pastelNetworkNodeBlockEntity) {
		for (PastelNetwork network : networks) {
			if (network.canConnect(pastelNetworkNodeBlockEntity)) {
				return network;
			}
		}
		return new PastelNetwork(pastelNetworkNodeBlockEntity.getWorld());
	}
	
	public void removeNode(PastelNetworkNodeBlockEntity pastelNetworkNodeBlockEntity) {
		if (pastelNetworkNodeBlockEntity instanceof PastelNetworkConnectionNode) {
			pastelNetworkConnectionNodes.remove(pastelNetworkNodeBlockEntity);
		} else if (pastelNetworkNodeBlockEntity instanceof PastelNetworkProviderNodeBlockEntity) {
			pastelNetworkProviderNodeBlockEntities.remove(pastelNetworkNodeBlockEntity);
		} else if (pastelNetworkNodeBlockEntity instanceof PastelNetworkPullerNodeBlockEntity) {
			pastelNetworkPullerNodes.remove(pastelNetworkNodeBlockEntity);
		} else if (pastelNetworkNodeBlockEntity instanceof PastelNetworkPusherNodeBlockEntity) {
			pastelNetworkPusherNodes.remove(pastelNetworkNodeBlockEntity);
		} else if (pastelNetworkNodeBlockEntity instanceof PastelNetworkStorageNodeBlockEntity) {
			pastelNetworkStorageNodes.remove(pastelNetworkNodeBlockEntity);
		}
	}
	
	public boolean canConnect(PastelNetworkNodeBlockEntity newNode) {
		List<PastelNetworkNodeBlockEntity> allNodes = new ArrayList<>();
		allNodes.addAll(this.pastelNetworkConnectionNodes);
		allNodes.addAll(this.pastelNetworkProviderNodeBlockEntities);
		allNodes.addAll(this.pastelNetworkPullerNodes);
		allNodes.addAll(this.pastelNetworkPusherNodes);
		allNodes.addAll(this.pastelNetworkStorageNodes);
		
		for (PastelNetworkNodeBlockEntity currentNode : allNodes) {
			if (currentNode.canSee(newNode)) {
				return true;
			}
		}
		return false;
	}
	
	public void join(PastelNetwork network) {
		this.pastelNetworkConnectionNodes.addAll(network.pastelNetworkConnectionNodes);
		this.pastelNetworkProviderNodeBlockEntities.addAll(network.pastelNetworkProviderNodeBlockEntities);
		this.pastelNetworkPullerNodes.addAll(network.pastelNetworkPullerNodes);
		this.pastelNetworkPusherNodes.addAll(network.pastelNetworkPusherNodes);
		this.pastelNetworkStorageNodes.addAll(network.pastelNetworkStorageNodes);
		networks.remove(network);
	}
	
	public void split() {
		//TODO
	}
	
	
}
