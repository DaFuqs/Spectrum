package de.dafuqs.spectrum.blocks.pastel_network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.fabricmc.api.*;

import java.util.*;

public class PastelNetworkManager {

    @Environment(EnvType.CLIENT)
    private static PastelNetworkManager clientManager;
    private static ServerPastelNetworkManager serverManager;

    @Environment(EnvType.CLIENT)
    public static PastelNetworkManager getClientInstance() {
        if (clientManager == null) {
            clientManager = new PastelNetworkManager();
        }
        return clientManager;
    }


    public static ServerPastelNetworkManager getServerInstance() {
        if (serverManager == null) {
            serverManager = new ServerPastelNetworkManager();
        }
        return serverManager;
    }

    public static void clearClientInstance() {
        clientManager = null;
    }

    public static void clearServerInstance() {
        serverManager = null;
    }

    public static PastelNetworkManager getInstance(boolean client) {
        if (client) {
            return getClientInstance();
        } else {
            return getServerInstance();
        }
    }

    public static class ServerPastelNetworkManager extends PastelNetworkManager {

        private final TickLooper tickLooper = new TickLooper(40);

        public PastelNetwork joinNetwork(PastelNodeBlockEntity node) {
            PastelNetwork network = super.joinNetwork(node);
            node.updateInClientWorld();
            return network;
        }

        public PastelNetwork joinNetwork(PastelNodeBlockEntity node, UUID uuid) {
            PastelNetwork network = super.joinNetwork(node, uuid);
            node.updateInClientWorld();
            return network;
        }

        public void tickLogic() {
            for (PastelNetwork network : networks) {
                network.tickLogic();
            }
        }

    }


    protected List<PastelNetwork> networks = new ArrayList<>();

    public PastelNetwork joinNetwork(PastelNodeBlockEntity node) {
        for (PastelNetwork network : networks) {
            if (network.canConnect(node)) {
                network.addNode(node);
                return network;
            }
        }

        PastelNetwork network = new PastelNetwork(node.getWorld());
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

        PastelNetwork network = new PastelNetwork(node.getWorld(), uuid);
        this.networks.add(network);
        network.addNode(node);
        return network;
    }

    public void remove(PastelNetwork network) {
        this.networks.remove(network);
    }

}
