package de.dafuqs.spectrum.blocks.pastel_network.transfer;

import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.world.*;

import java.util.*;

public class ServerPastelNetworkManager extends PastelNetworkManager {

    @Override
    public PastelNetwork joinNetwork(PastelNodeBlockEntity node) {
        PastelNetwork network = super.joinNetwork(node);
        node.updateInClientWorld();
        return network;
    }

    @Override
    public PastelNetwork joinNetwork(PastelNodeBlockEntity node, UUID uuid) {
        PastelNetwork network = super.joinNetwork(node, uuid);
        node.updateInClientWorld();
        return network;
    }

    @Override
    public PastelNetwork newNetwork(World world) {
        return new ServerPastelNetwork(world);
    }

    @Override
    public PastelNetwork newNetwork(World world, UUID uuid) {
        return new ServerPastelNetwork(world, uuid);
    }

    public void tick() {
        for (PastelNetwork network : networks) {
            network.tick();
        }
    }

}