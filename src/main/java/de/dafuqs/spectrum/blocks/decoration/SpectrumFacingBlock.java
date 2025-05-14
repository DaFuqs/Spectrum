package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class SpectrumFacingBlock extends DirectionalBlock {
	
	public static final MapCodec<SpectrumFacingBlock> CODEC = simpleCodec(SpectrumFacingBlock::new);
	
	public SpectrumFacingBlock(Properties settings) {
		super(settings);
		this.registerDefaultState((this.stateDefinition.any()).setValue(DirectionalBlock.FACING, Direction.UP));
	}

	@Override
	public MapCodec<? extends SpectrumFacingBlock> codec() {
		return CODEC;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		Direction direction = ctx.getClickedFace();
		BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(direction.getOpposite()));
		return blockState.is(this) && blockState.getValue(FACING) == direction ? this.defaultBlockState().setValue(FACING, direction.getOpposite()) : this.defaultBlockState().setValue(FACING, direction);
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
}
