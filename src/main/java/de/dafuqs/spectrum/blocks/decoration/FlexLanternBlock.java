package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;

public class FlexLanternBlock extends DiagonalBlock implements SimpleWaterloggedBlock {
	
	public static final MapCodec<FlexLanternBlock> CODEC = simpleCodec(FlexLanternBlock::new);
	
	public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
	public static final BooleanProperty TALL = BooleanProperty.create("tall");
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final VoxelShape SHAPE_STANDING_SMALL, SHAPE_STANDING_TALL, SHAPE_HANGING_SMALL, SHAPE_HANGING_TALL;
	
	public FlexLanternBlock(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState().setValue(HANGING, false).setValue(TALL, true).setValue(WATERLOGGED, false));
	}
	
	@Override
	public MapCodec<? extends FlexLanternBlock> codec() {
		return CODEC;
	}
	
	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
		var player = ctx.getPlayer();
		var state = super.getStateForPlacement(ctx);
		
		if (state != null) {
			if (player != null && player.isShiftKeyDown()) {
				state = state.setValue(TALL, false);
			}
			if (ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER) {
				state = state.setValue(WATERLOGGED, true);
			}
			
			state = state.setValue(HANGING, ctx.getClickedFace() == Direction.DOWN);
		}
		
		return state;
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
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		Direction direction = state.getValue(HANGING) ? Direction.UP : Direction.DOWN;
		return Block.canSupportCenter(world, pos.relative(direction), direction.getOpposite());
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(HANGING, TALL, WATERLOGGED);
	}
	
	static {
		SHAPE_STANDING_SMALL = Block.box(4, 0, 4, 12, 13, 12);
		SHAPE_STANDING_TALL = Block.box(4, 0, 4, 12, 16, 12);
		SHAPE_HANGING_SMALL = Block.box(4, 7, 4, 12, 16, 12);
		SHAPE_HANGING_TALL = Block.box(4, 4, 4, 12, 16, 12);
	}
}
