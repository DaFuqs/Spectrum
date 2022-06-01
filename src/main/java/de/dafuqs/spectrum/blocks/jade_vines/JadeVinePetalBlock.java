package de.dafuqs.spectrum.blocks.jade_vines;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class JadeVinePetalBlock extends Block {
	
	public JadeVinePetalBlock(Settings settings) {
		super(settings);
	}
	
	// makes blocks like torches being unable to be placed against it
	public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
		return VoxelShapes.empty();
	}
	
	public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
		return 1;
	}
	
}
