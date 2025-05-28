package de.dafuqs.spectrum.blocks.decoration;

import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.shapes.*;

public class WeepingGalaLanternBlock extends FlexLanternBlock {
	
	public static final VoxelShape SHAPE_STANDING_SMALL = Block.box(4, 0, 4, 12, 11, 12);
	public static final VoxelShape SHAPE_STANDING_TALL = Block.box(4, 0, 4, 12, 14, 12);
	public static final VoxelShape SHAPE_HANGING_SMALL = Block.box(4, 5, 4, 12, 16, 12);
	public static final VoxelShape SHAPE_HANGING_TALL = Block.box(4, 2, 4, 12, 16, 12);
	
	public WeepingGalaLanternBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		var tall = state.getValue(TALL);
		
		if (state.getValue(HANGING)) {
			return tall ? SHAPE_HANGING_TALL : SHAPE_HANGING_SMALL;
		} else {
			return tall ? SHAPE_STANDING_TALL : SHAPE_STANDING_SMALL;
		}
	}
	
}
