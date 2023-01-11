package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PastelTransmission implements SchedulerMap.Callback {

    private @Nullable PastelNetwork network;
    private final List<BlockPos> nodePositions;
    private final ItemVariant variant;
    private final int amount;

    public PastelTransmission(List<BlockPos> nodePositions, ItemVariant variant, int amount) {
        this.nodePositions = nodePositions;
        this.variant = variant;
        this.amount = amount;
    }

    public void setNetwork(@NotNull PastelNetwork network) {
        this.network = network;
    }

    public @Nullable PastelNetwork getNetwork() {
        return this.network;
    }

    public List<BlockPos> getNodePositions() {
        return nodePositions;
    }

    public ItemVariant getVariant() {
        return this.variant;
    }

    public long getAmount() {
        return this.amount;
    }

    public BlockPos getStartPos() {
        return this.nodePositions.get(0);
    }

    @Override
    public void trigger() {
        arriveAtDestination();
    }

    private void arriveAtDestination() {
        BlockPos destinationPos = nodePositions.get(nodePositions.size() - 1);
        PastelNodeBlockEntity destinationNode = this.network.getNodeAt(destinationPos);
        if (!this.network.getWorld().isClient) {
            int inserted = 0;
            if (destinationNode != null) {
                Storage<ItemVariant> destinationStorage = destinationNode.getConnectedStorage();
                if (destinationStorage != null) {
                    try (Transaction transaction = Transaction.openOuter()) {
                        if (destinationStorage.supportsInsertion()) {
                            inserted = (int) destinationStorage.insert(variant, amount, transaction);
                            transaction.commit();
                        }
                    }
                }
            }
            if (inserted != amount) {
                ItemScatterer.spawn(this.network.getWorld(), destinationPos.getX() + 0.5, destinationPos.getY() + 0.5, destinationPos.getZ() + 0.5, variant.toStack(amount - inserted));
            }
        }
    }

    public NbtCompound toNbt() {
        NbtCompound compound = new NbtCompound();
        compound.put("Variant", this.variant.toNbt());
        compound.putInt("Amount", this.amount);
        NbtList posList = new NbtList();
        for (BlockPos pos : nodePositions) {
            NbtCompound posCompound = new NbtCompound();
            posCompound.putInt("X", pos.getX());
            posCompound.putInt("Y", pos.getY());
            posCompound.putInt("Z", pos.getZ());
            posList.add(posCompound);
        }
        compound.put("Nodes", posList);
        return compound;
    }

    public PastelTransmission fromNbt(NbtCompound nbt) {
        ItemVariant variant = ItemVariant.fromNbt(nbt.getCompound("Variant"));
        int amount = nbt.getInt("Amount");

        List<BlockPos> posList = new ArrayList<>();
        for (NbtElement e : nbt.getList("", NbtElement.COMPOUND_TYPE)) {
            NbtCompound compound = (NbtCompound) e;
            BlockPos blockPos = new BlockPos(compound.getInt("X"), compound.getInt("Y"), compound.getInt("Z"));
            posList.add(blockPos);
        }

        return new PastelTransmission(posList, variant, amount);
    }

    public static void writeToBuf(PacketByteBuf buf, PastelTransmission transfer) {
        buf.writeInt(transfer.nodePositions.size());
        for (BlockPos pos : transfer.nodePositions) {
            buf.writeBlockPos(pos);
        }
        transfer.variant.toPacket(buf);
        buf.writeInt(transfer.amount);
    }

    public static PastelTransmission fromPacket(PacketByteBuf buf) {
        int posCount = buf.readInt();
        List<BlockPos> posList = new ArrayList<>();
        for (int i = 0; i < posCount; i++) {
            posList.add(buf.readBlockPos());
        }
        ItemVariant variant = ItemVariant.fromPacket(buf);
        int amount = buf.readInt();
        return new PastelTransmission(posList, variant, amount);
    }

}
