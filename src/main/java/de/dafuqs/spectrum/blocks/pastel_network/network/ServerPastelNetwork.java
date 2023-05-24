package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.registry.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.jgrapht.*;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.graph.*;

import java.util.*;

public class ServerPastelNetwork extends PastelNetwork {

    protected SchedulerMap<PastelTransmission> transmissions = new SchedulerMap<>();
    protected final ServerPastelTransmissionLogic serverPastelTransmissionLogic;

    public ServerPastelNetwork(World world, @Nullable UUID uuid) {
        super(world, uuid);
        this.serverPastelTransmissionLogic = new ServerPastelTransmissionLogic(this);
    }

    public void checkNetworkMergesForNewNode(PastelNodeBlockEntity newNode) {
        int biggestNetworkNodeCount = this.getNodeCount();
        PastelNetwork biggestNetwork = this;
        List<PastelNetwork> networksToMerge = new ArrayList<>();

        for (PastelNetwork currentNetwork : Pastel.getServerInstance().networks) {
            if (currentNetwork == this) {
                continue;
            }
            if (currentNetwork.canConnect(newNode)) {
                if (currentNetwork.getNodeCount() > biggestNetworkNodeCount) {
                    networksToMerge.add(biggestNetwork);
                    biggestNetwork = currentNetwork;
                } else {
                    networksToMerge.add(currentNetwork);
                }
                break;
            }
        }

        if (networksToMerge.size() == 0) {
            return;
        }

        for (PastelNetwork networkToMerge : networksToMerge) {
            ((ServerPastelNetwork) biggestNetwork).incorporate(networkToMerge);
        }
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
        serverPastelTransmissionLogic.invalidateCache();
        Pastel.getInstance(networkToIncorporate.world.isClient).remove(networkToIncorporate);
    }

    @Override
    public void addNode(PastelNodeBlockEntity node) {
        super.addNode(node);
        serverPastelTransmissionLogic.invalidateCache();
    }

    public boolean removeNode(PastelNodeBlockEntity node, NodeRemovalReason reason) {
        boolean hadNode = this.nodes.get(node.getNodeType()).remove(node);
        if (!hadNode) {
            return false;
        }

        if (hasNodes()) {
            if (this.graph != null) {
                // delete the now removed node from this networks graph
                this.graph.removeVertex(node);
                serverPastelTransmissionLogic.invalidateCache();
            }
            Graph<PastelNodeBlockEntity, DefaultEdge> graph = getGraph();

            // check if the removed node split the network into subnetworks
            ConnectivityInspector<PastelNodeBlockEntity, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(graph);
            List<Set<PastelNodeBlockEntity>> connectedSets = connectivityInspector.connectedSets();
            if (connectedSets.size() != 1) {
                for (int i = 1; i < connectedSets.size(); i++) {
                    Set<PastelNodeBlockEntity> disconnectedNodes = connectedSets.get(i);
                    PastelNetwork newNetwork = Pastel.getServerInstance().createNetwork(this.world, null);
                    for (PastelNodeBlockEntity disconnectedNode : disconnectedNodes) {
                        this.nodes.get(disconnectedNode.getNodeType()).remove(disconnectedNode);
                        getGraph().removeVertex(disconnectedNode);
                        newNetwork.addNode(disconnectedNode);
                        disconnectedNode.setNetwork(newNetwork);
                    }
                }
            }
            return true;
        } else if (reason == NodeRemovalReason.BROKEN || reason == NodeRemovalReason.MOVED) {
            Pastel.getServerInstance().remove(this);
        }
        return false;
    }

    public void tick() {
        this.transmissions.tick();
        this.serverPastelTransmissionLogic.tick();
    }

    public void addTransmission(PastelTransmission transmission, int travelTime) {
        transmission.setNetwork(this);
        this.transmissions.put(transmission, travelTime);
    }

    public NbtCompound toNbt() {
        NbtCompound compound = new NbtCompound();
        compound.putUuid("UUID", this.uuid);
        compound.putString("World", this.world.getRegistryKey().getValue().toString());

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

        for (NbtElement e : compound.getList("Transmissions", NbtElement.COMPOUND_TYPE)) {
            NbtCompound t = (NbtCompound) e;
            int delay = t.getInt("Delay");
            PastelTransmission transmission = PastelTransmission.fromNbt(t.getCompound("Transmission"));
            network.addTransmission(transmission, delay);
        }
        return network;
    }

}
