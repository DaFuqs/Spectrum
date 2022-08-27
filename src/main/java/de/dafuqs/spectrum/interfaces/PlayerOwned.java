package de.dafuqs.spectrum.interfaces;

import io.wispforest.owo.Owo;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface PlayerOwned {
	
	static PlayerEntity getPlayerEntityIfOnline(UUID ownerUUID) {
		if (ownerUUID == null || Owo.currentServer() == null) {
			return null;
		}
		return Owo.currentServer().getPlayerManager().getPlayer(ownerUUID);
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
