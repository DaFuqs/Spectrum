package de.dafuqs.spectrum.blocks.spirit_sallow;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

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
	public void setOwner(Player playerEntity) {
		this.ownerUUID = playerEntity.getUUID();
		setChanged();
	}
	
	// Serialize the BlockEntity
	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.saveAdditional(tag, registryLookup);
		
		if (this.ownerUUID != null) {
			tag.putUUID("OwnerUUID", this.ownerUUID);
		}
	}
	
	// Deserialize the BlockEntity
	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		super.loadAdditional(tag, registryLookup);
		
		if (tag.contains("OwnerUUID")) {
			this.ownerUUID = tag.getUUID("OwnerUUID");
		} else {
			this.ownerUUID = null;
		}
	}
	
}
