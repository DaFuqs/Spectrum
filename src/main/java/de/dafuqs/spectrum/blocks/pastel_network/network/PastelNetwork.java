package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.blocks.pastel_network.transfer.*;
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
    protected SchedulerMap<PastelTransfer> transfers = new SchedulerMap<>();

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
            Pastel.getInstance(this.world.isClient).remove(this);
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

    public List<PastelNodeBlockEntity> getNodes(PastelNodeType type) {
        return this.nodes.get(type);
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
        for (Map.Entry<PastelNodeType, List<PastelNodeBlockEntity>> nodeList : this.nodes.entrySet()) {
            List<PastelNodeBlockEntity> existingNodes = this.nodes.get(nodeList.getKey());
            for (PastelNodeBlockEntity node : nodeList.getValue()) {
                existingNodes.add(node);
                node.setNetwork(this);
            }
        }
        Pastel.getInstance(network.world.isClient).remove(network);
    }

    public void split() {
        //TODO
    }

    public void tick() {
        transfers.tick();
    }

    public String getName() {
        return this.name;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void addTransfer(PastelTransfer transfer, int travelTime) {
        this.transfers.put(transfer, travelTime);
    }

}
