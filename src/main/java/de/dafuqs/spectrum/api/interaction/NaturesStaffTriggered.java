package de.dafuqs.spectrum.api.interaction;

import net.minecraft.core.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;

/**
 * Blocks that have an effect when a Nature's Staff is used on them
 */
public interface NaturesStaffTriggered {
	/**
	 * @return if the staff can be used on the state
	 */
	boolean canUseNaturesStaff(Level world, BlockPos pos, BlockState state);
	
	/**
	 * @return if effects should play on that pos
	 */
	boolean onNaturesStaffUse(Level world, BlockPos pos, BlockState state, Player player);
}
