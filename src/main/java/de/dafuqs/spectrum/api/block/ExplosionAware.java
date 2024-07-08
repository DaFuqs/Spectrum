package de.dafuqs.spectrum.api.block;

import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.explosion.*;

public interface ExplosionAware {

	/**
	 * Alternate Method of the vanilla onDestroyedByExplosion() logic
	 * called before the block is set to air and therefore having state information
	 * and still intact block entity.
	 */
	default void beforeDestroyedByExplosion(World world, BlockPos pos, BlockState state, Explosion explosion) {

	}

	/**
	 * The block to place when the block is exploded.
	 * Unless you want to use a custom block, return Blocks.AIR.getDefaultState() (vanilla default)
	 * @return the state to replace the block with
	 */
	default BlockState getStateForExplosion(World world, BlockPos blockPos, BlockState stateAtPos) {
		return Blocks.AIR.getDefaultState();
	}

}
