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

    public void tick() {
        for (PastelNetwork network : networks) {
            network.tick();
        }
    }

    public @Nullable PastelNetwork getNetwork(UUID uuid) {
        for (PastelNetwork network : networks) {
            if (network.getUUID() == uuid) {
                return network;
            }
        }
        return null;
    }

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