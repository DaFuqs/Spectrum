package de.dafuqs.spectrum.blocks.farming;

import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.block.state.*;

public class ExtraTickFarmlandBlock extends SpectrumFarmlandBlock {
	
	public ExtraTickFarmlandBlock(Properties settings, BlockState bareState) {
		super(settings.randomTicks(), bareState);
	}

//	@Override
//	public MapCodec<? extends ExtraTickFarmlandBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}

	/**
	 * If there is a crop block on top of this block: tick it, too
	 * => the crop grows faster
	 */
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		BlockPos topPos = pos.above();
		BlockState topBlockState = world.getBlockState(topPos);
		if (shouldMaintainFarmland(world, pos)) {
			topBlockState.randomTick(world, topPos, random);
		}
		
		super.randomTick(state, world, pos, random);
	}
	
}
