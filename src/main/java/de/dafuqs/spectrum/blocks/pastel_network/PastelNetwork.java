package de.dafuqs.spectrum.blocks.pastel_network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PastelNetwork {

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
	
	/**
	 * Node network:
	 * - Need to be placed on inventories (sided?)
	 * - All components can be dyed (pigment, crafting or right-clicking)
	 * - A certain colored node can only interact with the nodes it composites into, but not vice-versa (blue => cyan, but not cyan => blue)
	 * - Connected on sight, like CC nodes
	 * - Use Mermaids gem on node to transform it into fluid node
	 *
	 * CLEAR
	 * Basic connection node, not interacting actively ("connectors")
	 */
	protected List<PastelNetworkConnectionNode> pastelNetworkConnectionNodes = new ArrayList<>();
	
	/**
	 * TOPAZ
	 * Storage (everything that has no target gets put here)
	 */
	protected List<PastelNetworkStorageNode> pastelNetworkStorageNodes = new ArrayList<>();
	
	/**
	 * AMETHYST
	 * Passive Provider (can be requested from)
	 */
	protected List<PastelNetworkProviderNode> pastelNetworkProviderNodes = new ArrayList<>();
	
	/**
	 * CITRINE
	 * Active Provider (pushes items into network)
	 */
	protected List<PastelNetworkPusherNode> pastelNetworkPusherNodes = new ArrayList<>();
	
	/**
	 * ONYX
	 * Requester Nodes, requests on redstone (active>passive>storage)
	 */
	protected List<PastelNetworkPullerNode> pastelNetworkPullerNodes = new ArrayList<>();
	
	/**
	 * MOONSTONE
	 * Giant node. Getting close to it lets the player get items from the network
	 */
	//protected List<InteractionNode> interactionNodes = new ArrayList<>();
	
	protected static List<PastelNetwork> networks = new ArrayList<>();
	
	protected String name;
	
	public static PastelNetwork getNetworkForNewNode(PastelNetworkNodeBlockEntity pastelNetworkNodeBlockEntity) {
		for(PastelNetwork network : networks) {
			if(network.canConnect(pastelNetworkNodeBlockEntity)) {
				return network;
			}
		}
		return new PastelNetwork(pastelNetworkNodeBlockEntity.getWorld());
	}
	
	public void removeNode(PastelNetworkNodeBlockEntity pebbleNetworkNodeBlock) {
		if(pebbleNetworkNodeBlock instanceof PastelNetworkConnectionNode) {
			pastelNetworkConnectionNodes.remove(pebbleNetworkNodeBlock);
		} else if(pebbleNetworkNodeBlock instanceof PastelNetworkProviderNode) {
			pastelNetworkProviderNodes.remove(pebbleNetworkNodeBlock);
		} else if(pebbleNetworkNodeBlock instanceof PastelNetworkPullerNode) {
			pastelNetworkPullerNodes.remove(pebbleNetworkNodeBlock);
		} else if(pebbleNetworkNodeBlock instanceof PastelNetworkPusherNode) {
			pastelNetworkPusherNodes.remove(pebbleNetworkNodeBlock);
		} else if(pebbleNetworkNodeBlock instanceof PastelNetworkStorageNode) {
			pastelNetworkStorageNodes.remove(pebbleNetworkNodeBlock);
		}
	}
	
	public boolean canConnect(PastelNetworkNodeBlockEntity newNode) {
		List<PastelNetworkNodeBlockEntity> allNodes = new ArrayList<>();
		allNodes.addAll(this.pastelNetworkConnectionNodes);
		allNodes.addAll(this.pastelNetworkProviderNodes);
		allNodes.addAll(this.pastelNetworkPullerNodes);
		allNodes.addAll(this.pastelNetworkPusherNodes);
		allNodes.addAll(this.pastelNetworkStorageNodes);
		
		for(PastelNetworkNodeBlockEntity currentNode : allNodes) {
			if(currentNode.canSee(newNode)) {
				return true;
			}
		}
		return false;
	}
	
	public PastelNetwork(World world) {
		this.name = networkNames.get(world.random.nextInt(networkNames.size()));
	}
	
	public void join(PastelNetwork network) {
		this.pastelNetworkConnectionNodes.addAll(network.pastelNetworkConnectionNodes);
		this.pastelNetworkProviderNodes.addAll(network.pastelNetworkProviderNodes);
		this.pastelNetworkPullerNodes.addAll(network.pastelNetworkPullerNodes);
		this.pastelNetworkPusherNodes.addAll(network.pastelNetworkPusherNodes);
		this.pastelNetworkStorageNodes.addAll(network.pastelNetworkStorageNodes);
		networks.remove(network);
	}
	
	public void split() {
		//TODO
	}
	
	
}
