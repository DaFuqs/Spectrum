package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.server.world.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

// Persisted together with the overworld. Resetting the overworld will also reset all networks
public class ServerPastelNetworkManager extends PersistentState implements PastelNetworkManager<ServerWorld, ServerPastelNetwork> {
	
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
		// TODO: We need to spoof a datafixer type, null will prevent data from being read
		PersistentState.Type<ServerPastelNetworkManager> type = new PersistentState.Type<>(ServerPastelNetworkManager::new, (nbtCompound, lookup) -> ServerPastelNetworkManager.fromNbt(nbtCompound), null);
		return world.getPersistentStateManager().getOrCreate(type, PERSISTENT_STATE_ID);
	}
	
	public ServerPastelNetwork createNetwork(ServerWorld world, PastelNodeBlockEntity initialNode) {
		ServerPastelNetwork network = new ServerPastelNetwork(world, initialNode);
		this.networks.add(network);
		initialNode.setNetworkUUID(network.getUUID());
		return network;
	}
	
	@Override
	public ServerPastelNetwork createNetwork(ServerWorld world, UUID uuid, int color) {
		ServerPastelNetwork network = new ServerPastelNetwork(world, uuid, color);
		this.networks.add(network);
		return network;
	}
	
	@Override
	public Optional<ServerPastelNetwork> getNetwork(UUID uuid) {
		return networks.stream().filter(n -> n.uuid.equals(uuid)).findFirst();
	}
	
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		NbtList networkList = new NbtList();
		for (ServerPastelNetwork network : this.networks) {
			var opt = ServerPastelNetwork.CODEC.encodeStart(NbtOps.INSTANCE, network).result();
			if (opt.isPresent()) {
				var wrapper = new NbtCompound();
				wrapper.put("network", opt.get());
				wrapper.put("scheduler", transgender(network.getTransmissions()));
				// Trans missions?... do... do they really?
				networkList.add(wrapper);
			}
		}
		nbt.put("Networks", networkList);
		return nbt;
	}
	
	public static ServerPastelNetworkManager fromNbt(NbtCompound nbt) {
		ServerPastelNetworkManager manager = new ServerPastelNetworkManager();
		for (NbtElement element : nbt.getList("Networks", NbtElement.COMPOUND_TYPE)) {
			var comp = (NbtCompound) element;
			var netNbt = comp.get("network");
			var schedulerNbt = comp.getCompound("scheduler");
			
			Optional<ServerPastelNetwork> network = CodecHelper.fromNbt(ServerPastelNetwork.CODEC, netNbt);
			if (network.isPresent()) {
				// I truly, really did try to make the transmission codec work. And I failed ~ Azzyypaaras
				network.get().getTransmissions().putAll(transDecode(schedulerNbt, network.get()));
				manager.networks.add(network.get());
			}
		}
		return manager;
	}
	
	private static @NotNull HashMap<PastelTransmission, Integer> transDecode(NbtCompound schedulerNbt, ServerPastelNetwork network) {
		var transmissions = schedulerNbt.getList("transmissions", NbtElement.COMPOUND_TYPE);
		var timers = schedulerNbt.getIntArray("timers");
		var map = new HashMap<PastelTransmission, Integer>();
		
		for (int i = 0; i < transmissions.size(); i++) {
			var result = PastelTransmission.CODEC.decode(NbtOps.INSTANCE, transmissions.get(i)).result();
			
			if (result.isEmpty())
				continue;
			
			var trans = result.get().getFirst();
			trans.setNetwork(network);
			map.put(
					trans,
					timers[i]
			);
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
	
	private static NbtCompound transgender(Map<PastelTransmission, Integer> trans) {
		var transNbt = new NbtCompound();
		var transmissions = new NbtList();
		var timers = new int[trans.size()];
		for (Map.Entry<PastelTransmission, Integer> transmissionEntry : trans.entrySet()) {
			var result = PastelTransmission.CODEC.encodeStart(NbtOps.INSTANCE, transmissionEntry.getKey()).result();
			if (result.isPresent()) {
				transmissions.add(result.get());
				timers[transmissions.indexOf(result.get())] = transmissionEntry.getValue();
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
