package de.dafuqs.pigment.blocks.detector;

import de.dafuqs.pigment.interfaces.PlayerOwned;
import de.dafuqs.pigment.registries.PigmentBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;


public class PlayerDetectorBlockEntity extends BlockEntity implements PlayerOwned {

    private UUID ownerUUID;
    private String ownerName;

    public PlayerDetectorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PigmentBlockEntityTypes.PLAYER_DETECTOR, blockPos, blockState);
    }

    public CompoundTag writeNbt(CompoundTag tag) {
        super.writeNbt(tag);

        if(this.ownerUUID !=  null) {
            tag.putUuid("UUID", this.ownerUUID);
        }
        if(this.ownerName !=  null) {
            tag.putString("OwnerName", this.ownerName);
        }

        return tag;
    }

    public void readNbt(CompoundTag tag) {
        super.readNbt(tag);

        if(tag.contains("UUID")) {
            this.ownerUUID = tag.getUuid("UUID");
        } else {
            this.ownerUUID = null;
        }
        if(tag.contains("OwnerName")) {
            this.ownerName = tag.getString("OwnerName");
        } else {
            this.ownerName = "";
        }
    }

    @Override
    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    public void setOwnerName(String name) {
        this.ownerName = name;
    }
    public String getOwnerName() {
        return this.ownerName;
    }

    public void setPlayerData(UUID uuid, String name) {
        this.setOwnerUUID(uuid);
        this.setOwnerName(name);

    }
}
