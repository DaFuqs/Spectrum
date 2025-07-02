package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class ShinglesBlock extends HorizontalDirectionalBlock {
	
	public static final MapCodec<ShinglesBlock> CODEC = simpleCodec(ShinglesBlock::new);
	
	public ShinglesBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public MapCodec<? extends ShinglesBlock> codec() {
		return CODEC;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
}
