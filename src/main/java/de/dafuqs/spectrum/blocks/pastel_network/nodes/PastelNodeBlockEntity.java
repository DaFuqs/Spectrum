package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.blocks.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.screenhandler.v1.*;
import net.fabricmc.fabric.api.transfer.v1.item.*;
import net.fabricmc.fabric.api.transfer.v1.storage.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.listener.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.screen.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.text.*;
import net.minecraft.util.collection.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class PastelNodeBlockEntity extends BlockEntity implements FilterConfigurable, ExtendedScreenHandlerFactory {

    public static int ITEM_FILTER_COUNT = 5;
    public static int RANGE = 16;
    protected PastelNetwork network;
    protected @Nullable UUID networkUUIDToMerge = null;
    protected long lastTransferTick = 0;
    protected long cachedRedstonePowerTick = 0;
    protected boolean cachedNoRedstonePower = true;

    private final List<Item> filterItems;

    public PastelNodeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpectrumBlockEntities.PASTEL_NODE, blockPos, blockState);
        this.filterItems = DefaultedList.ofSize(ITEM_FILTER_COUNT, Items.AIR);
    }

    public @Nullable Storage<ItemVariant> getConnectedStorage() {
        BlockState state = this.getCachedState();
        if (state.getBlock() instanceof PastelNodeBlock) {
            Direction direction = state.get(PastelNodeBlock.FACING).getOpposite();
            return ItemStorage.SIDED.find(this.world, this.getPos().offset(direction), direction);
        }
        return null;
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        if (!world.isClient) {
            if (this.networkUUIDToMerge != null) {
                this.network = Pastel.getServerInstance().joinNetwork(this, this.networkUUIDToMerge);
                this.networkUUIDToMerge = null;
            } else if (this.network == null) {
                this.network = Pastel.getServerInstance().joinNetwork(this, null);
            }
        }
    }

    public boolean canTransfer() {
        long time = this.world.getTime();
        if (time > this.cachedRedstonePowerTick) {
            this.cachedNoRedstonePower = world.getReceivedRedstonePower(this.pos) == 0;
        }
        return this.world.getTime() > lastTransferTick && this.cachedNoRedstonePower;
    }

    public void markTransferred() {
        this.lastTransferTick = world.getTime();
        this.markDirty();
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
        if (nbt.contains("LastTransferTick", NbtElement.LONG_TYPE)) {
            this.lastTransferTick = nbt.getLong("LastTransferTick");
        }
        if (this.getNodeType().usesFilters()) {
            readFilterNbt(nbt, this.filterItems);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (this.network != null) {
            nbt.putUuid("Network", this.network.getUUID());
        }
        nbt.putLong("LastTransferTick", this.lastTransferTick);
        if (this.getNodeType().usesFilters()) {
            writeFilterNbt(nbt, this.filterItems);
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

    // triggered when the chunk is unloaded, or the world quit
    @Override
    public void markRemoved() {
        super.markRemoved();
        if (this.network != null && this.world != null) {
            this.network.removeNode(this, NodeRemovalReason.UNLOADED);
            this.network = null;
        }
    }

    public void onBroken() {
        if (this.network != null) {
            this.network.removeNode(this, NodeRemovalReason.BROKEN);
            this.network = null;
        }
    }

    public boolean canConnect(PastelNodeBlockEntity node) {
        return this.pos.isWithinDistance(node.pos, RANGE);
    }

    public PastelNetwork getNetwork() {
        return this.network;
    }

    public PastelNodeType getNodeType() {
        if (this.getCachedState().getBlock() instanceof PastelNodeBlock pastelNodeBlock) {
            return pastelNodeBlock.pastelNodeType;
        }
        return PastelNodeType.CONNECTION;
    }

    public void setNetwork(PastelNetwork network) {
        this.network = network;
        if (this.world != null && !this.world.isClient) {
            updateInClientWorld();
            this.markDirty();
        }
    }

    // interaction methods
    public void updateInClientWorld() {
        ((ServerWorld) world).getChunkManager().markForUpdate(pos);
    }

    @Override
    public int getFilterCount() {
        return getNodeType().usesFilters() ? ITEM_FILTER_COUNT : 0;
    }

    @Override
    public List<Item> getItemFilters() {
        return this.filterItems;
    }

    @Override
    public void setFilterItem(int slot, Item item) {
        this.filterItems.set(slot, item);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.spectrum.pastel_node");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new FilteringScreenHandler(syncId, inv, this);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        FilterConfigurable.writeScreenOpeningData(buf, filterItems);
    }

}
