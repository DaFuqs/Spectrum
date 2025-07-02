package de.dafuqs.spectrum.blocks.redstone;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class BlockBreakerBlockEntity extends BlockEntity implements PlayerOwned {
	
	private UUID ownerUUID;
	
	public BlockBreakerBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.BLOCK_BREAKER, pos, state);
	}
	
	@Override
	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	
	@Override
	public void setOwner(Player playerEntity) {
		this.ownerUUID = playerEntity.getUUID();
		this.setChanged();
	}
	
	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.saveAdditional(nbt, registryLookup);
		
		PlayerOwned.writeOwnerUUID(nbt, this.ownerUUID);
	}
	
	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
		super.loadAdditional(nbt, registryLookup);
		
		this.ownerUUID = PlayerOwned.readOwnerUUID(nbt);
	}
	
}
