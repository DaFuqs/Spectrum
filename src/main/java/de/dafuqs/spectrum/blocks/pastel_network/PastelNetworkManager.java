package de.dafuqs.spectrum.blocks.pastel_network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;

import java.util.*;

public class PastelNetworkManager {

    protected static List<PastelNetwork> networks = new ArrayList<>();

    public static PastelNetwork getNetworkForNewNode(PastelNodeBlockEntity node) {
        for (PastelNetwork network : networks) {
            if (network.canConnect(node)) {
                return network;
            }
        }
        return new PastelNetwork(node.getWorld());
    }

    public static void remove(PastelNetwork network) {
        networks.remove(network);
    }

    public static void add(PastelNetwork network) {
        networks.add(network);
    }

    public static void tick() {
        for (PastelNetwork network : networks) {
            network.tick();
        }
    }

}
