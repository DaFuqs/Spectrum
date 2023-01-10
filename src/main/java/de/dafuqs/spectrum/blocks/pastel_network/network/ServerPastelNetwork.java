package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.jgrapht.*;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;

import java.util.*;

public class ServerPastelNetwork extends PastelNetwork {

    protected final TransferLogic transferLogic;

    public ServerPastelNetwork(World world, @Nullable UUID uuid) {
        super(world, uuid);
        this.transferLogic = new TransferLogic(this);
    }

    public void checkNetworkMergesForNewNode(PastelNodeBlockEntity newNode) {
        int biggestNetworkNodeCount = this.getNodeCount();
        PastelNetwork biggestNetwork = this;
        List<PastelNetwork> networksToMerge = new ArrayList<>();

        for (PastelNetwork currentNetwork : Pastel.getServerInstance().networks) {
            if (currentNetwork == this) {
                continue;
            }
            if (currentNetwork.canConnect(newNode)) {
                if (currentNetwork.getNodeCount() > biggestNetworkNodeCount) {
                    networksToMerge.add(biggestNetwork);
                    biggestNetwork = currentNetwork;
                } else {
                    networksToMerge.add(currentNetwork);
                }
                break;
            }
        }

        if (networksToMerge.size() == 0) {
            return;
        }

        for (PastelNetwork networkToMerge : networksToMerge) {
            ((ServerPastelNetwork) biggestNetwork).incorporate(networkToMerge);
        }
    }

    public void incorporate(PastelNetwork networkToIncorporate) {
        for (Map.Entry<PastelNodeType, Set<PastelNodeBlockEntity>> nodesToIncorporate : networkToIncorporate.getNodes().entrySet()) {
            PastelNodeType type = nodesToIncorporate.getKey();
            for (PastelNodeBlockEntity nodeToIncorporate : nodesToIncorporate.getValue()) {
                this.nodes.get(type).add(nodeToIncorporate);
                nodeToIncorporate.setNetwork(this);
            }
        }
        this.graph = null;
        Pastel.getInstance(networkToIncorporate.world.isClient).remove(networkToIncorporate);
    }

    public boolean removeNode(PastelNodeBlockEntity node) {
        if (super.removeNode(node) && hasNodes()) {
            Graph<PastelNodeBlockEntity, DefaultEdge> graph = getGraph();

            // check if the removed node split the network into subnetworks
            ConnectivityInspector<PastelNodeBlockEntity, DefaultEdge> connectivityInspector = new ConnectivityInspector<>(graph);
            List<Set<PastelNodeBlockEntity>> connectedSets = connectivityInspector.connectedSets();
            if (connectedSets.size() != 1) {
                for (int i = 1; i < connectedSets.size(); i++) {
                    Set<PastelNodeBlockEntity> disconnectedNodes = connectedSets.get(i);
                    PastelNetwork newNetwork = Pastel.getServerInstance().createNetwork(this.world, null);
                    for (PastelNodeBlockEntity disconnectedNode : disconnectedNodes) {
                        this.nodes.get(disconnectedNode.getNodeType()).remove(disconnectedNode);
                        getGraph().removeVertex(disconnectedNode);
                        newNetwork.addNode(disconnectedNode);
                        disconnectedNode.setNetwork(newNetwork);
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.transferLogic.tick();
    }

    public static class TransferLogic {

        public static final int MAX_TRANSFER_AMOUNT = 1;
        public static final int TRANSFER_TICKS_PER_NODE = 40;

        private final TickLooper tickLooper = new TickLooper(TRANSFER_TICKS_PER_NODE);
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

            transferBetween(PastelNodeType.PUSHER, PastelNodeType.PULLER);
            transferBetween(PastelNodeType.PROVIDER, PastelNodeType.PULLER);
            transferBetween(PastelNodeType.STORAGE, PastelNodeType.PULLER);
            transferBetween(PastelNodeType.PUSHER, PastelNodeType.STORAGE);

            return false;
        }

        private void transferBetween(PastelNodeType sourceType, PastelNodeType destinationType) {
            for (PastelNodeBlockEntity sourceNode : network.getNodes(sourceType)) {
                Storage<ItemVariant> sourceStorage = sourceNode.getConnectedStorage();
                if (sourceStorage != null && sourceStorage.supportsExtraction()) {
                    tryTransferToType(sourceNode, sourceStorage, destinationType);
                }
            }
        }

        private void tryTransferToType(PastelNodeBlockEntity sourceNode, Storage<ItemVariant> sourceStorage, PastelNodeType type) {
            for (PastelNodeBlockEntity destinationNode : this.network.getNodes(type)) {
                Storage<ItemVariant> destinationStorage = destinationNode.getConnectedStorage();
                if (destinationStorage != null && destinationStorage.supportsInsertion()) {
                    transferBetween(sourceNode, sourceStorage, destinationNode, destinationStorage);
                    return;
                }
            }
        }

        private void transferBetween(PastelNodeBlockEntity sourceNode, Storage<ItemVariant> sourceStorage, PastelNodeBlockEntity destinationNode, Storage<ItemVariant> destinationStorage) {
            try (Transaction transaction = Transaction.openOuter()) {
                ResourceAmount<ItemVariant> resourceAmount = StorageUtil.findExtractableContent(sourceStorage, transaction);
                if (resourceAmount == null) {
                    return;
                }
                int validAmount = (int) Math.min(resourceAmount.amount(), MAX_TRANSFER_AMOUNT);
                validAmount = (int) destinationStorage.simulateInsert(resourceAmount.resource(), validAmount, transaction);
                if (validAmount > 0) {
                    sourceStorage.extract(resourceAmount.resource(), validAmount, transaction);
                    Optional<PastelTransfer> transfer = buildTransfer(sourceNode, destinationNode, resourceAmount.resource(), validAmount);
                    if (transfer.isPresent()) {
                        PastelTransfer t = transfer.get();
                        this.network.addTransfer(t, TRANSFER_TICKS_PER_NODE * t.getNodes().size());
                        transaction.commit();
                    }
                    return;
                }
                transaction.abort();
            }
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
}
