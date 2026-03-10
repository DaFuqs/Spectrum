package de.dafuqs.spectrum.api.block;

import net.minecraft.nbt.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.scores.*;

import java.util.*;

public interface PlayerOwnedWithName extends PlayerOwned {
	
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
	
	String getOwnerName();
	
	default boolean isOwnerOrSameTeamAsOwner(Player player) {
		if(getOwnerUUID().equals(player.getUUID())) {
			return true;
		}
		
		PlayerTeam team = player.getTeam();
		return  team != null && team.getPlayers().contains(getOwnerName());
	}
	
}
