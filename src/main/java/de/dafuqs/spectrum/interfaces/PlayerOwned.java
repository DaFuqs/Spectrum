package de.dafuqs.spectrum.interfaces;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.UUID;

public interface PlayerOwned {
	
	static PlayerEntity getPlayerEntityIfOnline(World world, UUID ownerUUID) {
		if (ownerUUID != null) {
			return world.getPlayerByUuid(ownerUUID);
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
	
	default PlayerEntity getPlayerEntityIfOnline(World world) {
		if (this.getOwnerUUID() != null) {
			return world.getPlayerByUuid(this.getOwnerUUID());
		}
		return null;
	}
	
}
