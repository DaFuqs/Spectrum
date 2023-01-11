package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.networking.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.storage.base.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.jgrapht.*;
import org.jgrapht.alg.connectivity.*;
import org.jgrapht.alg.interfaces.*;
import org.jgrapht.alg.shortestpath.*;
import org.jgrapht.graph.*;

import java.util.*;

public class ServerPastelNetwork extends PastelNetwork {

    protected final TransmissionLogic transmissionLogic;

    public ServerPastelNetwork(World world, @Nullable UUID uuid) {
        super(world, uuid);
        this.transmissionLogic = new TransmissionLogic(this);
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
        this.transmissionLogic.tick();
    }

    public static class TransmissionLogic {

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

            transferBetween(PastelNodeType.SENDER, PastelNodeType.GATHER);
            transferBetween(PastelNodeType.PROVIDER, PastelNodeType.GATHER);
            transferBetween(PastelNodeType.STORAGE, PastelNodeType.GATHER);
            transferBetween(PastelNodeType.SENDER, PastelNodeType.STORAGE);

            return false;
        }

        private void transferBetween(PastelNodeType sourceType, PastelNodeType destinationType) {
            for (PastelNodeBlockEntity sourceNode : network.getNodes(sourceType)) {
                if (!sourceNode.canTransfer()) {
                    continue;
                }

                Storage<ItemVariant> sourceStorage = sourceNode.getConnectedStorage();
                if (sourceStorage != null && sourceStorage.supportsExtraction()) {
                    tryTransferToType(sourceNode, sourceStorage, destinationType);
                }
            }
        }

        private void tryTransferToType(PastelNodeBlockEntity sourceNode, Storage<ItemVariant> sourceStorage, PastelNodeType type) {
            for (PastelNodeBlockEntity destinationNode : this.network.getNodes(type)) {
                if (!destinationNode.canTransfer()) {
                    continue;
                }

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
                    Optional<PastelTransmission> transfer = buildTransfer(sourceNode, destinationNode, resourceAmount.resource(), validAmount);
                    if (transfer.isPresent()) {
                        PastelTransmission t = transfer.get();
                        int travelTime = TRANSFER_TICKS_PER_NODE * t.getNodePositions().size();
                        this.network.addTransmission(t, travelTime);
                        SpectrumS2CPacketSender.sendPastelTransfer(network, travelTime, t);
                        transaction.commit();
                        sourceNode.markTransferred();
                        destinationNode.markTransferred();
                    }
                    return;
                }
                transaction.abort();
            }
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

    public NbtCompound toNbt() {
        NbtCompound compound = new NbtCompound();
        compound.putUuid("UUID", this.uuid);
        compound.putString("World", this.world.getRegistryKey().getValue().toString());

        NbtList transmissionList = new NbtList();
        for (Map.Entry<PastelTransmission, Integer> transmission : this.transmissions) {
            NbtCompound transmissionCompound = new NbtCompound();
            transmissionCompound.putInt("Delay", transmission.getValue());
            transmissionCompound.put("Transmission", transmission.getKey().toNbt());
            transmissionList.add(transmissionCompound);
        }
        compound.put("Transmissions", transmissionList);

        return compound;
    }

    public static ServerPastelNetwork fromNbt(NbtCompound compound) {
        World world = SpectrumCommon.minecraftServer.getWorld(RegistryKey.of(Registry.WORLD_KEY, Identifier.tryParse(compound.getString("World"))));
        UUID uuid = compound.getUuid("UUID");
        ServerPastelNetwork network = new ServerPastelNetwork(world, uuid);

        for (NbtElement e : compound.getList("Transmissions", NbtElement.COMPOUND_TYPE)) {
            NbtCompound t = (NbtCompound) e;
            int delay = t.getInt("Delay");
            PastelTransmission transmission = PastelTransmission.fromNbt(t.getCompound("Transmission"));
            network.addTransmission(transmission, delay);
        }
        return network;
    }

}
