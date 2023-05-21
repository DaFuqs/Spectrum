package de.dafuqs.spectrum.blocks.spirit_sallow;

import de.dafuqs.spectrum.interfaces.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.util.math.*;

import java.util.*;

public class OminousSaplingBlockEntity extends BlockEntity implements PlayerOwned {
	
	public UUID ownerUUID;
	
	public OminousSaplingBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.OMINOUS_SAPLING, blockPos, blockState);
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
