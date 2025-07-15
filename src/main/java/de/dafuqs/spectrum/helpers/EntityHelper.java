package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;

import java.util.*;

public class EntityHelper {
	
	public static void addPlayerTrust(Entity entity, Player player) {
		addPlayerTrust(entity, player.getUUID());
	}
	
	public static void addPlayerTrust(Entity entity, UUID playerUUID) {
		if (entity instanceof AbstractHorse horseBaseEntity) {
			if (horseBaseEntity.getOwnerUUID() == null) {
				horseBaseEntity.setOwnerUUID(playerUUID);
			}
		} else if (entity instanceof Fox foxEntity) {
			((FoxEntityAccessor) foxEntity).invokeAddTrustedUuid(playerUUID);
		}
	}
	
	public static boolean isRealPlayer(Entity entity) {
		// this should filter out most fake players (kibe, FAPI)
		return entity instanceof Player && entity.getClass().getCanonicalName().startsWith("net.minecraft");
	}
	
	public static boolean isRealPlayerProjectileOrPet(Entity entity) {
		if (entity instanceof TamableAnimal tameableEntity) {
			Entity owner = tameableEntity.getOwner();
			return isRealPlayer(owner);
		}
		if (entity instanceof Projectile projectileEntity) {
			Entity owner = projectileEntity.getOwner();
			return isRealPlayer(owner);
		}
		return isRealPlayer(entity);
	}
}