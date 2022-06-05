package de.dafuqs.spectrum.blocks.spirit_sallow;

import de.dafuqs.spectrum.interfaces.PlayerOwned;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class OminousSaplingBlockEntity extends BlockEntity implements PlayerOwned {
	
	public UUID ownerUUID;
	
	public OminousSaplingBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntityRegistry.OMINOUS_SAPLING, blockPos, blockState);
	}
	
	public OminousSaplingBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(PlayerEntity playerEntity) {
		this.ownerUUID = playerEntity.getUuid();
	}
	
	// Serialize the BlockEntity
	@Override
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		
		if (this.ownerUUID != null) {
			tag.putUuid("OwnerUUID", this.ownerUUID);
		}
	}
	
	// Deserialize the BlockEntity
	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		
		if (tag.contains("OwnerUUID")) {
			this.ownerUUID = tag.getUuid("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
	}
	
}
