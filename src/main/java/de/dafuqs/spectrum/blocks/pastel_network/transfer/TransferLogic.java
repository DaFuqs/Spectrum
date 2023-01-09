package de.dafuqs.spectrum.blocks.pastel_network.transfer;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.minecraft.inventory.*;
import org.jgrapht.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;

import java.util.*;

public class TransferLogic {

    public static final int MAX_TRANSFER_AMOUNT = 1;
    public static final int TRANSFER_TICKS = 40;

    private final TickLooper tickLooper = new TickLooper(TRANSFER_TICKS);
    private final ServerPastelNetwork network;

    public TransferLogic(ServerPastelNetwork network) {
        this.network = network;
    }

    public boolean tick() {
        tickLooper.tick();
        if (!tickLooper.reachedCap()) {
            return false;
        }
        tickLooper.reset();

        initiateTransfers(PastelNodeType.PUSHER, PastelNodeType.PULLER);
        initiateTransfers(PastelNodeType.PROVIDER, PastelNodeType.PULLER);
        initiateTransfers(PastelNodeType.STORAGE, PastelNodeType.PULLER);
        initiateTransfers(PastelNodeType.PUSHER, PastelNodeType.STORAGE);

        return false;
    }

    private void initiateTransfers(PastelNodeType sourceType, PastelNodeType destinationType) {
        for (PastelNodeBlockEntity sourceNode : network.getNodes(sourceType)) {
            Inventory sourceInventory = sourceNode.getConnectedInventory();
            if (sourceInventory != null) {
                InventoryStorage sourceStorage = InventoryStorage.of(sourceInventory, null);
                if (sourceStorage.supportsExtraction()) {
                    if (tryTransferToType(sourceNode, sourceStorage, destinationType)) {
                        return;
                    }
                }
            }
        }
    }

    private boolean tryTransferToType(PastelNodeBlockEntity sourceNode, InventoryStorage sourceStorage, PastelNodeType type) {
        for (PastelNodeBlockEntity destination : this.network.getNodes(type)) {
            Inventory destinationInventory = destination.getConnectedInventory();
            if (destinationInventory != null) {
                InventoryStorage destinationStorage = InventoryStorage.of(destinationInventory, null);
                if (destinationStorage.supportsInsertion() && transferBetween(sourceNode, sourceStorage, destination, destinationStorage)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean transferBetween(PastelNodeBlockEntity sourceNode, InventoryStorage sourceStorage, PastelNodeBlockEntity destinationNode, InventoryStorage destinationStorage) {
        ItemVariant variant = ItemVariant.blank();
        for (SingleSlotStorage sourceSlot : sourceStorage.getSlots()) {
            if (sourceSlot.isResourceBlank()) {
                continue;
            }
            Object resource = sourceSlot.getResource();
            if (resource instanceof ItemVariant itemVariant) {
                variant = itemVariant;
                break;
            }
        }
        if (variant.isBlank()) {
            return false;
        }

        try (Transaction transaction = Transaction.openOuter()) {
            long amount = sourceStorage.extract(variant, MAX_TRANSFER_AMOUNT, transaction);
            int validAmount = (int) destinationStorage.simulateInsert(variant, amount, transaction);
            if (validAmount > 0) {
                transaction.commit();
                Optional<PastelTransfer> transfer = buildTransfer(sourceNode, destinationNode, variant, validAmount);
                if (transfer.isPresent()) {
                    PastelTransfer t = transfer.get();
                    this.network.addTransfer(t, TRANSFER_TICKS * t.getNodes().size());
                }
                return true;
            }
        }
        return false;
    }

    public Optional<PastelTransfer> buildTransfer(PastelNodeBlockEntity source, PastelNodeBlockEntity destination, ItemVariant variant, int amount) {
        GraphPath<PastelNodeBlockEntity, DefaultEdge> graphPath = getPath(source, destination);
        if (graphPath != null) {
            return Optional.of(new PastelTransfer(graphPath.getVertexList(), variant, amount));
        }
        return Optional.empty();
    }

    public GraphPath<PastelNodeBlockEntity, DefaultEdge> getPath(PastelNodeBlockEntity source, PastelNodeBlockEntity destination) {
        DijkstraShortestPath<PastelNodeBlockEntity, DefaultEdge> dijkstraAlg = new DijkstraShortestPath<>(this.network.getGraph());
        ShortestPathAlgorithm.SingleSourcePaths<PastelNodeBlockEntity, DefaultEdge> iPaths = dijkstraAlg.getPaths(source);
        return iPaths.getPath(destination);
    }

}
