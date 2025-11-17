package de.dafuqs.spectrum.blocks.deeper_down;

import net.minecraft.core.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;

public class RotatedPillarSplinterspawnInfestedBlock extends SplinterspawnInfestedBlock {
	
	public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
	
	public RotatedPillarSplinterspawnInfestedBlock(Block hostBlock, Properties properties) {
		super(hostBlock, properties);
	}
	
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return rotatePillar(state, rotation);
	}
	
	public static BlockState rotatePillar(BlockState state, Rotation rotation) {
		switch (rotation) {
			case COUNTERCLOCKWISE_90:
			case CLOCKWISE_90:
				switch (state.getValue(AXIS)) {
					case X -> {
						return state.setValue(AXIS, Direction.Axis.Z);
					}
					case Z -> {
						return state.setValue(AXIS, Direction.Axis.X);
					}
					default -> {
						return state;
					}
				}
			default:
				return state;
		}
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AXIS);
	}
	
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());
	}
	
}
