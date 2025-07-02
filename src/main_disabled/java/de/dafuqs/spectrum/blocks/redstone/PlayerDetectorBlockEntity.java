package de.dafuqs.spectrum.blocks.redstone;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;


public class PlayerDetectorBlockEntity extends BlockEntity implements PlayerOwned {
	
	private UUID ownerUUID;
	private String ownerName;
	
	public PlayerDetectorBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.PLAYER_DETECTOR, blockPos, blockState);
	}
	
	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.saveAdditional(tag, registryLookup);
		
		if (this.ownerUUID != null) {
			tag.putUUID("UUID", this.ownerUUID);
		}
		if (this.ownerName != null) {
			tag.putString("OwnerName", this.ownerName);
		}
	}
	
	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.loadAdditional(tag, registryLookup);
		
		if (tag.contains("UUID")) {
			this.ownerUUID = tag.getUUID("UUID");
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
	public void setOwner(Player playerEntity) {
		this.ownerUUID = playerEntity.getUUID();
		this.ownerName = playerEntity.getName().getString();
		setChanged();
	}
	
}
