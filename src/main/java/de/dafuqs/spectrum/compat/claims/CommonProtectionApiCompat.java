package de.dafuqs.spectrum.compat.claims;

import eu.pb4.common.protection.api.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;

public class CommonProtectionApiCompat {
	
	static boolean canBreak(Level world, BlockPos pos, Entity cause) {
		if (cause instanceof Player playerEntity) {
			return CommonProtection.canBreakBlock(world, pos, playerEntity.getGameProfile(), playerEntity);
		} else {
			return CommonProtection.canBreakBlock(world, pos, ProtectionProvider.UNKNOWN, null);
		}
	}
	
	static boolean canInteract(Level world, Entity entityToInteractWith, Entity cause) {
		if (cause instanceof Player playerEntity) {
			return CommonProtection.canInteractEntity(world, entityToInteractWith, playerEntity.getGameProfile(), playerEntity);
		} else {
			return CommonProtection.canInteractEntity(world, entityToInteractWith, ProtectionProvider.UNKNOWN, null);
		}
	}
	
	static boolean canInteract(Level world, BlockPos pos, Entity cause) {
		if (cause instanceof Player playerEntity) {
			return CommonProtection.canInteractBlock(world, pos, playerEntity.getGameProfile(), playerEntity);
		} else {
			return CommonProtection.canInteractBlock(world, pos, ProtectionProvider.UNKNOWN, null);
		}
	}
	
	static boolean canModify(Level world, BlockPos pos, Entity cause) {
		if (cause instanceof Player playerEntity) {
			return CommonProtection.canPlaceBlock(world, pos, playerEntity.getGameProfile(), playerEntity) &&
					CommonProtection.canBreakBlock(world, pos, playerEntity.getGameProfile(), playerEntity);
		} else {
			return CommonProtection.canPlaceBlock(world, pos, ProtectionProvider.UNKNOWN, null) &&
					CommonProtection.canBreakBlock(world, pos, ProtectionProvider.UNKNOWN, null);
		}
	}
	
	static boolean canPlaceBlock(Level world, BlockPos pos, Entity cause) {
		if (cause instanceof Player playerEntity) {
			return CommonProtection.canPlaceBlock(world, pos, playerEntity.getGameProfile(), playerEntity);
		} else {
			return CommonProtection.canPlaceBlock(world, pos, ProtectionProvider.UNKNOWN, null);
		}
	}
	
}
