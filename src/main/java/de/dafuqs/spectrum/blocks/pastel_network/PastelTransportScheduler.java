package de.dafuqs.spectrum.blocks.pastel_network;

import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.minecraft.item.*;
import org.jgrapht.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;

import java.util.*;

public class PastelTransportScheduler {

    private SchedulerMap<PastelTransfer<ItemStack>> transfers = new SchedulerMap<>();

    public static class PastelTransfer<T> {

        private final List<PastelNodeBlockEntity> nodes;
        private final T transport;

        public PastelTransfer(List<PastelNodeBlockEntity> nodes, T transport) {
            this.nodes = nodes;
            this.transport = transport;
        }

        public PastelNodeBlockEntity nextNode() {
            return nodes.remove(0);
        }

        public T getTransport() {
            return this.transport;
        }

    }

    public static Optional<PastelTransfer<ItemStack>> buildTransfer(PastelNetwork network, PastelNodeBlockEntity source, PastelNodeBlockEntity destination, ItemStack stack) {
        GraphPath<PastelNodeBlockEntity, DefaultEdge> graphPath = getPath(network, source, destination);
        if (graphPath != null) {
            return Optional.of(new PastelTransfer<>(graphPath.getVertexList(), stack));
        }
        return Optional.empty();
    }

    private static SimpleGraph<PastelNodeBlockEntity, DefaultEdge> graph;

    public static GraphPath<PastelNodeBlockEntity, DefaultEdge> getPath(PastelNetwork network, PastelNodeBlockEntity source, PastelNodeBlockEntity destination) {
        if (graph == null) {
            graph = buildGraph(network);
        }

        DijkstraShortestPath<PastelNodeBlockEntity, DefaultEdge> dijkstraAlg = new DijkstraShortestPath<>(graph);
        ShortestPathAlgorithm.SingleSourcePaths<PastelNodeBlockEntity, DefaultEdge> iPaths = dijkstraAlg.getPaths(source);
        return iPaths.getPath(destination);
    }

    public static SimpleGraph<PastelNodeBlockEntity, DefaultEdge> buildGraph(PastelNetwork network) {
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


    /*
    public static Optional<PastelTransfer<ItemStack>> buildTransfer(PastelNetwork network, PastelNodeBlockEntity source, PastelNodeBlockEntity destination, ItemStack stack)  {
        Optional<Queue<PastelNodeBlockEntity>> path = getPath(network, source, destination);
        if(path.isPresent()) {
            return Optional.of(new PastelTransfer<>(path.get(), stack));
        }
        return Optional.empty();
    }

    public static Optional<Queue<PastelNodeBlockEntity>> getPath(PastelNetwork network, PastelNodeBlockEntity source, PastelNodeBlockEntity destination) {
        Map<PastelNodeBlockEntity, BreadthFirstSearchAlgorithm.Node<PastelNodeBlockEntity>> graph = new HashMap<>();
        for(PastelNodeBlockEntity node : network.getAllNodes()) {
            graph.put(node, new BreadthFirstSearchAlgorithm.Node<>(node));
        }

        for(Map.Entry<PastelNodeBlockEntity, BreadthFirstSearchAlgorithm.Node<PastelNodeBlockEntity>> node : graph.entrySet()) {
            for(Map.Entry<PastelNodeBlockEntity, BreadthFirstSearchAlgorithm.Node<PastelNodeBlockEntity>> node2 : graph.entrySet()) {
                if(node.getKey() == node2.getKey()) {
                    continue;
                }
                if(node.getKey().canSee(node2.getKey())) {
                    node.getValue().connect(node2.getValue());
                }
            }
        }

        return BreadthFirstSearchAlgorithm.isConnected(destination, graph.get(source));
    }*/

}
