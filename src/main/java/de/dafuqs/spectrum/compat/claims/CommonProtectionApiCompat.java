package de.dafuqs.spectrum.compat.claims;

import eu.pb4.common.protection.api.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class CommonProtectionApiCompat {
	
	public static boolean isProtected(World world, BlockPos pos, Entity cause) {
		if (cause instanceof PlayerEntity playerEntity) {
			return CommonProtection.canBreakBlock(world, pos, playerEntity.getGameProfile(), playerEntity);
		} else {
			return CommonProtection.canBreakBlock(world, pos, ProtectionProvider.UNKNOWN, null);
		}
	}
	
	public static boolean canInteract(World world, Entity entityToInteractWith, Entity cause) {
		if (cause instanceof PlayerEntity playerEntity) {
			return CommonProtection.canInteractEntity(world, entityToInteractWith, playerEntity.getGameProfile(), playerEntity);
		} else {
			return CommonProtection.canInteractEntity(world, entityToInteractWith, ProtectionProvider.UNKNOWN, null);
		}
	}
	
}
