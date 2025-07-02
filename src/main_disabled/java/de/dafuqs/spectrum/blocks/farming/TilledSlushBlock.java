package de.dafuqs.spectrum.blocks.farming;

import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;

public class TilledSlushBlock extends ExtraTickFarmlandBlock {
	
	public TilledSlushBlock(Properties settings, BlockState bareState) {
		super(settings, bareState);
		this.registerDefaultState(defaultBlockState().setValue(MOISTURE, 7));
	}

//	@Override
//	public MapCodec<? extends > getCodec() {
//		//TODO: Make the codec
//		return CODEC;
//	}
	
	@Override
	protected boolean isNearWater(LevelReader world, BlockPos pos) {
		return true;
	}
	
}
