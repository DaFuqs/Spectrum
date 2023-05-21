package de.dafuqs.spectrum.blocks.furniture;

import net.minecraft.block.*;
import net.minecraft.block.piston.*;
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
	public static final BooleanProperty ALT = BooleanProperty.of("alt");
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final VoxelShape SHAPE_STANDING, SHAPE_STANDING_ALT, SHAPE_HANGING, SHAPE_HANGING_ALT;
	
	public FlexLanternBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(HANGING, false).with(ALT, false).with(WATERLOGGED, false));
	}
	
	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
		var player = ctx.getPlayer();
		var state = super.getPlacementState(ctx);
		
		if (state != null) {
			if (player != null) {
				state = state.with(ALT, player.isSneaking());
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
		var alt = state.get(ALT);
		
		if (state.get(HANGING)) {
			return alt ? SHAPE_HANGING_ALT : SHAPE_HANGING;
		} else {
			return alt ? SHAPE_STANDING_ALT : SHAPE_STANDING;
		}
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = state.get(HANGING) ? Direction.UP : Direction.DOWN;
		return Block.sideCoversSmallSquare(world, pos.offset(direction), direction.getOpposite());
	}
	
	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(HANGING, ALT, WATERLOGGED);
	}
	
	static {
		SHAPE_STANDING = Block.createCuboidShape(4, 0, 4, 12, 13, 12);
		SHAPE_STANDING_ALT = Block.createCuboidShape(4, 0, 4, 12, 16, 12);
		SHAPE_HANGING = Block.createCuboidShape(4, 4, 4, 12, 16, 12);
		SHAPE_HANGING_ALT = Block.createCuboidShape(4, 7, 4, 12, 16, 12);
	}
}
