package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.util.math.*;

import java.util.*;

public class PlayerTrackerBlockEntity extends BlockEntity {

	private final List<UUID> playersThatOpenedAlready = new ArrayList<>();

	public PlayerTrackerBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.PLAYER_TRACKING, pos, state);
	}

	public boolean hasTaken(PlayerEntity player) {
		return this.playersThatOpenedAlready.contains(player.getUuid());
	}

	public void markTaken(PlayerEntity player) {
		this.playersThatOpenedAlready.add(player.getUuid());
		markDirty();
	}

	@Override
	protected void writeNbt(NbtCompound tag) {
		if (!playersThatOpenedAlready.isEmpty()) {
			NbtList uuidList = new NbtList();
			for (UUID uuid : playersThatOpenedAlready) {
				NbtCompound nbtCompound = new NbtCompound();
				nbtCompound.putUuid("UUID", uuid);
				uuidList.add(nbtCompound);
			}
			tag.put("OpenedPlayers", uuidList);
		}
	}

	@Override
	public void readNbt(NbtCompound tag) {
		this.playersThatOpenedAlready.clear();
		if (tag.contains("OpenedPlayers", NbtElement.LIST_TYPE)) {
			NbtList list = tag.getList("OpenedPlayers", NbtElement.COMPOUND_TYPE);
			for (int i = 0; i < list.size(); i++) {
				NbtCompound compound = list.getCompound(i);
				UUID uuid = compound.getUuid("UUID");
				this.playersThatOpenedAlready.add(uuid);
			}
		}
	}
}
