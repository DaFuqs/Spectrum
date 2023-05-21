package de.dafuqs.spectrum.blocks.energy;

import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.item.*;
import net.minecraft.state.*;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class InkwellBlock extends FacingBlock {
	
	public static final DirectionProperty FACING = Properties.FACING;
	
	protected static final VoxelShape SHAPE_UP = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D);
	protected static final VoxelShape SHAPE_DOWN = Block.createCuboidShape(4.0D, 8.0D, 4.0D, 12.0D, 16.0D, 12.0D);
	protected static final VoxelShape SHAPE_NORTH = Block.createCuboidShape(4.0D, 4.0D, 8.0D, 12.0D, 12.0D, 16.0D);
	protected static final VoxelShape SHAPE_SOUTH = Block.createCuboidShape(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 8.0D);
	protected static final VoxelShape SHAPE_EAST = Block.createCuboidShape(0.0D, 4.0D, 4.0D, 8.0D, 12.0D, 12.0D);
	protected static final VoxelShape SHAPE_WEST = Block.createCuboidShape(8.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D);
	
	public InkwellBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction direction = ctx.getSide();
		return this.getDefaultState().with(FACING, direction);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(Text.translatable("block.spectrum.inkwell.tooltip").formatted(Formatting.GRAY));
	}
	
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		super.neighborUpdate(state, world, pos, block, fromPos, notify);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		switch (state.get(FACING)) {
			case UP -> {
				return SHAPE_UP;
			}
			case DOWN -> {
				return SHAPE_DOWN;
			}
			case NORTH -> {
				return SHAPE_NORTH;
			}
			case EAST -> {
				return SHAPE_EAST;
			}
			case SOUTH -> {
				return SHAPE_SOUTH;
			}
			default -> {
				return SHAPE_WEST;
			}
		}
	}
	
}