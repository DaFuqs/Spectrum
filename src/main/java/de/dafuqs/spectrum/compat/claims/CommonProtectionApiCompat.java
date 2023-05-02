package de.dafuqs.spectrum.compat.claims;

import eu.pb4.common.protection.api.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class CommonProtectionApiCompat {
	
	public static boolean isProtected(World world, BlockPos pos, Entity entity) {
		if (entity instanceof PlayerEntity playerEntity) {
			return CommonProtection.canBreakBlock(world, pos, playerEntity.getGameProfile(), playerEntity);
		} else {
			return CommonProtection.canBreakBlock(world, pos, ProtectionProvider.UNKNOWN, null);
		}
	}
	
}
