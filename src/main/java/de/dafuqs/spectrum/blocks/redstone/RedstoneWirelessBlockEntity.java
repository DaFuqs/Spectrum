package de.dafuqs.spectrum.blocks.redstone;

import de.dafuqs.spectrum.blocks.RedstonePoweredBlock;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;


public class RedstoneWirelessBlockEntity extends BlockEntity implements RedstonePoweredBlock {

    private UUID ownerUUID;
    private String ownerName;

    public RedstoneWirelessBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpectrumBlockEntityRegistry.REDSTONE_WIRELESS, blockPos, blockState);
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        return tag;
    }

    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
    }


}
