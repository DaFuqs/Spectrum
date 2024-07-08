package de.dafuqs.spectrum.api.block;

import de.dafuqs.spectrum.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface PlayerOwned {
	
	static PlayerEntity getPlayerEntityIfOnline(UUID ownerUUID) {
		if (ownerUUID != null && SpectrumCommon.minecraftServer != null) {
			return SpectrumCommon.minecraftServer.getPlayerManager().getPlayer(ownerUUID);
		}
		return null;
	}
	
	UUID getOwnerUUID();
	
	void setOwner(PlayerEntity playerEntity);
	
	default boolean hasOwner() {
		return getOwnerUUID() != null;
	}
	
	default boolean isOwner(PlayerEntity playerEntity) {
		return playerEntity.getUuid().equals(getOwnerUUID());
	}
	
	@Nullable
	default PlayerEntity getOwnerIfOnline() {
		UUID ownerUUID = this.getOwnerUUID();
		if (ownerUUID != null) {
			return SpectrumCommon.minecraftServer.getPlayerManager().getPlayer(ownerUUID);
		}
		return null;
	}
	
	static void writeOwnerUUID(NbtCompound nbt, UUID ownerUUID) {
		if (ownerUUID != null) {
			nbt.putUuid("OwnerUUID", ownerUUID);
		}
	}
	
	static UUID readOwnerUUID(NbtCompound nbt) {
		if (nbt.contains("OwnerUUID")) {
			return nbt.getUuid("OwnerUUID");
		}
		return null;
	}
	
	static void writeOwnerName(NbtCompound nbt, String ownerName) {
		if (ownerName != null) {
			nbt.putString("OwnerName", ownerName);
		}
	}
	
	static String readOwnerName(NbtCompound nbt) {
		if (nbt.contains("OwnerName")) {
			return nbt.getString("OwnerName");
		}
		return "???";
	}
	
}
