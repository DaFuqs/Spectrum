package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

import java.util.*;

public class PastelTransfer implements SchedulerMap.Callback {

    private final List<PastelNodeBlockEntity> nodes;
    private final ItemVariant variant;
    private final int amount;

    public PastelTransfer(List<PastelNodeBlockEntity> nodes, ItemVariant variant, int amount) {
        this.nodes = nodes;
        this.variant = variant;
        this.amount = amount;
    }

    public List<PastelNodeBlockEntity> getNodes() {
        return nodes;
    }

    public ItemVariant getVariant() {
        return this.variant;
    }

    public long getAmount() {
        return this.amount;
    }

    @Override
    public void trigger() {
        PastelNodeBlockEntity lastNode = nodes.get(nodes.size() - 1);
        if (!lastNode.getWorld().isClient) {
            int inserted = 0;
            Storage<ItemVariant> destinationStorage = lastNode.getConnectedStorage();
            try (Transaction transaction = Transaction.openOuter()) {
                if (destinationStorage.supportsInsertion()) {
                    inserted = (int) destinationStorage.insert(variant, amount, transaction);
                    transaction.commit();
                }
            }
            if (inserted != amount) {
                BlockPos pos = lastNode.getPos();
                ItemScatterer.spawn(lastNode.getWorld(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, variant.toStack(amount - inserted));
            }
        }
    }

    public NbtCompound toNbt() {
        NbtCompound compound = new NbtCompound();
        compound.put("Variant", this.variant.toNbt());
        compound.putInt("Amount", this.amount);
        NbtList posList = new NbtList();
        for (PastelNodeBlockEntity node : nodes) {
            NbtCompound posCompound = new NbtCompound();
            posCompound.putInt("X", node.getPos().getX());
            posCompound.putInt("Y", node.getPos().getY());
            posCompound.putInt("Z", node.getPos().getZ());
            posList.add(posCompound);
        }
        compound.put("Nodes", posList);
        return compound;
    }

}
