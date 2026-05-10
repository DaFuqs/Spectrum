package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.saveddata.*;

import java.util.*;

// Persisted together with the overworld. Resetting the overworld will also reset all networks
public class ServerPastelNetworkManager extends SavedData implements PastelNetworkManager<ServerLevel, ServerPastelNetwork> {
	
	private static final String PERSISTENT_STATE_ID = "spectrum_pastel_network_manager";
	
	private final List<ServerPastelNetwork> networks = new ArrayList<>();
	
	public ServerPastelNetworkManager() {
		super();
	}
	
	@Override
	public boolean isDirty() {
		return true;
	}
	
	public static ServerPastelNetworkManager get(ServerLevel world) {
		SavedData.Factory<ServerPastelNetworkManager> type = new SavedData.Factory<>(ServerPastelNetworkManager::new, (nbtCompound, lookup) -> ServerPastelNetworkManager.fromNbt(nbtCompound), null);
		return world.getDataStorage().computeIfAbsent(type, PERSISTENT_STATE_ID);
	}
	
	public ServerPastelNetwork createNetwork(ServerLevel world, PastelNodeBlockEntity initialNode) {
		ServerPastelNetwork network = new ServerPastelNetwork(world, initialNode);
		this.networks.add(network);
		initialNode.setNetworkUUID(network.getUUID());
		return network;
	}
	
	@Override
	public ServerPastelNetwork createNetwork(ServerLevel world, UUID uuid, int color) {
		ServerPastelNetwork network = new ServerPastelNetwork(world, uuid, color);
		this.networks.add(network);
		return network;
	}
	
	@Override
	public Optional<ServerPastelNetwork> getNetwork(UUID uuid) {
		return networks.stream().filter(n -> n.uuid.equals(uuid)).findFirst();
	}
	
	
	@Override
	public CompoundTag save(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		ListTag networkList = new ListTag();
		for (ServerPastelNetwork network : this.networks) {
			var opt = ServerPastelNetwork.CODEC.encodeStart(NbtOps.INSTANCE, network).result();
			if (opt.isPresent()) {
				var wrapper = new CompoundTag();
				wrapper.put("network", opt.get());
				wrapper.put("scheduler", transgender(network.getTransmissions()));
				wrapper.put("graph", network.graphToNbt());
				networkList.add(wrapper);
			}
		}
		nbt.put("Networks", networkList);
		return nbt;
	}
	
	public static ServerPastelNetworkManager fromNbt(CompoundTag nbt) {
		ServerPastelNetworkManager manager = new ServerPastelNetworkManager();
		for (Tag element : nbt.getList("Networks", Tag.TAG_COMPOUND)) {
			var comp = (CompoundTag) element;
			var netNbt = comp.get("network");
			var schedulerNbt = comp.getCompound("scheduler");
			var graphNbt = comp.getCompound("graph");
			
			Optional<ServerPastelNetwork> network = CodecHelper.fromNbt(ServerPastelNetwork.CODEC, netNbt);
			if (network.isPresent()) {
				network.get().getTransmissions().putAll(transDecode(schedulerNbt, network.get()));
				network.get().setGraph(ServerPastelNetwork.graphFromNbt(graphNbt));
				manager.networks.add(network.get());
			}
		}
		return manager;
	}
	
	private static HashMap<PastelTransmission, Integer> transDecode(CompoundTag schedulerNbt, ServerPastelNetwork network) {
		var transmissions = schedulerNbt.getList("transmissions", Tag.TAG_COMPOUND);
		var timers = schedulerNbt.getIntArray("timers");
		var map = new HashMap<PastelTransmission, Integer>();
		
		for (int i = 0; i < transmissions.size(); i++) {
			var result = PastelTransmission.CODEC.decode(NbtOps.INSTANCE, transmissions.get(i)).result();
			
			if (result.isEmpty())
				continue;
			
			PastelTransmission transmission = result.get().getFirst();
			transmission.setNetwork(network);
			map.put(transmission, timers[i]);
		}
		return map;
	}
	
	public void tick() {
		// using a for here instead of foreach
		// to prevent ConcurrentModificationExceptions
		//noinspection ForLoopReplaceableByForEach
		for (int i = 0; i < this.networks.size(); i++) {
			this.networks.get(i).tick();
		}
	}
	
	private static CompoundTag transgender(Map<PastelTransmission, Integer> trans) {
		var transNbt = new CompoundTag();
		var transmissions = new ListTag();
		var timers = new int[trans.size()];
		for (Map.Entry<PastelTransmission, Integer> transmissionEntry : trans.entrySet()) {
			var result = PastelTransmission.CODEC.encodeStart(NbtOps.INSTANCE, transmissionEntry.getKey()).result();
			if (result.isPresent()) {
				transmissions.add(result.get());
				timers[transmissions.size() - 1] = transmissionEntry.getValue();
			}
		}
		
		transNbt.put("transmissions", transmissions);
		transNbt.putIntArray("timers", timers);
		return transNbt;
	}
	
	@Override
	public void removeNetwork(UUID uuid) {
		ServerPastelNetwork foundNetwork = null;
		for (ServerPastelNetwork network : this.networks) {
			if (network.uuid.equals(uuid)) {
				foundNetwork = network;
				break;
			}
		}
		if (foundNetwork != null) {
			this.networks.remove(foundNetwork);
			PastelNetworkRemovedPayload.send(foundNetwork);
		}
	}
	
	public void removeNode(PastelNodeBlockEntity node, NodeRemovalReason reason) {
		Optional<ServerPastelNetwork> optional = node.getServerNetwork();
		if (optional.isPresent()) {
			ServerPastelNetwork network = optional.get();
			
			if (network.size() == 1) {
				this.removeNetwork(network.getUUID());
			} else {
				network.removeNode(node, reason);
			}
		}
	}
	
}
