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

	Map<PastelNodeType, List<PastelNodeBlockEntity>> nodes = new HashMap<>();

	protected World world;
	protected String name;

	public PastelNetwork(World world) {
		this.world = world;
		this.name = networkNames.get(world.random.nextInt(networkNames.size()));
		for (PastelNodeType type : PastelNodeType.values()) {
			this.nodes.put(type, new ArrayList<>());
		}
		PastelNetworkManager.add(this);
	}

	public void addNode(PastelNodeBlockEntity node) {
		this.nodes.get(node.getNodeType()).add(node);
	}

	public void removeNode(PastelNodeBlockEntity node) {
		this.nodes.get(node.getNodeType()).remove(node);

		if (!hasNodes()) {
			PastelNetworkManager.remove(this);
		}
	}

	private boolean hasNodes() {
		for (List<PastelNodeBlockEntity> nodeList : this.nodes.values()) {
			if (!nodeList.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public boolean canConnect(PastelNodeBlockEntity newNode) {
		if (newNode.getWorld() != this.world) {
			return false;
		}

		for (List<PastelNodeBlockEntity> nodeList : this.nodes.values()) {
			for (PastelNodeBlockEntity currentNode : nodeList) {
				if (currentNode.canSee(newNode)) {
					return true;
				}
			}
		}
		return false;
	}

	public void join(PastelNetwork network) {
		for (Map.Entry<PastelNodeType, List<PastelNodeBlockEntity>> nodeList : network.getNodes().entrySet()) {
			List<PastelNodeBlockEntity> existingNodes = this.nodes.get(nodeList.getKey());
			for (PastelNodeBlockEntity node : nodeList.getValue()) {
				existingNodes.add(node);
				node.setNetwork(this);
			}
		}
		PastelNetworkManager.remove(network);
	}

	public Map<PastelNodeType, List<PastelNodeBlockEntity>> getNodes() {
		return this.nodes;
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
