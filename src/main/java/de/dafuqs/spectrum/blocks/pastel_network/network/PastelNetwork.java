package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

import java.util.*;

public class PastelNetwork {

    protected static final List<String> networkNames = List.of(
            "Alpha",
            "Beta",
            "Gamma",
            "Delta",
            "Epsilon",
            "Zeta",
            "Eta",
            "Theta",
            "Iota",
            "Kappa",
            "Lambda",
            "My",
            "Ny",
            "Xi",
            "Omikron",
            "Pi",
            "Rho",
            "Sigma",
            "Tau",
            "Ypsilon",
            "Phi",
            "Chi",
            "Psi",
            "Omeg"
    );

    protected Map<PastelNodeType, List<PastelNodeBlockEntity>> nodes = new HashMap<>();
    protected World world;
    protected String name;
    protected UUID uuid;

    public PastelNetwork(World world) {
        this(world, UUID.randomUUID());
    }

    public PastelNetwork(World world, UUID uuid) {
        this.world = world;
        this.uuid = uuid;
        this.name = networkNames.get(world.random.nextInt(networkNames.size()));
        for (PastelNodeType type : PastelNodeType.values()) {
            this.nodes.put(type, new ArrayList<>());
        }
    }

    public void addNode(PastelNodeBlockEntity node) {
        this.nodes.get(node.getNodeType()).add(node);
    }

    public void removeNode(PastelNodeBlockEntity node) {
        this.nodes.get(node.getNodeType()).remove(node);

        if (!hasNodes()) {
            PastelNetworkManager.getInstance(this.world.isClient).remove(this);
        }
    }

    private boolean hasNodes() {
        for (List<PastelNodeBlockEntity> nodeList : this.nodes.values()) {
            if (!nodeList.isEmpty()) {
                return true;
            }
        }
        return false;
    }


    public Map<PastelNodeType, List<PastelNodeBlockEntity>> getGroupedNodes() {
        return this.nodes;
    }

    public List<PastelNodeBlockEntity> getAllNodes() {
        List<PastelNodeBlockEntity> nodes = new ArrayList<>();
        for (Map.Entry<PastelNodeType, List<PastelNodeBlockEntity>> nodeList : this.nodes.entrySet()) {
            nodes.addAll(this.nodes.get(nodeList.getKey()));
        }
        return nodes;
    }

    public boolean canConnect(PastelNodeBlockEntity newNode) {
        if (newNode.getWorld() != this.world) {
            return false;
        }

        for (List<PastelNodeBlockEntity> nodeList : this.nodes.values()) {
            for (PastelNodeBlockEntity currentNode : nodeList) {
                if (currentNode.canSee(newNode)) {
                    return true;
                }
            }
        }
        return false;
    }

    // TODO: call
    public void merge(PastelNetwork network) {
        for (Map.Entry<PastelNodeType, List<PastelNodeBlockEntity>> nodeList : network.getGroupedNodes().entrySet()) {
            List<PastelNodeBlockEntity> existingNodes = this.nodes.get(nodeList.getKey());
            for (PastelNodeBlockEntity node : nodeList.getValue()) {
                existingNodes.add(node);
                node.setNetwork(this);
            }
        }
        PastelNetworkManager.getInstance(network.world.isClient).remove(network);
    }

    public void split() {
        //TODO
    }

    public void tickLogic() {
        for (PastelNodeBlockEntity pusher : this.nodes.get(PastelNodeType.PUSHER)) {
            Inventory pusherInventory = pusher.getConnectedInventory();
            if (pusherInventory != null) {
                InventoryStorage pusherStorage = InventoryStorage.of(pusherInventory, null);
                for (PastelNodeBlockEntity storage : this.nodes.get(PastelNodeType.STORAGE)) {
                    Inventory storageInventory = storage.getConnectedInventory();
                    if (storageInventory != null) {
                        InventoryStorage storageStorage = InventoryStorage.of(storageInventory, null);

                        ItemVariant stone = ItemVariant.of(Items.STONE);
                        try (Transaction transaction = Transaction.openOuter()) {
                            if (pusherStorage.extract(stone, 1, transaction) == 1 && storageStorage.insert(stone, 1, transaction) == 1) {
                                transaction.commit();
                            }
                        }
                    }
                }
            }
        }
    }

    private final SchedulerMap<BlockPos> particleCooldowns = new SchedulerMap<>();

    protected final boolean queueParticle(BlockPos blockPos) {
        if (!particleCooldowns.containsKey(blockPos)) {
            particleCooldowns.put(blockPos, 3);
            return true;
        }
        return false;
    }

    public String getName() {
        return this.name;
    }

    public UUID getUUID() {
        return this.uuid;
    }

}
