package de.dafuqs.spectrum.api.block;

import com.mojang.authlib.*;
import de.dafuqs.spectrum.*;
import net.fabricmc.fabric.api.entity.*;
import net.minecraft.nbt.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public interface PlayerOwned {
	
	static @Nullable Player getPlayerEntityIfOnline(@Nullable UUID ownerUUID) {
		if (ownerUUID != null && SpectrumCommon.minecraftServer != null) return SpectrumCommon.minecraftServer.getPlayerList().getPlayer(ownerUUID);
		return null;
	}
	
	@Nullable UUID getOwnerUUID();
	
	void setOwner(Player playerEntity);
	
	default boolean hasOwner() {
		return getOwnerUUID() != null;
	}
	
	default boolean isOwner(Player playerEntity) {
		return playerEntity.getUUID().equals(getOwnerUUID());
	}

	default @Nullable Player getOwnerIfOnline() {
		return getPlayerEntityIfOnline(this.getOwnerUUID());
	}

	default @Nullable Player getOwnerIfOnline(Level level) {
		UUID ownerUUID = this.getOwnerUUID();
		if (ownerUUID == null) return null;
		if (level instanceof ServerLevel serverLevel) return serverLevel.getServer().getPlayerList().getPlayer(ownerUUID);
		return level.getPlayerByUUID(ownerUUID);
	}

	default Player getFakeOwner(ServerLevel level) {
		UUID ownerUUID = this.getOwnerUUID();
		assert ownerUUID != null; // needed for fake player, obviously
		GameProfile fakeProfile = new GameProfile(ownerUUID, "[Block Breaker of " + ownerUUID + "]");
		return FakePlayer.get(level, fakeProfile);
	}
	
	static void writeOwnerUUID(CompoundTag nbt, @Nullable UUID ownerUUID) {
		if (ownerUUID != null) nbt.putUUID("OwnerUUID", ownerUUID);
	}
	
	static @Nullable UUID readOwnerUUID(CompoundTag nbt) {
		return nbt.contains("OwnerUUID") ? nbt.getUUID("OwnerUUID") : null;
	}
	
	static void writeOwnerName(CompoundTag nbt, @Nullable String ownerName) {
		if (ownerName != null) nbt.putString("OwnerName", ownerName);
	}
	
	static String readOwnerName(CompoundTag nbt) {
		return nbt.contains("OwnerName") ? nbt.getString("OwnerName") : "???";
	}
	
}
