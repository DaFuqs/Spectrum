package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.util.math.*;

import java.util.*;

public class ManxiBlockEntity extends BlockEntity {

	private final Set<UUID> takers = new HashSet<>();

	public ManxiBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.MANXI, pos, state);
	}

	public boolean hasTaken(PlayerEntity player) {
		return takers.contains(player.getUuid());
	}

	public void markTaken(PlayerEntity player) {
		takers.add(player.getUuid());
		markDirty();
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		int i = 0;
		for (UUID taker : takers) {
			nbt.putUuid("uuid" + i, taker);
		}
		nbt.putInt("length", takers.size());
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		var size = nbt.getInt("length");
		for (int i = 0; i < size; i++) {
			takers.add(nbt.getUuid("uuid" + i));
		}
	}
}
