package de.dafuqs.spectrum.interfaces;

import io.wispforest.owo.Owo;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface PlayerOwned {
	
	static PlayerEntity getPlayerEntityIfOnline(UUID ownerUUID) {
		// TODO - If the UUID passed is null, and the server isn't there, will this not just crash?
		if (ownerUUID != null || Owo.currentServer() == null) {
			return Owo.currentServer().getPlayerManager().getPlayer(ownerUUID);
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
			return Owo.currentServer().getPlayerManager().getPlayer(ownerUUID);
		}
		return null;
	}
	
}
