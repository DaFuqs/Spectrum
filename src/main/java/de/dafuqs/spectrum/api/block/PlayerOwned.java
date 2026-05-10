package de.dafuqs.spectrum.api.block;

import de.dafuqs.spectrum.*;
import net.minecraft.nbt.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import org.jspecify.annotations.Nullable;

import java.util.*;

public interface PlayerOwned {
	
	@Nullable UUID getOwnerUUID();
	
	void setOwner(Player playerEntity);
	
	default boolean hasOwner() {
		return getOwnerUUID() != null;
	}
	
	default boolean isOwner(Player playerEntity) {
		return playerEntity.getUUID().equals(getOwnerUUID());
	}

	default @Nullable Player getOwnerIfOnline(@Nullable Level level) {
		return getPlayerIfOnline(level, this.getOwnerUUID());
	}

	static @Nullable Player getPlayerIfOnline(@Nullable Level level, @Nullable UUID ownerUUID) {
		if (ownerUUID == null || level == null) {
			return null;
		}
		if(level instanceof ServerLevel serverLevel) {
			return serverLevel.getServer().getPlayerList().getPlayer(ownerUUID);
		} else {
			return level.getPlayerByUUID(ownerUUID);
		}
	}
	
	static void writeOwnerUUID(CompoundTag nbt, @Nullable UUID ownerUUID) {
		if (ownerUUID != null) {
			nbt.putUUID("OwnerUUID", ownerUUID);
		}
	}
	
}
