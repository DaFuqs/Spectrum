package de.dafuqs.spectrum.blocks.flammable;

import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class FlammableSlabBlock extends SlabBlock {
	
	public FlammableSlabBlock(Properties properties) {
		super(properties.ignitedByLava());
	}
	
	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return 5;
	}
	
	@Override
	public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return 20;
	}
	
}
