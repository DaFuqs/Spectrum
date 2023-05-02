package de.dafuqs.spectrum.compat.claims;

import com.mojang.authlib.*;
import eu.pb4.common.protection.api.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class CommonProtectionApiCompat {
	
	public static boolean isProtected(World world, BlockPos pos, PlayerEntity player) {
		GameProfile profile = player == null ? CommonProtection.UNKNOWN : player.getGameProfile();
		return CommonProtection.canBreakBlock(world, pos, profile, player);
	}
	
}
