package de.dafuqs.spectrum.api.block;

import de.dafuqs.spectrum.*;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.player.*;
import org.jetbrains.annotations.*;

import java.util.*;

public interface PlayerOwned {
	
	static Player getPlayerEntityIfOnline(UUID ownerUUID) {
		if (ownerUUID != null && SpectrumCommon.minecraftServer != null) {
			return SpectrumCommon.minecraftServer.getPlayerList().getPlayer(ownerUUID);
		}
		return null;
	}
	
	UUID getOwnerUUID();
	
	void setOwner(Player playerEntity);
	
	default boolean hasOwner() {
		return getOwnerUUID() != null;
	}
	
	default boolean isOwner(Player playerEntity) {
		return playerEntity.getUUID().equals(getOwnerUUID());
	}
	
	@Nullable
	default Player getOwnerIfOnline() {
		UUID ownerUUID = this.getOwnerUUID();
		if (ownerUUID != null && SpectrumCommon.minecraftServer != null) {
			return SpectrumCommon.minecraftServer.getPlayerList().getPlayer(ownerUUID);
		}
		return null;
	}
	
	static void writeOwnerUUID(CompoundTag nbt, UUID ownerUUID) {
		if (ownerUUID != null) {
			nbt.putUUID("OwnerUUID", ownerUUID);
		}
	}
	
	static UUID readOwnerUUID(CompoundTag nbt) {
		if (nbt.contains("OwnerUUID")) {
			return nbt.getUUID("OwnerUUID");
		}
		return null;
	}
	
	static void writeOwnerName(CompoundTag nbt, String ownerName) {
		if (ownerName != null) {
			nbt.putString("OwnerName", ownerName);
		}
	}
	
	static String readOwnerName(CompoundTag nbt) {
		if (nbt.contains("OwnerName")) {
			return nbt.getString("OwnerName");
		}
		return "???";
	}
	
}
