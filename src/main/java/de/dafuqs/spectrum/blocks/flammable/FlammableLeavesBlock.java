package de.dafuqs.spectrum.blocks.flammable;

import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import javax.annotation.*;

public class FlammableLeavesBlock extends LeavesBlock {
	
	public FlammableLeavesBlock(Properties properties) {
		super(properties.ignitedByLava());
	}
	
	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return 30;
	}
	
	@Override
	public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return 60;
	}
	
	
}
