package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.inventory.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.listener.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class PastelNodeBlockEntity extends BlockEntity {

    public static int RANGE = 16;
    protected PastelNetwork network;
    protected @Nullable UUID networkUUIDToMerge = null;

    public PastelNodeBlockEntity(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public boolean canSee(PastelNodeBlockEntity node) {
        return node.pos.isWithinDistance(this.pos, RANGE);
    }

    public @Nullable Inventory getConnectedInventory() {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof PastelNodeBlock) {
            Direction direction = state.get(PastelNodeBlock.FACING).getOpposite();
            BlockEntity connectedBlockEntity = world.getBlockEntity(pos.offset(direction));
            if (connectedBlockEntity instanceof Inventory inventory) {
                return inventory;
            }
        }
        return null;
    }

    public void onBreak() {
        if (this.network != null) {
            this.network.removeNode(this);
        }
    }

    public PastelNetwork getNetwork() {
        return this.network;
    }

    public abstract PastelNodeType getNodeType();

    public void setNetwork(PastelNetwork network) {
        this.network = network;
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        if (!world.isClient) {
            if (this.networkUUIDToMerge != null) {
                this.network = Pastel.getServerInstance().joinNetwork(this, this.networkUUIDToMerge);
            } else if (this.network == null) {
                this.network = Pastel.getServerInstance().joinNetwork(this);
            }
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("Network")) {
            UUID networkUUID = nbt.getUuid("Network");
            if (this.world == null) {
                this.networkUUIDToMerge = networkUUID;
            } else {
                this.network = Pastel.getInstance(world.isClient).joinNetwork(this, networkUUID);
            }
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (this.network != null) {
            nbt.putUuid("Network", this.network.getUUID());
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        this.writeNbt(nbtCompound);
        return nbtCompound;
    }

    // interaction methods
    public void updateInClientWorld() {
        ((ServerWorld) world).getChunkManager().markForUpdate(pos);
    }

    public Direction getDirection() {
        try {
            return world.getBlockState(pos).get(PastelNodeBlock.FACING);
        } catch (Exception e) {
            return Direction.DOWN;
        }
    }

}
