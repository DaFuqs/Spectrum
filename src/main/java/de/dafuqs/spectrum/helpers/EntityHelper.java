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
	
}