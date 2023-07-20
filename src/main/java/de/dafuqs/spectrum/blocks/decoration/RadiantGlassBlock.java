package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class RadiantGlassBlock extends GlassBlock {
	
	public RadiantGlassBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		if (stateFrom.isOf(this) || stateFrom.isOf(SpectrumBlocks.RADIANT_SEMI_PERMEABLE_GLASS)) {
			return true;
		}
		return super.isSideInvisible(state, stateFrom, direction);
	}
	
	@Override
	public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}
	
}
