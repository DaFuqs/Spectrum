package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ServerPastelNetworkManager extends PastelNetworkManager {

    public PastelNetwork joinNetwork(PastelNodeBlockEntity node, @Nullable UUID uuid) {
        if (uuid == null) {
            for (PastelNetwork network : networks) {
                if (network.canConnect(node)) {
                    network.addNode(node);
                    ((ServerPastelNetwork) network).checkNetworkMergesForNewNode(node);
                    return network;
                }
            }
        } else {
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < networks.size(); i++) {
                PastelNetwork network = networks.get(i);
                if (network.getUUID().equals(uuid)) {
                    network.addNode(node);
                    return network;
                }
            }
        }

        PastelNetwork network = createNetwork(node.getWorld(), uuid);
        network.addNode(node);
        return network;
    }

    @Override
    public PastelNetwork createNetwork(World world, @Nullable UUID uuid) {
        PastelNetwork network = new ServerPastelNetwork(world, uuid);
        this.networks.add(network);
        return network;
    }

    public NbtCompound toNbt(NbtCompound tag) {
        NbtList networkList = new NbtList();
        for (PastelNetwork network : this.networks) {
            NbtCompound compound = network.toNbt();
            networkList.add(compound);
        }
        tag.put("Networks", networkList);
        return tag;
    }

}