package de.dafuqs.spectrum.blocks.redstone;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;

public class RedstoneInteractionBlock extends Block {
	
	public static final MapCodec<RedstoneInteractionBlock> CODEC = simpleCodec(RedstoneInteractionBlock::new);
	
	public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
	public static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;
	
	public RedstoneInteractionBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(ORIENTATION, FrontAndTop.EAST_UP).setValue(TRIGGERED, false));
	}

	@Override
	public MapCodec<? extends RedstoneInteractionBlock> codec() {
		return CODEC;
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(ORIENTATION, rotation.rotation().rotate(state.getValue(ORIENTATION)));
	}
	
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.setValue(ORIENTATION, mirror.rotation().rotate(state.getValue(ORIENTATION)));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ORIENTATION, TRIGGERED);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		Direction direction = ctx.getNearestLookingDirection().getOpposite();
		Direction direction2 = switch (direction) {
			case DOWN -> ctx.getHorizontalDirection().getOpposite();
			case UP -> ctx.getHorizontalDirection();
			case NORTH, SOUTH, WEST, EAST -> Direction.UP;
		};
		
		return this.defaultBlockState()
				.setValue(ORIENTATION, FrontAndTop.fromFrontAndTop(direction, direction2))
				.setValue(TRIGGERED, ctx.getLevel().hasNeighborSignal(ctx.getClickedPos()));
	}
	
}
