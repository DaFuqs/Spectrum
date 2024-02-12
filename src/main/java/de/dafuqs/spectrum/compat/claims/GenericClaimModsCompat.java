package de.dafuqs.spectrum.compat.claims;

import net.fabricmc.loader.api.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class GenericClaimModsCompat {
	
	public static final boolean IS_COMMON_PROTECTION_API_PRESENT = FabricLoader.getInstance().isModLoaded("common-protection-api");
	
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
	public static boolean canBreak(World world, BlockPos pos, @Nullable Entity cause) {
		if (IS_COMMON_PROTECTION_API_PRESENT) {
			return CommonProtectionApiCompat.canBreak(world, pos, cause);
		}
		return true;
	}
	
	public static boolean canInteract(World world, Entity entity, @Nullable Entity cause) {
		if (IS_COMMON_PROTECTION_API_PRESENT) {
			return CommonProtectionApiCompat.canInteract(world, entity, cause);
		}
		return true;
	}

	public static boolean canInteract(World world, BlockPos pos, @Nullable Entity cause) {
		if (IS_COMMON_PROTECTION_API_PRESENT) {
			return CommonProtectionApiCompat.canInteract(world, pos, cause);
		}
		return true;
	}

	/**
	 * Used to determine whether you can break and place blocks in this area, which is useful
	 * for swapping blocks
	 *
	 * @param world the world that should get modified
	 * @param pos   the pos that should get modified
	 * @return if modification is allowed
	 */
	public static boolean canModify(World world, BlockPos pos, @Nullable Entity cause) {
		if (IS_COMMON_PROTECTION_API_PRESENT) {
			return CommonProtectionApiCompat.canModify(world, pos, cause);
		}
		return true;
	}

	public static boolean canPlaceBlock(World world, BlockPos pos, @Nullable Entity cause) {
		if (IS_COMMON_PROTECTION_API_PRESENT) {
			return CommonProtectionApiCompat.canPlaceBlock(world, pos, cause);
		}
		return true;
	}
	
}
