package de.dafuqs.spectrum.blocks.decoration;

import net.minecraft.block.*;
import net.minecraft.item.*;

public class SpectrumLineFacingBlock extends SpectrumFacingBlock {
	
	public SpectrumLineFacingBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
	}
	
}
