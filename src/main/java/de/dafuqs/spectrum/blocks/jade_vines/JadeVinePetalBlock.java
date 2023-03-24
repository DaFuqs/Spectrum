package de.dafuqs.spectrum.blocks.jade_vines;

import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

public class JadeVinePetalBlock extends Block {
	
	public JadeVinePetalBlock(Settings settings) {
		super(settings);
	}
	
	// makes blocks like torches being unable to be placed against it
	@Override
	public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
		return VoxelShapes.empty();
	}
	
	@Override
	public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
		return 1;
	}
	
}
