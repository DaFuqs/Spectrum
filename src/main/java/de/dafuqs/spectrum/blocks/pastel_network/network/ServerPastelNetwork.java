package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.blocks.pastel_network.transfer.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;

import java.util.*;

public class ServerPastelNetwork extends PastelNetwork {

    protected final TransferLogic logic;
    private @Nullable Graph<PastelNodeBlockEntity, DefaultEdge> graph;

    public ServerPastelNetwork(World world) {
        super(world);
        this.logic = new TransferLogic(this);
    }

    public ServerPastelNetwork(World world, UUID uuid) {
        super(world, uuid);
        this.logic = new TransferLogic(this);
    }

    @Override
    public void addNode(PastelNodeBlockEntity node) {
        super.addNode(node);
        Graph<PastelNodeBlockEntity, DefaultEdge> graph = getGraph();
        graph.addVertex(node);
        for (PastelNodeBlockEntity existingNode : this.getAllNodes()) {
            if (node == existingNode) {
                continue;
            }
            if (node.canSee(node)) {
                graph.addEdge(node, existingNode);
            }
        }
    }

    @Override
    public void removeNode(PastelNodeBlockEntity node) {
        Graph<PastelNodeBlockEntity, DefaultEdge> graph = getGraph();
        graph.removeVertex(node);
        super.removeNode(node);
    }

    @Override
    public void tick() {
        super.tick();
        this.logic.tick();
    }

    public Graph<PastelNodeBlockEntity, DefaultEdge> getGraph() {
        if (this.graph == null) {
            this.graph = buildGraph(this);
        }
        return this.graph;
    }

    public static @NotNull SimpleGraph<PastelNodeBlockEntity, DefaultEdge> buildGraph(@NotNull PastelNetwork network) {
        SimpleGraph<PastelNodeBlockEntity, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);

        for (PastelNodeBlockEntity node : network.getAllNodes()) {
            g.addVertex(node);
        }

        for (PastelNodeBlockEntity node : network.getAllNodes()) {
            for (PastelNodeBlockEntity node2 : network.getAllNodes()) {
                if (node == node2) {
                    continue;
                }
                if (node.canSee(node2)) {
                    g.addEdge(node, node2);
                }
            }
        }

        return g;
    }

}
