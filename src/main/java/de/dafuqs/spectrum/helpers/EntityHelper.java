package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.mixin.accessors.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.neoforged.neoforge.common.util.*;

import javax.annotation.*;
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
			((FoxEntityAccessor) foxEntity).invokeAddTrustedUUID(playerUUID);
		}
	}
	
	public static boolean isRealPlayer(@Nullable Entity entity) {
		return !(entity instanceof FakePlayer);
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