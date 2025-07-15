package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

import java.util.*;

public class PlayerTrackerBlockEntity extends BlockEntity {
	
	private final List<UUID> playersThatOpenedAlready = new ArrayList<>();
	
	public PlayerTrackerBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PLAYER_TRACKING, pos, state);
	}
	
	public boolean hasTaken(Player player) {
		return this.playersThatOpenedAlready.contains(player.getUUID());
	}
	
	public void markTaken(Player player) {
		this.playersThatOpenedAlready.add(player.getUUID());
		setChanged();
	}
	
	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		if (!playersThatOpenedAlready.isEmpty()) {
			ListTag uuidList = new ListTag();
			for (UUID uuid : playersThatOpenedAlready) {
				CompoundTag nbtCompound = new CompoundTag();
				nbtCompound.putUUID("UUID", uuid);
				uuidList.add(nbtCompound);
			}
			tag.put("OpenedPlayers", uuidList);
		}
	}
	
	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registryLookup) {
		this.playersThatOpenedAlready.clear();
		if (tag.contains("OpenedPlayers", Tag.TAG_LIST)) {
			ListTag list = tag.getList("OpenedPlayers", Tag.TAG_COMPOUND);
			for (int i = 0; i < list.size(); i++) {
				CompoundTag compound = list.getCompound(i);
				UUID uuid = compound.getUUID("UUID");
				this.playersThatOpenedAlready.add(uuid);
			}
		}
	}
}
