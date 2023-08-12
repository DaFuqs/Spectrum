package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ServerPastelNetwork extends PastelNetwork {
	
	// new transfers are checked for every 10 ticks
	private final TickLooper transferLooper = new TickLooper(10);
	
	protected final SchedulerMap<PastelTransmission> transmissions = new SchedulerMap<>();
	protected final PastelTransmissionLogic transmissionLogic;
	
	public ServerPastelNetwork(World world, @Nullable UUID uuid) {
		super(world, uuid);
		this.transmissionLogic = new PastelTransmissionLogic(this);
	}
	
	public void incorporate(PastelNetwork networkToIncorporate) {
        for (Map.Entry<PastelNodeType, Set<PastelNodeBlockEntity>> nodesToIncorporate : networkToIncorporate.getNodes().entrySet()) {
            PastelNodeType type = nodesToIncorporate.getKey();
            for (PastelNodeBlockEntity nodeToIncorporate : nodesToIncorporate.getValue()) {
                this.nodes.get(type).add(nodeToIncorporate);
                nodeToIncorporate.setNetwork(this);
            }
        }
		this.graph = null;
		this.transmissionLogic.invalidateCache();
	}
	
	@Override
	public void addNode(PastelNodeBlockEntity node) {
		super.addNode(node);
		this.transmissionLogic.invalidateCache();
	}
	
	@Override
	public boolean removeNode(PastelNodeBlockEntity node, NodeRemovalReason reason) {
		boolean result = super.removeNode(node, reason);
		this.transmissionLogic.invalidateCache();
		return result;
	}
	
	@Override
	public void tick() {
		this.transmissions.tick();
		
		this.transferLooper.tick();
		if (this.transferLooper.reachedCap()) {
			this.transferLooper.reset();
			this.transmissionLogic.tick();
		}
	}
	
	@Override
	public void addTransmission(PastelTransmission transmission, int travelTime) {
		transmission.setNetwork(this);
		this.transmissions.put(transmission, travelTime);
	}
	
	public NbtCompound toNbt() {
		NbtCompound compound = new NbtCompound();
		compound.putUuid("UUID", this.uuid);
		compound.putString("World", this.world.getRegistryKey().getValue().toString());
		compound.put("Looper", this.transferLooper.toNbt());
		
		NbtList transmissionList = new NbtList();
        for (Map.Entry<PastelTransmission, Integer> transmission : this.transmissions) {
            NbtCompound transmissionCompound = new NbtCompound();
            transmissionCompound.putInt("Delay", transmission.getValue());
            transmissionCompound.put("Transmission", transmission.getKey().toNbt());
            transmissionList.add(transmissionCompound);
        }
        compound.put("Transmissions", transmissionList);

        return compound;
    }

    public static ServerPastelNetwork fromNbt(NbtCompound compound) {
		World world = SpectrumCommon.minecraftServer.getWorld(RegistryKey.of(Registry.WORLD_KEY, Identifier.tryParse(compound.getString("World"))));
		UUID uuid = compound.getUuid("UUID");
	
		ServerPastelNetwork network = new ServerPastelNetwork(world, uuid);
		if (compound.contains("Looper", NbtElement.COMPOUND_TYPE)) {
			network.transferLooper.readNbt(compound.getCompound("Looper"));
		}
	
		for (NbtElement e : compound.getList("Transmissions", NbtElement.COMPOUND_TYPE)) {
			NbtCompound t = (NbtCompound) e;
			int delay = t.getInt("Delay");
			PastelTransmission transmission = PastelTransmission.fromNbt(t.getCompound("Transmission"));
			network.addTransmission(transmission, delay);
		}
		return network;
	}

}
