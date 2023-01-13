package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.networking.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.minecraft.util.math.*;
import org.jgrapht.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;

import java.util.*;

public class TransmissionLogic {

    private enum TransferMode {
        PUSH,
        PULL,
        PUSH_PULL
    }

    public static final int MAX_TRANSFER_AMOUNT = 1;
    public static final int TRANSFER_TICKS_PER_NODE = 40;

    private final TickLooper tickLooper = new TickLooper(TRANSFER_TICKS_PER_NODE);
    private final ServerPastelNetwork network;

    public TransmissionLogic(ServerPastelNetwork network) {
        this.network = network;
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
            ResourceAmount<ItemVariant> resourceAmount = StorageUtil.findExtractableContent(sourceStorage, transaction);
            if (resourceAmount != null) {
                int validAmount = (int) Math.min(resourceAmount.amount(), MAX_TRANSFER_AMOUNT);
                validAmount = (int) destinationStorage.simulateInsert(resourceAmount.resource(), validAmount, transaction);
                if (validAmount > 0) {
                    sourceStorage.extract(resourceAmount.resource(), validAmount, transaction);
                    Optional<PastelTransmission> transfer = buildTransfer(sourceNode, destinationNode, resourceAmount.resource(), validAmount);
                    if (transfer.isPresent()) {
                        PastelTransmission t = transfer.get();
                        int travelTime = TRANSFER_TICKS_PER_NODE * t.getNodePositions().size();
                        this.network.addTransmission(t, travelTime);
                        SpectrumS2CPacketSender.sendPastelTransfer(network, travelTime, t);
                        if (transferMode == TransferMode.PULL) {
                            destinationNode.markTransferred();
                        } else if (transferMode == TransferMode.PUSH) {
                            sourceNode.markTransferred();
                        } else {
                            destinationNode.markTransferred();
                            sourceNode.markTransferred();
                        }

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
        GraphPath<PastelNodeBlockEntity, DefaultEdge> graphPath = getPath(source, destination);
        if (graphPath != null) {
            List<BlockPos> vertexPositions = new ArrayList<>();
            for (PastelNodeBlockEntity vertex : graphPath.getVertexList()) {
                vertexPositions.add(vertex.getPos());
            }
            return Optional.of(new PastelTransmission(vertexPositions, variant, amount));
        }
        return Optional.empty();
    }

    public GraphPath<PastelNodeBlockEntity, DefaultEdge> getPath(PastelNodeBlockEntity source, PastelNodeBlockEntity destination) {
        DijkstraShortestPath<PastelNodeBlockEntity, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<>(this.network.getGraph());
        ShortestPathAlgorithm.SingleSourcePaths<PastelNodeBlockEntity, DefaultEdge> paths = dijkstraShortestPath.getPaths(source);
        return paths.getPath(destination);
    }

}
