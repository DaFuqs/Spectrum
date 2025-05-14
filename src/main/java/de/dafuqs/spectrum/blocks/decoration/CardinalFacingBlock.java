package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;

public class CardinalFacingBlock extends Block {
	
	public static final MapCodec<CardinalFacingBlock> CODEC = simpleCodec(CardinalFacingBlock::new);
	
	public static final BooleanProperty CARDINAL_FACING = BooleanProperty.create("cardinal_facing");
	
	public CardinalFacingBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(CARDINAL_FACING, false));
	}

	@Override
	public MapCodec<? extends CardinalFacingBlock> codec() {
		return CODEC;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		Direction facing = ctx.getHorizontalDirection();
		boolean facingVertical = facing.equals(Direction.EAST) || facing.equals(Direction.WEST);
		return this.defaultBlockState().setValue(CARDINAL_FACING, facingVertical);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(CARDINAL_FACING);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		boolean cardinal = state.getValue(CARDINAL_FACING);
		return state.setValue(CARDINAL_FACING, (rotation.ordinal() % 2 == 1) != cardinal);
	}
}
