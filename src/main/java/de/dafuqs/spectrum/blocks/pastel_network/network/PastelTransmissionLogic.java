package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.networking.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;
import org.jgrapht.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;

import java.util.*;

public class PastelTransmissionLogic {
    
    private enum TransferMode {
        PUSH,
        PULL,
        PUSH_PULL
    }
    
    public static final int MAX_TRANSFER_AMOUNT = 1;
    public static final int MAX_TRANSFER_TRIES = 99;
    public static final int TRANSFER_TICKS_PER_NODE = 30;
    private final ServerPastelNetwork network;
    
    private DijkstraShortestPath<PastelNodeBlockEntity, DefaultEdge> dijkstra;
    private Map<PastelNodeBlockEntity, Map<PastelNodeBlockEntity, GraphPath<PastelNodeBlockEntity, DefaultEdge>>> pathCache = new HashMap<>();
    
    
    public PastelTransmissionLogic(ServerPastelNetwork network) {
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
        Map<PastelNodeBlockEntity, GraphPath<PastelNodeBlockEntity, DefaultEdge>> e = this.pathCache.getOrDefault(source, null);
        if (e != null) {
            if (e.containsKey(destination)) {
                return e.get(destination);
            }
        }
    
        // calculate and cache
        ShortestPathAlgorithm.SingleSourcePaths<PastelNodeBlockEntity, DefaultEdge> paths = this.dijkstra.getPaths(source);
        GraphPath<PastelNodeBlockEntity, DefaultEdge> path = paths.getPath(destination);
        if (this.pathCache.containsKey(source)) {
            this.pathCache.get(source).put(destination, path);
        } else {
            Map<PastelNodeBlockEntity, GraphPath<PastelNodeBlockEntity, DefaultEdge>> newMap = new HashMap<>();
            newMap.put(destination, path);
            this.pathCache.put(source, newMap);
        }
    
        return path;
    }

    public void tick() {
		transferBetween(PastelNodeType.SENDER, PastelNodeType.GATHER, TransferMode.PUSH_PULL);
		transferBetween(PastelNodeType.PROVIDER, PastelNodeType.GATHER, TransferMode.PULL);
		transferBetween(PastelNodeType.STORAGE, PastelNodeType.GATHER, TransferMode.PULL);
		transferBetween(PastelNodeType.SENDER, PastelNodeType.STORAGE, TransferMode.PUSH);
	}

    private void transferBetween(PastelNodeType sourceType, PastelNodeType destinationType, TransferMode transferMode) {
        for (PastelNodeBlockEntity sourceNode : this.network.getNodes(sourceType)) {
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
            List<ItemVariant> excludedItems = new ArrayList<>();
            for (int attempt = 0; attempt <= MAX_TRANSFER_TRIES; ++attempt) {
                ResourceAmount<ItemVariant> extractableAmount = getExtractableAmount(sourceNode, sourceStorage, destinationNode, excludedItems, transaction);
                if (extractableAmount != null) {
                    excludedItems.add(extractableAmount.resource());
                    int transferrableAmount = (int) Math.min(extractableAmount.amount(), MAX_TRANSFER_AMOUNT);
                    transferrableAmount = (int) destinationStorage.simulateInsert(extractableAmount.resource(), transferrableAmount + destinationNode.getItemCountUnderway(), transaction);
                    transferrableAmount = transferrableAmount - destinationNode.getItemCountUnderway(); // prevention to not overfill the container (send more transfers when the existing ones would fill it already)
                    if (transferrableAmount > 0) {
                        sourceStorage.extract(extractableAmount.resource(), transferrableAmount, transaction);
                        Optional<PastelTransmission> optionalTransmission = createTransmissionOnValidPath(sourceNode, destinationNode, extractableAmount.resource(), transferrableAmount);
                        if (optionalTransmission.isPresent()) {
                            PastelTransmission transmission = optionalTransmission.get();
                            int verticesCount = transmission.getNodePositions().size() - 1;
                            int travelTime = TRANSFER_TICKS_PER_NODE * verticesCount;
                            this.network.addTransmission(transmission, travelTime);
                            SpectrumS2CPacketSender.sendPastelTransmissionParticle(this.network, travelTime, transmission);
                            if (transferMode == TransferMode.PULL) {
                                destinationNode.markTransferred();
                            } else if (transferMode == TransferMode.PUSH) {
                                sourceNode.markTransferred();
                            } else {
                                destinationNode.markTransferred();
                                sourceNode.markTransferred();
                            }

                            destinationNode.addItemCountUnderway(transferrableAmount);
                            transaction.commit();
                            return true;
                        }
                    }
                }
                else {
                    transaction.abort();
                    break;
                }
            }
        }
        return false;
    }

    private ResourceAmount<ItemVariant> getExtractableAmount(PastelNodeBlockEntity sourceNode, Storage<ItemVariant> sourceStorage, PastelNodeBlockEntity destinationNode, List<ItemVariant> excludedItems, Transaction transaction) {
        return StorageUtil.findExtractableContent(sourceStorage, variant -> {
            if (!sourceNode.getTransferFilterTo(destinationNode).test(variant))
                return false;
            return excludedItems.stream().noneMatch(itemVariant -> itemVariant.isOf(variant.getItem()) && itemVariant.nbtMatches(variant.getNbt()));
        }, transaction);
    }
    
    public Optional<PastelTransmission> createTransmissionOnValidPath(PastelNodeBlockEntity source, PastelNodeBlockEntity destination, ItemVariant variant, int amount) {
        GraphPath<PastelNodeBlockEntity, DefaultEdge> graphPath = getPath(this.network.getGraph(), source, destination);
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
