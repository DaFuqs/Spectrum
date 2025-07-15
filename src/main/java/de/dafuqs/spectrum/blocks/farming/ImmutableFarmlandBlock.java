package de.dafuqs.spectrum.blocks.farming;

import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.block.state.*;

public class ImmutableFarmlandBlock extends SpectrumFarmlandBlock {
	
	public ImmutableFarmlandBlock(Properties settings, BlockState bareState) {
		super(settings, bareState);
	}

//	@Override
//	public MapCodec<? extends ImmutableFarmlandBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
	}
	
	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return false;
	}
	
}
