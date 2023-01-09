package de.dafuqs.spectrum.blocks.pastel_network.transfer;

import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.world.*;

import java.util.*;

public class PastelNetworkManager {

    protected List<PastelNetwork> networks = new ArrayList<>();

    public PastelNetwork newNetwork(World world) {
        return new PastelNetwork(world);
    }

    public PastelNetwork newNetwork(World world, UUID uuid) {
        return new PastelNetwork(world, uuid);
    }

    public PastelNetwork joinNetwork(PastelNodeBlockEntity node) {
        for (PastelNetwork network : networks) {
            if (network.canConnect(node)) {
                network.addNode(node);
                return network;
            }
        }

        PastelNetwork network = newNetwork(node.getWorld());
        this.networks.add(network);
        network.addNode(node);
        return network;
    }

    public PastelNetwork joinNetwork(PastelNodeBlockEntity node, UUID uuid) {
        for (PastelNetwork network : networks) {
            if (network.getUUID().equals(uuid)) {
                network.addNode(node);
                return network;
            }
        }

        PastelNetwork network = newNetwork(node.getWorld(), uuid);
        this.networks.add(network);
        network.addNode(node);
        return network;
    }

    public void remove(PastelNetwork network) {
        this.networks.remove(network);
    }

}
