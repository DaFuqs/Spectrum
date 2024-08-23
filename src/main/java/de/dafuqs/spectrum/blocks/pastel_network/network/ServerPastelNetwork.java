package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;
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

	@Override
	public void incorporate(PastelNetwork networkToIncorporate) {
        super.incorporate(networkToIncorporate);
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
		var priority = Priority.GENERIC;

		if (transferLooper.getTick() % 5 == 0) {
			priority = Priority.MODERATE;
		}
		else if (transferLooper.getTick() % 2 == 0) {
			priority = Priority.HIGH;
		}

		this.transferLooper.tick();
		var cap = transferLooper.reachedCap();
		if (cap || priority != Priority.GENERIC) {
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
				if (pastelNode.getRedstoneRing().map(r -> r == PastelNodeBlockEntity.DETECTOR).orElse(false))
					pastelNode.pulseRedstone();
			}
		}

		if (!nodeSync.isEmpty())
			SpectrumS2CPacketSender.sendPastelNodeStatusUpdate(nodeSync, false);
	}
	
	@Override
	public void addTransmission(PastelTransmission transmission, int travelTime) {
		transmission.setNetwork(this);
		this.transmissions.put(transmission, travelTime);
	}
	
	public NbtCompound toNbt() {
		NbtCompound compound = new NbtCompound();
		compound.putUuid("UUID", this.uuid);
		compound.putString("World", this.getWorld().getRegistryKey().getValue().toString());
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
		World world = SpectrumCommon.minecraftServer.getWorld(RegistryKey.of(RegistryKeys.WORLD, Identifier.tryParse(compound.getString("World"))));
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
