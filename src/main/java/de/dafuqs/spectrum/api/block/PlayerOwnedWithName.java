package de.dafuqs.spectrum.api.block;

import net.minecraft.nbt.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.scores.*;
import org.jspecify.annotations.*;

import java.util.*;

public interface PlayerOwnedWithName extends PlayerOwned {
	
	static @Nullable UUID readOwnerUUID(CompoundTag nbt) {
		if (nbt.contains("OwnerUUID")) {
			return nbt.getUUID("OwnerUUID");
		}
		return null;
	}
	
	static void writeOwnerName(CompoundTag nbt, @Nullable String ownerName) {
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
	
	@Nullable String getOwnerName();
	
	default boolean isOwnerOrSameTeamAsOwner(Player player) {
		UUID ownerUUID = getOwnerUUID();
		if(ownerUUID == null) {
			return false;
		}
		if(ownerUUID.equals(player.getUUID())) {
			return true;
		}
		PlayerTeam team = player.getTeam();
		return  team != null && team.getPlayers().contains(getOwnerName());
	}
	
}
