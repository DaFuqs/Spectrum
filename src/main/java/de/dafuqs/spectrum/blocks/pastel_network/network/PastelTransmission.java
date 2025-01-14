package de.dafuqs.spectrum.blocks.pastel_network.network;

import de.dafuqs.spectrum.blocks.pastel_network.nodes.*;
import de.dafuqs.spectrum.helpers.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.fabricmc.fabric.api.transfer.v1.transaction.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PastelTransmission implements SchedulerMap.Callback {
	
	private @Nullable ServerPastelNetwork network;
    private final List<BlockPos> nodePositions;
    private final ItemVariant variant;
    private final long amount;
    private final int vertexTime;
    
    public PastelTransmission(List<BlockPos> nodePositions, ItemVariant variant, long amount, int vertexTime) {
        this.nodePositions = nodePositions;
        this.variant = variant;
        this.amount = amount;
        this.vertexTime = vertexTime;
    }
	
	public void setNetwork(@NotNull ServerPastelNetwork network) {
        this.network = network;
    }

    public @Nullable PastelNetwork getNetwork() {
        return this.network;
    }

    public List<BlockPos> getNodePositions() {
        return nodePositions;
    }

    public int getVertexTime() {
        return vertexTime;
    }

    public int getTransmissionDuration() {
        return vertexTime * (nodePositions.size() - 1);
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
        if (nodePositions.size() == 0) {
            return;
        }

        BlockPos destinationPos = nodePositions.get(nodePositions.size() - 1);
        PastelNodeBlockEntity destinationNode = this.network.getNodeAt(destinationPos);
        World world = this.network.getWorld();
        if (!world.isClient) {
            int inserted = 0;
            if (destinationNode != null) {
                Storage<ItemVariant> destinationStorage = destinationNode.getConnectedStorage();
                if (destinationStorage != null) {
                    try (Transaction transaction = Transaction.openOuter()) {
                        if (destinationStorage.supportsInsertion()) {
                            inserted = (int) destinationStorage.insert(variant, amount, transaction);
                            destinationNode.addItemCountUnderway(-inserted);
                            transaction.commit();
                        }
                    }
                }
            }
            if (inserted != amount) {
                InWorldInteractionHelper.scatter(world, destinationPos.getX() + 0.5, destinationPos.getY() + 0.5, destinationPos.getZ() + 0.5, variant, amount - inserted);
            }
        }
    }

    public NbtCompound toNbt() {
        NbtCompound compound = new NbtCompound();
        compound.put("Variant", this.variant.toNbt());
        compound.putLong("Amount", this.amount);
        compound.putInt("VertexTime", this.vertexTime);
        NbtList posList = new NbtList();
        for (BlockPos pos : nodePositions) {
            NbtCompound posCompound = new NbtCompound();
            posCompound.putInt("X", pos.getX());
            posCompound.putInt("Y", pos.getY());
            posCompound.putInt("Z", pos.getZ());
            posList.add(posCompound);
        }
        compound.put("NodePositions", posList);
        return compound;
    }

    public static PastelTransmission fromNbt(NbtCompound nbt) {
        ItemVariant variant = ItemVariant.fromNbt(nbt.getCompound("Variant"));
        long amount = nbt.getLong("Amount");
        int time = nbt.getInt("VertexTime");

        List<BlockPos> posList = new ArrayList<>();
        for (NbtElement e : nbt.getList("NodePositions", NbtElement.COMPOUND_TYPE)) {
            NbtCompound compound = (NbtCompound) e;
            BlockPos blockPos = new BlockPos(compound.getInt("X"), compound.getInt("Y"), compound.getInt("Z"));
            posList.add(blockPos);
        }

        return new PastelTransmission(posList, variant, amount, time);
    }

    public static void writeToBuf(PacketByteBuf buf, PastelTransmission transfer) {
        buf.writeInt(transfer.nodePositions.size());
        for (BlockPos pos : transfer.nodePositions) {
            buf.writeBlockPos(pos);
        }
        transfer.variant.toPacket(buf);
        buf.writeLong(transfer.amount);
        buf.writeInt(transfer.vertexTime);
    }

    public static PastelTransmission fromPacket(PacketByteBuf buf) {
        int posCount = buf.readInt();
        List<BlockPos> posList = new ArrayList<>();
        for (int i = 0; i < posCount; i++) {
            posList.add(buf.readBlockPos());
        }
        ItemVariant variant = ItemVariant.fromPacket(buf);
        long amount = buf.readLong();
        int time = buf.readInt();
        return new PastelTransmission(posList, variant, amount, time);
    }

}
