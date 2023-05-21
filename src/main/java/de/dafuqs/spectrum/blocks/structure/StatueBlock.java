package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.blocks.decoration.*;
import net.minecraft.block.*;
import net.minecraft.block.enums.*;
import net.minecraft.item.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

public class StatueBlock extends DecoStoneBlock {
	
	public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	protected static final VoxelShape TOP_SHAPE = Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 11.0D, 13.0D);
	protected static final VoxelShape BOTTOM_SHAPE = Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
	
	public StatueBlock(Settings settings) {
		super(settings);
		this.setDefaultState((this.stateManager.getDefaultState()).with(HALF, DoubleBlockHalf.LOWER).with(FACING, Direction.NORTH));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HALF, FACING);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return state.get(HALF) == DoubleBlockHalf.LOWER ? BOTTOM_SHAPE : TOP_SHAPE;
	}
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}
	
}
