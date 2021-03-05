package de.dafuqs.pigment.blocks.chromatic_tree;

import de.dafuqs.pigment.interfaces.PlayerOwned;
import de.dafuqs.pigment.registries.PigmentBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class OminousSaplingBlockEntity extends BlockEntity implements PlayerOwned {

    public UUID ownerUUID;
    public String ownerName;

    public OminousSaplingBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PigmentBlockEntityTypes.OMINOUS_SAPLING_BLOCK_ENTITY_TYPE, blockPos, blockState);
    }

    public OminousSaplingBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Override
    public String getOwnerName() {
        return this.ownerName;
    }

    @Override
    public void setOwner(PlayerEntity playerEntity) {
        this.ownerUUID = playerEntity.getUuid();
        this.ownerName = playerEntity.getName().asString();
    }

    // Serialize the BlockEntity
    @Override
    public CompoundTag writeNbt(CompoundTag tag) {
        super.writeNbt(tag);

        // Save the current value of the number to the tag
        if(this.ownerUUID != null) {
            tag.putUuid("OwnerUUID", this.ownerUUID);
        }
        if(this.ownerName != null) {
            tag.putString("OwnerName", this.ownerName);
        }

        return tag;
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(CompoundTag tag) {
        super.readNbt(tag);

        if(tag.contains("OwnerUUID")) {
            this.ownerUUID = tag.getUuid("OwnerUUID");
        } else {
            this.ownerUUID = null;
        }
        if(tag.contains("OwnerName")) {
            this.ownerName = tag.getString("OwnerName");
        } else {
            this.ownerName = "???";
        }
    }

}
