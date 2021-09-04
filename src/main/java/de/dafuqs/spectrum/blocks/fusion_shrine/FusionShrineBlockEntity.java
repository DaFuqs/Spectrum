package de.dafuqs.spectrum.blocks.fusion_shrine;

import de.dafuqs.spectrum.networking.SpectrumS2CPackets;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FusionShrineBlockEntity extends BlockEntity {

    protected int INVENTORY_SIZE = 8;
    protected DefaultedList<ItemStack> inventory;
    Fluid storedFluid;

    public FusionShrineBlockEntity(BlockPos pos, BlockState state) {
        super(SpectrumBlockEntityRegistry.FUSION_SHRINE, pos, state);
        this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
        storedFluid = Fluids.EMPTY;
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory);
        this.storedFluid = Registry.FLUID.get(Identifier.tryParse(nbt.getString("fluid")));
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putString("fluid", Registry.FLUID.getId(storedFluid).toString());
        return nbt;
    }

    public static void tick(@NotNull World world, BlockPos blockPos, BlockState blockState, FusionShrineBlockEntity fusionShrineBlockEntity) {

    }

    public @NotNull Fluid getFluid() {
        return this.storedFluid;
    }

    public void setFluid(@NotNull Fluid fluid) {
        this.storedFluid = fluid;
        world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), Block.NO_REDRAW);
        world.addSyncedBlockEvent(pos, SpectrumBlocks.FUSION_SHRINE, 1, 0);
        this.markDirty();
    }

    public NbtCompound toInitialChunkDataNbt() {
        return this.writeNbt(new NbtCompound());
    }

    @Nullable
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        SpectrumS2CPackets.sendBlockEntityUpdate(this, SpectrumS2CPackets.BlockEntityUpdatePacketID.FUSION_SHRINE);
        return null;
    }

}