package de.dafuqs.pigment.blocks.detector;

import de.dafuqs.pigment.interfaces.PlayerOwned;
import de.dafuqs.pigment.registries.PigmentBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;


public class PlayerDetectorBlockEntity extends BlockEntity implements PlayerOwned {

    private UUID ownerUUID;
    private String ownerName;

    public PlayerDetectorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PigmentBlockEntityRegistry.PLAYER_DETECTOR, blockPos, blockState);
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        if(this.ownerUUID !=  null) {
            tag.putUuid("UUID", this.ownerUUID);
        }
        if(this.ownerName !=  null) {
            tag.putString("OwnerName", this.ownerName);
        }

        return tag;
    }

    public void readNbt(NbtCompound tag) {
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
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    @Override
    public void setOwner(PlayerEntity playerEntity) {
        this.ownerUUID = playerEntity.getUuid();
        this.ownerName = playerEntity.getName().asString();
    }

}
