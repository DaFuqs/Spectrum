package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class SpectrumDirectionalBlock extends DirectionalBlock {
	
	public static final MapCodec<SpectrumDirectionalBlock> CODEC = simpleCodec(SpectrumDirectionalBlock::new);
	
	public SpectrumDirectionalBlock(Properties properties) {
		super(properties);
		registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
	}
	
	@Override
	public MapCodec<? extends SpectrumDirectionalBlock> codec() {
		return CODEC;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getClickedFace());
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
