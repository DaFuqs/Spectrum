package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.networking.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;
import org.jgrapht.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;

import java.util.*;

public class ServerPastelTransmissionLogic {

    private enum TransferMode {
        PUSH,
        PULL,
        PUSH_PULL
    }

    public static final int MAX_TRANSFER_AMOUNT = 1;
    public static final int START_TRANSFER_EVERY_X_TICKS = 10;
    public static final int TRANSFER_TICKS_PER_NODE = 30;

    private final TickLooper tickLooper = new TickLooper(START_TRANSFER_EVERY_X_TICKS);
    private final ServerPastelNetwork network;
    private DijkstraShortestPath<PastelNodeBlockEntity, DefaultEdge> dijkstra;
    private Map<PastelNodeBlockEntity, Map<PastelNodeBlockEntity, GraphPath<PastelNodeBlockEntity, DefaultEdge>>> pathCache = new HashMap<>();


    public ServerPastelTransmissionLogic(ServerPastelNetwork network) {
        this.network = network;
    }

    public void invalidateCache() {
        this.dijkstra = null;
        this.pathCache = new HashMap<>();
    }

    public @Nullable GraphPath<PastelNodeBlockEntity, DefaultEdge> getPath(Graph<PastelNodeBlockEntity, DefaultEdge> graph, PastelNodeBlockEntity source, PastelNodeBlockEntity destination) {
        if (this.dijkstra == null) {
            this.dijkstra = new DijkstraShortestPath<>(graph);
        }

        // cache hit?
        Map<PastelNodeBlockEntity, GraphPath<PastelNodeBlockEntity, DefaultEdge>> e = pathCache.getOrDefault(source, null);
        if (e != null) {
            if (e.containsKey(destination)) {
                return e.get(destination);
            }
        }

        // calculate and cache
        ShortestPathAlgorithm.SingleSourcePaths<PastelNodeBlockEntity, DefaultEdge> paths = dijkstra.getPaths(source);
        GraphPath<PastelNodeBlockEntity, DefaultEdge> path = paths.getPath(destination);
        if (pathCache.containsKey(source)) {
            pathCache.get(source).put(destination, path);
        } else {
            Map<PastelNodeBlockEntity, GraphPath<PastelNodeBlockEntity, DefaultEdge>> newMap = new HashMap<>();
            newMap.put(destination, path);
            pathCache.put(source, newMap);
        }

        return path;
    }

    public boolean tick() {
        tickLooper.tick();
        if (!tickLooper.reachedCap()) {
            return false;
        }
        tickLooper.reset();

        transferBetween(PastelNodeType.SENDER, PastelNodeType.GATHER, TransferMode.PUSH_PULL);
        transferBetween(PastelNodeType.PROVIDER, PastelNodeType.GATHER, TransferMode.PULL);
        transferBetween(PastelNodeType.STORAGE, PastelNodeType.GATHER, TransferMode.PULL);
        transferBetween(PastelNodeType.SENDER, PastelNodeType.STORAGE, TransferMode.PUSH);

        return false;
    }

    private void transferBetween(PastelNodeType sourceType, PastelNodeType destinationType, TransferMode transferMode) {
        for (PastelNodeBlockEntity sourceNode : network.getNodes(sourceType)) {
            if (!sourceNode.canTransfer()) {
                continue;
            }

            Storage<ItemVariant> sourceStorage = sourceNode.getConnectedStorage();
            if (sourceStorage != null && sourceStorage.supportsExtraction()) {
                tryTransferToType(sourceNode, sourceStorage, destinationType, transferMode);
            }
        }
    }

    private void tryTransferToType(PastelNodeBlockEntity sourceNode, Storage<ItemVariant> sourceStorage, PastelNodeType type, TransferMode transferMode) {
        for (PastelNodeBlockEntity destinationNode : this.network.getNodes(type)) {
            if (!destinationNode.canTransfer()) {
                continue;
            }

            Storage<ItemVariant> destinationStorage = destinationNode.getConnectedStorage();
            if (destinationStorage != null && destinationStorage.supportsInsertion()) {
                boolean success = transferBetween(sourceNode, sourceStorage, destinationNode, destinationStorage, transferMode);
                if (success && transferMode != TransferMode.PULL) {
                    return;
                }
            }
        }
    }

    private boolean transferBetween(PastelNodeBlockEntity sourceNode, Storage<ItemVariant> sourceStorage, PastelNodeBlockEntity destinationNode, Storage<ItemVariant> destinationStorage, TransferMode transferMode) {
        try (Transaction transaction = Transaction.openOuter()) {
            ResourceAmount<ItemVariant> resourceAmount = StorageUtil.findExtractableContent(sourceStorage, sourceNode.getTransferFilterTo(destinationNode), transaction);
            if (resourceAmount != null) {
                int validAmount = (int) Math.min(resourceAmount.amount(), MAX_TRANSFER_AMOUNT);
                validAmount = (int) destinationStorage.simulateInsert(resourceAmount.resource(), validAmount + destinationNode.getItemCountUnderway(), transaction);
                validAmount = validAmount - destinationNode.getItemCountUnderway(); // prevention to not overfill the container (send more transfers when the existing ones would fill it already)
                if (validAmount > 0) {
                    sourceStorage.extract(resourceAmount.resource(), validAmount, transaction);
                    Optional<PastelTransmission> optionalTransmission = buildTransfer(sourceNode, destinationNode, resourceAmount.resource(), validAmount);
                    if (optionalTransmission.isPresent()) {
                        PastelTransmission transmission = optionalTransmission.get();
                        int verticesCount = transmission.getNodePositions().size() - 1;
                        int travelTime = TRANSFER_TICKS_PER_NODE * verticesCount;
                        this.network.addTransmission(transmission, travelTime);
                        SpectrumS2CPacketSender.sendPastelTransmission(network, travelTime, transmission);
                        if (transferMode == TransferMode.PULL) {
                            destinationNode.markTransferred();
                        } else if (transferMode == TransferMode.PUSH) {
                            sourceNode.markTransferred();
                        } else {
                            destinationNode.markTransferred();
                            sourceNode.markTransferred();
                        }

                        destinationNode.addItemCountUnderway(validAmount);
                        transaction.commit();
                        return true;
                    }
                }
            }
            transaction.abort();
        }
        return false;
    }

    public Optional<PastelTransmission> buildTransfer(PastelNodeBlockEntity source, PastelNodeBlockEntity destination, ItemVariant variant, int amount) {
        GraphPath<PastelNodeBlockEntity, DefaultEdge> graphPath = getPath(network.getGraph(), source, destination);
        if (graphPath != null) {
            List<BlockPos> vertexPositions = new ArrayList<>();
            for (PastelNodeBlockEntity vertex : graphPath.getVertexList()) {
                vertexPositions.add(vertex.getPos());
            }
            return Optional.of(new PastelTransmission(vertexPositions, variant, amount));
        }
        return Optional.empty();
    }

}
