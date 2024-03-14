package de.dafuqs.spectrum.api.interaction;

import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

/**
 * Blocks that have an effect when a Nature's Staff is used on them
 */
public interface NaturesStaffTriggered {
	/**
	 * @return if the staff can be used on the state
	 */
	boolean canUseNaturesStaff(World world, BlockPos pos, BlockState state);

	/**
	 * @return if effects should play on that pos
	 */
	boolean onNaturesStaffUse(World world, BlockPos pos, BlockState state, PlayerEntity player);
}
