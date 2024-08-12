package de.dafuqs.spectrum.blocks.decoration;

import net.minecraft.block.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class FlexLanternBlock extends DiagonalBlock implements Waterloggable {
	
	public static final BooleanProperty HANGING = Properties.HANGING;
	public static final BooleanProperty TALL = BooleanProperty.of("tall");
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final VoxelShape SHAPE_STANDING_SMALL, SHAPE_STANDING_TALL, SHAPE_HANGING_SMALL, SHAPE_HANGING_TALL;
	
	public FlexLanternBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(HANGING, false).with(TALL, true).with(WATERLOGGED, false));
	}
	
	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
		var player = ctx.getPlayer();
		var state = super.getPlacementState(ctx);
		
		if (state != null) {
			if (player != null && player.isSneaking()) {
				state = state.with(TALL, false);
			}
			if (ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER) {
				state = state.with(WATERLOGGED, true);
			}
			
			state = state.with(HANGING, ctx.getSide() == Direction.DOWN);
		}
		
		return state;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		var tall = state.get(TALL);
		
		if (state.get(HANGING)) {
			return tall ? SHAPE_HANGING_TALL : SHAPE_HANGING_SMALL;
		} else {
			return tall ? SHAPE_STANDING_TALL : SHAPE_STANDING_SMALL;
		}
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = state.get(HANGING) ? Direction.UP : Direction.DOWN;
		return Block.sideCoversSmallSquare(world, pos.offset(direction), direction.getOpposite());
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(HANGING, TALL, WATERLOGGED);
	}
	
	static {
		SHAPE_STANDING_SMALL = Block.createCuboidShape(4, 0, 4, 12, 13, 12);
		SHAPE_STANDING_TALL = Block.createCuboidShape(4, 0, 4, 12, 16, 12);
		SHAPE_HANGING_SMALL = Block.createCuboidShape(4, 7, 4, 12, 16, 12);
		SHAPE_HANGING_TALL = Block.createCuboidShape(4, 4, 4, 12, 16, 12);
	}
}
