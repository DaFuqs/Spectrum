package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.server.world.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.graph.*;

import java.util.*;

// Persisted together with the overworld
// resetting the overworld will also reset all networks
public class ServerPastelNetworkManager extends PersistentState implements PastelNetworkManager {
	
	private static final String PERSISTENT_STATE_ID = "spectrum_pastel_network_manager";
	
	private final List<ServerPastelNetwork> networks = new ArrayList<>();
	
	public ServerPastelNetworkManager() {
		super();
	}
	
	@Override
	public boolean isDirty() {
		return true;
	}
	
	public static ServerPastelNetworkManager get(ServerWorld world) {
		return world.getPersistentStateManager().getOrCreate(ServerPastelNetworkManager::fromNbt, ServerPastelNetworkManager::new, PERSISTENT_STATE_ID);
	}

	@Override
	public Optional<? extends PastelNetwork> getNetwork(UUID uuid) {
		return networks.stream().filter(n -> n.uuid.equals(uuid)).findFirst();
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		NbtList networkList = new NbtList();
		for (ServerPastelNetwork network : this.networks) {
			NbtCompound compound = network.toNbt();
			networkList.add(compound);
		}
		nbt.put("Networks", networkList);
		return nbt;
	}
	
	public static ServerPastelNetworkManager fromNbt(NbtCompound nbt) {
		ServerPastelNetworkManager manager = new ServerPastelNetworkManager();
		for (NbtElement element : nbt.getList("Networks", NbtElement.COMPOUND_TYPE)) {
			var compound = (NbtCompound) element;
			World world = SpectrumCommon.minecraftServer.getWorld(RegistryKey.of(RegistryKeys.WORLD, Identifier.tryParse(compound.getString("World"))));
			UUID uuid = compound.getUuid("UUID");
			var network = new ServerPastelNetwork(world, uuid);
			network.fromNbt(compound);
			manager.networks.add(network);
		}
		return manager;
	}
	
	@Override
	public ServerPastelNetwork createNetwork(World world, @Nullable UUID uuid) {
		ServerPastelNetwork network = new ServerPastelNetwork(world, uuid);
		this.networks.add(network);
		return network;
	}
	
	public void tick() {
		// using a for here instead of foreach
		// to prevent ConcurrentModificationExceptions
		//noinspection ForLoopReplaceableByForEach
		for (int i = 0; i < this.networks.size(); i++) {
			this.networks.get(i).tick();
		}
	}

	@Override
	@Contract("_, null -> new")
	public PastelNetwork joinOrCreateNetwork(PastelNodeBlockEntity node, @Nullable UUID uuid) {
		if (uuid != null) {
			//noinspection ForLoopReplaceableByForEach
			for (int i = 0; i < this.networks.size(); i++) {
				PastelNetwork network = this.networks.get(i);
				if (network.getUUID().equals(uuid)) {
					network.addNode(node);
					return network;
				}
			}
		}
		
		ServerPastelNetwork network = createNetwork(node.getWorld(), uuid);
		network.addNode(node);
		return network;
	}

	@Override
	public void connectNodes(PastelNodeBlockEntity node, PastelNodeBlockEntity parent, @NotNull UUID id) {
		PastelNetwork mainNetwork, yieldingNetwork;

		if (parent.getParentNetwork() != null) {
			mainNetwork = parent.getParentNetwork();
			yieldingNetwork = node.getParentNetwork();

			if (yieldingNetwork == null) {
				mainNetwork.addNodeAndConnect(node, parent);
				node.setParentNetwork(mainNetwork);
				return;
			}
		}
		else if (node.getParentNetwork() != null) {
			mainNetwork = node.getParentNetwork();
			yieldingNetwork = parent.getParentNetwork();

			if (yieldingNetwork == null) {
				mainNetwork.addNodeAndConnect(parent, node);
				parent.setParentNetwork(mainNetwork);
				return;
			}
		}
		else {
			mainNetwork = createNetwork(node.getWorld(), id);
			mainNetwork.addNode(parent);
			parent.setParentNetwork(mainNetwork);
			mainNetwork.addNodeAndConnect(node, parent);
			node.setParentNetwork(mainNetwork);
			return;
		}

		if (mainNetwork == yieldingNetwork) {
			return;
		}

		mainNetwork.incorporate(yieldingNetwork, node, parent);
		this.networks.remove(yieldingNetwork);
	}

	@Override
	public void removeNode(PastelNodeBlockEntity node, NodeRemovalReason reason) {
		ServerPastelNetwork network = (ServerPastelNetwork) node.getParentNetwork();
		if (network != null) {
			network.removeNode(node, reason);
			
			if (reason == NodeRemovalReason.UNLOADED)
				return;
			
			if (network.hasNodes()) {
				// check if the removed node split the network into subnetworks
				checkForNetworkSplit(network);
			} else if (reason.destructive) {
				this.networks.remove(network);
			}
		}
	}
	
}
