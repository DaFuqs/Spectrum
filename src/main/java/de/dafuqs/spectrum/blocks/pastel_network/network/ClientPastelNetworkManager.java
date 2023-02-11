package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.client.render.*;
import net.minecraft.util.math.*;
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

    private static void renderDebugLine(VertexConsumerProvider vertexConsumerProvider, int color, Vec3d offset, Vec3d normalized, Matrix4f positionMatrix) {
        vertexConsumerProvider.getBuffer(RenderLayer.getLines())
                .vertex(positionMatrix, 0.5F, 0.5F, 0.5F)
                .color(color)
                .normal((float) normalized.x, (float) normalized.y, (float) normalized.z)
                .next();
        vertexConsumerProvider.getBuffer(RenderLayer.getLines())
                .vertex(positionMatrix, (float) offset.x, (float) offset.y, (float) offset.z)
                .color(color)
                .normal((float) normalized.x, (float) normalized.y, (float) normalized.z)
                .next();
    }

}