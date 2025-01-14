package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.networking.*;
import net.minecraft.nbt.*;
import net.minecraft.server.world.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

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
	public Optional<ServerPastelNetwork> getNetwork(UUID uuid) {
		return networks.stream().filter(n -> n.uuid.equals(uuid)).findFirst();
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		NbtList networkList = new NbtList();
		for (ServerPastelNetwork network : this.networks) {
			networkList.add(network.toNbt());
		}
		nbt.put("Networks", networkList);
		return nbt;
	}
	
	public static ServerPastelNetworkManager fromNbt(NbtCompound nbt) {
		ServerPastelNetworkManager manager = new ServerPastelNetworkManager();
		for (NbtElement element : nbt.getList("Networks", NbtElement.COMPOUND_TYPE)) {
			manager.networks.add(ServerPastelNetwork.fromNbt((NbtCompound) element));
		}
		return manager;
	}
	
	@Override
	public ServerPastelNetwork createNetwork(World world, UUID uuid) {
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

	@Contract("_, null -> new")
	public PastelNetwork joinOrCreateNetwork(PastelNodeBlockEntity node, @Nullable UUID uuid) {
		if (uuid != null) {
			//noinspection ForLoopReplaceableByForEach
			for (int i = 0; i < this.networks.size(); i++) {
				ServerPastelNetwork network = this.networks.get(i);
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
	
	public void connectNodes(PastelNodeBlockEntity node, PastelNodeBlockEntity parent) {
		Optional<ServerPastelNetwork> mainNetwork, yieldingNetwork;
		
		if (parent.getServerNetwork().isPresent()) {
			mainNetwork = parent.getServerNetwork();
			yieldingNetwork = node.getServerNetwork();
			
			if (yieldingNetwork.isEmpty()) {
				mainNetwork.get().addNodeAndConnect(node, parent);
				node.setNetworkUUID(mainNetwork.get().getUUID());
				SpectrumS2CPacketSender.syncPastelNetworkEdges(mainNetwork.get(), node.getPos());
				return;
			}
		} else if (node.getServerNetwork().isPresent()) {
			mainNetwork = node.getServerNetwork();
			yieldingNetwork = parent.getServerNetwork();
			
			if (yieldingNetwork.isEmpty()) {
				mainNetwork.get().addNodeAndConnect(parent, node);
				parent.setNetworkUUID(mainNetwork.get().getUUID());
				SpectrumS2CPacketSender.syncPastelNetworkEdges(mainNetwork.get(), node.getPos());
				return;
			}
		}
		else {
			ServerPastelNetwork newNetwork = createNetwork(node.getWorld(), node.getNodeId());
			newNetwork.addNode(parent);
			parent.setNetworkUUID(newNetwork.getUUID());
			newNetwork.addNodeAndConnect(node, parent);
			node.setNetworkUUID(newNetwork.getUUID());
			SpectrumS2CPacketSender.syncPastelNetworkEdges(newNetwork, node.getPos());
			return;
		}
		
		if (mainNetwork == yieldingNetwork) {
			return;
		}
		
		mainNetwork.get().incorporate(yieldingNetwork.get(), node, parent);
		this.networks.remove(yieldingNetwork);
	}

	public void removeNode(PastelNodeBlockEntity node, NodeRemovalReason reason) {
		Optional<ServerPastelNetwork> optional = node.getServerNetwork();
		if (optional.isPresent()) {
			ServerPastelNetwork network = optional.get();
			network.removeNode(node, reason);
			
			if (reason == NodeRemovalReason.UNLOADED) {
				return;
			}
			
			if (network.hasNodes()) {
				// check if the removed node split the network into subnetworks
				network.checkForNetworkSplit();
			} else if (reason.destructive) {
				this.networks.remove(network);
			}
			SpectrumS2CPacketSender.syncPastelNetworkEdges(network, node.getPos());
		}
	}
	
}
