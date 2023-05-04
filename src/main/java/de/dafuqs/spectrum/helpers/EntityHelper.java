package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;

import java.util.*;

public class EntityHelper {
	
	public static void addPlayerTrust(Entity entity, PlayerEntity player) {
		addPlayerTrust(entity, player.getUuid());
	}
	
	public static void addPlayerTrust(Entity entity, UUID playerUUID) {
		if (entity instanceof AbstractHorseEntity horseBaseEntity) {
			if (horseBaseEntity.getOwnerUuid() == null) {
				horseBaseEntity.setOwnerUuid(playerUUID);
			}
		} else if (entity instanceof FoxEntity foxEntity) {
			((FoxEntityAccessor) foxEntity).invokeAddTrustedUuid(playerUUID);
		}
	}
	
	public static boolean isRealPlayer(Entity entity) {
		// this should filter out most fake players (kibe, FAPI)
		return entity instanceof PlayerEntity && entity.getClass().getCanonicalName().startsWith("net.minecraft");
	}
	
	public static boolean isRealPlayerOrPet(Entity entity) {
		if (entity instanceof TameableEntity tameableEntity) {
			Entity owner = tameableEntity.getOwner();
			return isRealPlayer(owner);
		}
		return isRealPlayer(entity);
	}
}