package de.dafuqs.spectrum.api.block;

import net.minecraft.nbt.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.scores.*;
import org.jspecify.annotations.*;

import java.util.*;

public interface PlayerOwnedWithName extends PlayerOwned {
	
	static @Nullable UUID readOwnerUUID(CompoundTag nbt) {
		return nbt.contains("OwnerUUID") ? nbt.getUUID("OwnerUUID") : null;
	}
	
	static void writeOwnerName(CompoundTag nbt, @Nullable String ownerName) {
		if (ownerName != null) nbt.putString("OwnerName", ownerName);
	}
	
	static String readOwnerName(CompoundTag nbt) {
		return nbt.contains("OwnerName") ? nbt.getString("OwnerName") : "???";
	}
	
	@Nullable String getOwnerName();
	
	default boolean isOwnerOrSameTeamAsOwner(Player player) {
		if (Objects.equals(getOwnerUUID(), player.getUUID())) return true;
		
		PlayerTeam team = player.getTeam();
		return team != null && team.getPlayers().contains(getOwnerName());
	}
	
}
