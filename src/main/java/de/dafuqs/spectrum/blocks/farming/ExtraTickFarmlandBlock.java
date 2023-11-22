package de.dafuqs.spectrum.blocks.farming;

import net.minecraft.block.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;

public class ExtraTickFarmlandBlock extends SpectrumFarmlandBlock {
	
	public ExtraTickFarmlandBlock(Settings settings, BlockState bareState) {
		super(settings, bareState);
	}
	
	@Override
	public boolean hasRandomTicks(BlockState state) {
		return true;
	}
	
	/**
	 * If there is a crop block on top of this block: tick it, too
	 * => the crop grows faster
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		BlockPos topPos = pos.up();
		BlockState topBlockState = world.getBlockState(topPos);
		if (hasCrop(world, pos)) {
			topBlockState.getBlock().randomTick(topBlockState, world, topPos, random);
		}
		
		super.randomTick(state, world, pos, random);
	}
	
}
