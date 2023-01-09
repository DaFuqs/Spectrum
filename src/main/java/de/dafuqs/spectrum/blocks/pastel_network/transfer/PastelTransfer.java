package de.dafuqs.spectrum.blocks.pastel_network.transfer;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

import java.util.*;

public class PastelTransfer implements SchedulerMap.Callback {

    private final List<PastelNodeBlockEntity> nodes;
    private final ItemVariant transport;
    private final int amount;

    public PastelTransfer(List<PastelNodeBlockEntity> nodes, ItemVariant transport, int amount) {
        this.nodes = nodes;
        this.transport = transport;
        this.amount = amount;
    }

    public List<PastelNodeBlockEntity> getNodes() {
        return nodes;
    }

    public ItemVariant getTransport() {
        return this.transport;
    }

    public long getAmount() {
        return this.amount;
    }

    @Override
    public void trigger() {
        PastelNodeBlockEntity lastNode = nodes.get(nodes.size() - 1);
        if (!lastNode.getWorld().isClient) {
            int inserted = 0;
            if (lastNode.getConnectedInventory() != null) {
                InventoryStorage destinationStorage = InventoryStorage.of(lastNode.getConnectedInventory(), null);
                try (Transaction transaction = Transaction.openOuter()) {
                    if (destinationStorage.supportsInsertion()) {
                        inserted = (int) destinationStorage.insert(transport, amount, transaction);
                        transaction.commit();
                    }
                }
            }
            if (inserted != amount) {
                BlockPos pos = lastNode.getPos();
                ItemScatterer.spawn(lastNode.getWorld(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, transport.toStack(amount - inserted));
            }
        }
    }
}
