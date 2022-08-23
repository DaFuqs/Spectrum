package de.dafuqs.spectrum.interfaces;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface PlayerOwned {
	
	static PlayerEntity getPlayerEntityIfOnline(UUID ownerUUID) {
		if (ownerUUID != null || SpectrumCommon.minecraftServer == null) {
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
	
}
