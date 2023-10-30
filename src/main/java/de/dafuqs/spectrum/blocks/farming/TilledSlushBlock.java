package de.dafuqs.spectrum.blocks.farming;

import net.minecraft.block.*;

public class TilledSlushBlock extends ExtraTickFarmlandBlock {
	
	public TilledSlushBlock(Settings settings, BlockState bareState) {
		super(settings, bareState);
		this.setDefaultState(getDefaultState().with(MOISTURE, 7));
	}
	
}
