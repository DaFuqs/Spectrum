package de.dafuqs.spectrum.compat.claims;

import net.fabricmc.loader.api.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class GenericClaimModsCompat {
	
	public static boolean IS_COMMON_PROTECTION_API_PRESENT = FabricLoader.getInstance().isModLoaded("common-protection-api");
	
	/**
	 * Call this for all kinds of world modifications
	 * For each supported protection mod add a single check here
	 * instead of spreading individual protection mods over the whole codebase
	 * <p>
	 * This also means we do not need any kind of hard compat
	 *
	 * @param world the world that should get modified
	 * @param pos   the pos that should get modified
	 * @return if modification is allowed
	 */
	public static boolean isProtected(World world, BlockPos pos, @Nullable Entity entity) {
		if (IS_COMMON_PROTECTION_API_PRESENT) {
			return !CommonProtectionApiCompat.isProtected(world, pos, entity);
		}
		return true;
	}
	
}
