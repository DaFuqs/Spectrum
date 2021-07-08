package de.dafuqs.pigment.blocks.ender;

import de.dafuqs.pigment.interfaces.PlayerOwned;
import de.dafuqs.pigment.registries.PigmentBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class EnderHopperBlockEntity extends BlockEntity implements PlayerOwned {

    private UUID ownerUUID;
    private String ownerName;

    public EnderHopperBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PigmentBlockEntityRegistry.ENDER_HOPPER, blockPos, blockState);
    }

    protected Text getContainerName() {
        if(this.ownerName == null) {
            return new TranslatableText("block.pigment.ender_hopper");
        } else {
            return new TranslatableText("block.pigment.ender_hopper_with_owner", this.ownerName);
        }
    }

    public ItemStack getStack(int slot) {
        PlayerEntity playerEntity = world.getPlayerByUuid(this.ownerUUID);
        EnderChestInventory enderInventory = playerEntity.getEnderChestInventory();
        return enderInventory.getStack(slot);
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

    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        if(tag.contains("OwnerUUID")) {
            this.ownerUUID = tag.getUuid("OwnerUUID");
        } else {
            this.ownerUUID = null;
        }
        if(tag.contains("OwnerName")) {
            this.ownerName = tag.getString("OwnerName");
        } else {
            this.ownerName = null;
        }
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        if(this.ownerUUID != null) {
            tag.putUuid("OwnerUUID", this.ownerUUID);
        }
        if(this.ownerName != null) {
            tag.putString("OwnerName", this.ownerName);
        }

        return tag;
    }

}
