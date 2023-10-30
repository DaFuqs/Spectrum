package de.dafuqs.spectrum.blocks.farming;

import net.minecraft.block.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;

public class ImmutableFarmlandBlock extends SpectrumFarmlandBlock {
	
	public ImmutableFarmlandBlock(Settings settings, BlockState bareState) {
		super(settings, bareState);
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
	}
	
	@Override
	public boolean hasRandomTicks(BlockState state) {
		return false;
	}
	
}
