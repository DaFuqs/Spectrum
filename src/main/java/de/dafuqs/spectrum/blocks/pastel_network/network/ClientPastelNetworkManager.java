package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.world.*;

import java.util.*;

public class ClientPastelNetworkManager extends PastelNetworkManager {

    public PastelNetwork joinNetwork(PastelNodeBlockEntity node, UUID uuid) {
        PastelNetwork foundNetwork = null;
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < networks.size(); i++) {
            PastelNetwork network = networks.get(i);
            if (network.getUUID().equals(uuid)) {
                network.addNode(node);
                foundNetwork = network;
            } else {
                network.removeNode(node);
            }
        }
        if (foundNetwork != null) {
            return foundNetwork;
        }

        PastelNetwork network = createNetwork(node.getWorld(), uuid);
        this.networks.add(network);
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