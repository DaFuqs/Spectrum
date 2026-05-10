package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class PlayerTrackerBlockEntity extends BlockEntity {
	
	private UUIDMemory uuidMemory = new UUIDMemory();

	public PlayerTrackerBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PLAYER_TRACKING, pos, state);
	}
	
	public boolean hasTaken(Player player) {
		return this.uuidMemory.hasUUID(player);
	}
	
	public void markTaken(Player player) {
		this.uuidMemory.addUUID(player);
		setChanged();
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		tag.put("player_memory", this.uuidMemory.toNbt());
	}

	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		if (tag.contains("player_memory"))
			this.uuidMemory = UUIDMemory.fromNbt(tag.getCompound("player_memory"));
	}
}
