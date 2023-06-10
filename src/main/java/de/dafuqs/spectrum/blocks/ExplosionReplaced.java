package de.dafuqs.spectrum.blocks;

import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public interface ExplosionReplaced {
	
	BlockState getStateForExplosion(World world, BlockPos blockPos, BlockState stateAtPos);
	
}
