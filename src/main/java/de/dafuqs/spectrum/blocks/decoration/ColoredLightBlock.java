package de.dafuqs.spectrum.blocks.decoration;

import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class ColoredLightBlock extends RedstoneLampBlock {
	
	public ColoredLightBlock(Settings settings) {
		super(settings);
	}
	
	/**
	 * Disable culling for this block
	 * => the translucent outlines will be rendered
	 * even if the side is obstructed by a block
	 * (disabling culling is not nice for performance,
	 * but usually most sides will be visible either way)
	 */
	@Override
	public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
		return VoxelShapes.empty();
	}
	
}
