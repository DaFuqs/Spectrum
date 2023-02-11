package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ClientPastelNetworkManager implements PastelNetworkManager {

    protected List<PastelNetwork> networks = new ArrayList<>();

    public void remove(PastelNetwork network) {
        this.networks.remove(network);
    }

    public @Nullable PastelNetwork getNetwork(UUID uuid) {
        for (PastelNetwork network : this.networks) {
            if (network.getUUID() == uuid) {
                return network;
            }
        }
        return null;
    }

    public List<PastelNetwork> getNetworks() {
        return networks;
    }

    public PastelNetwork joinNetwork(PastelNodeBlockEntity node, UUID uuid) {
        PastelNetwork foundNetwork = null;
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < this.networks.size(); i++) {
            PastelNetwork network = this.networks.get(i);
            if (network.getUUID().equals(uuid)) {
                network.addNode(node);
                foundNetwork = network;
            } else {
                if (network.removeNode(node, NodeRemovalReason.MOVED)) {
                    i--;
                }
                // network empty => delete
                if (!network.hasNodes()) {
                    remove(network);
                }
            }
        }
        if (foundNetwork != null) {
            return foundNetwork;
        }

        PastelNetwork network = createNetwork(node.getWorld(), uuid);
        network.addNode(node);
        return network;
    }

    @Override
    public PastelNetwork createNetwork(World world, UUID uuid) {
        PastelNetwork network = new PastelNetwork(world, uuid);
        this.networks.add(network);
        return network;
    }

}