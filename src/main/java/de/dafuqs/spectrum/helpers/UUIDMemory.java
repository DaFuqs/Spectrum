package de.dafuqs.spectrum.helpers;

import net.minecraft.nbt.*;
import net.minecraft.world.entity.player.*;

import java.util.*;

public class UUIDMemory {
	
	private final List<UUID> UUIDs = new ArrayList<>();
	
	public Tag toNbt() {
		CompoundTag nbt = new CompoundTag();
		if (UUIDs.isEmpty()) return nbt;
		
		ListTag uuidList = new ListTag();
		for (UUID uuid : UUIDs) {
			CompoundTag nbtCompound = new CompoundTag();
			nbtCompound.putUUID("uuid", uuid);
			uuidList.add(nbtCompound);
		}
		nbt.put("ids", uuidList);
		return nbt;
	}
	
	public static UUIDMemory fromNbt(CompoundTag tag) {
		UUIDMemory memory = new UUIDMemory();
		if (!tag.contains("ids", Tag.TAG_LIST)) return memory;
		
		ListTag list = tag.getList("ids", Tag.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {
			CompoundTag compound = list.getCompound(i);
			UUID uuid = compound.getUUID("uuid");
			memory.UUIDs.add(uuid);
		}
		return memory;
	}
	
	public boolean hasUUID(UUID uuid) {
		return this.UUIDs.contains(uuid);
	}
	
	public void addUUID(UUID uuid) {
		this.UUIDs.add(uuid);
	}
	
	public boolean hasUUID(Player player) {
		return hasUUID(player.getUUID());
	}
	
	public void addUUID(Player player) {
		addUUID(player.getUUID());
	}
}
