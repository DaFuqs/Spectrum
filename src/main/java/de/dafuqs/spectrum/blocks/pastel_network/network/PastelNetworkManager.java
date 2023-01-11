package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class PastelNetworkManager {

    protected List<PastelNetwork> networks = new ArrayList<>();

    public abstract PastelNetwork createNetwork(World world, UUID uuid);

    public abstract PastelNetwork joinNetwork(PastelNodeBlockEntity node, @Nullable UUID uuid);

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

}
