package de.dafuqs.spectrum.helpers;

import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.jspecify.annotations.*;

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
	
	public static boolean isRealPlayer(@Nullable Entity entity) {
		// this should filter out most fake players (kibe, FAPI)
		return entity instanceof Player && entity.getClass().getCanonicalName().startsWith("net.minecraft");
	}
	
	public static boolean isRealPlayerProjectileOrPet(@Nullable Entity entity) {
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
	
	public static void applyGravity(Entity entity, double gravityMod, Level world) {
		// don't affect creative/spectators/... players or immune boss mobs
		if (gravityMod == 0.
				|| !entity.isPushable() || entity.isNoGravity() || entity.isSpectator()
				|| entity instanceof Player player && player.getAbilities().flying) return;
		entity.push(0, gravityMod, 0);
		
		// if falling very slowly => reset fall distance / damage
		if (gravityMod > 0 && entity.getDeltaMovement().y > -0.4)
			entity.fallDistance = 0;
		
		if (world.getGameTime() % 20 == 0 && entity instanceof ServerPlayerEntityAccessor serverPlayerEntity)
			serverPlayerEntity.processAppliedGravityForAdvancements(gravityMod);
		
		return;
	}
	
}