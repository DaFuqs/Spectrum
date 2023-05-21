package de.dafuqs.spectrum.blocks.redstone;

import de.dafuqs.spectrum.interfaces.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.util.math.*;

import java.util.*;


public class PlayerDetectorBlockEntity extends BlockEntity implements PlayerOwned {
	
	private UUID ownerUUID;
	private String ownerName;
	
	public PlayerDetectorBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.PLAYER_DETECTOR, blockPos, blockState);
	}
	
	@Override
	public void writeNbt(NbtCompound tag) {
		super.writeNbt(tag);
		
		if (this.ownerUUID != null) {
			tag.putUuid("UUID", this.ownerUUID);
		}
		if (this.ownerName != null) {
			tag.putString("OwnerName", this.ownerName);
		}
	}
	
	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		
		if (tag.contains("UUID")) {
			this.ownerUUID = tag.getUuid("UUID");
		} else {
			this.ownerUUID = null;
		}
		if (tag.contains("OwnerName")) {
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
		this.ownerName = playerEntity.getName().getString();
	}
	
}
