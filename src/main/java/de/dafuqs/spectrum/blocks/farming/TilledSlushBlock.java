package de.dafuqs.spectrum.blocks.farming;

import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class TilledSlushBlock extends ExtraTickFarmlandBlock {
	
	public TilledSlushBlock(Settings settings, BlockState bareState) {
		super(settings, bareState);
		this.setDefaultState(getDefaultState().with(MOISTURE, 7));
	}
	
	@Override
	protected boolean isWaterNearby(WorldView world, BlockPos pos) {
		return true;
	}
	
}
