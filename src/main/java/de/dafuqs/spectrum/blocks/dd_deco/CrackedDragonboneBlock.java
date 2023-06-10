package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.spectrum.blocks.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class CrackedDragonboneBlock extends PillarBlock implements ExplosionReplaced {
	
	public CrackedDragonboneBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public BlockState getStateForExplosion(World world, BlockPos blockPos, BlockState stateAtPos) {
		return stateAtPos;
	}
	
}
