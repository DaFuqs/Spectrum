package de.dafuqs.spectrum.blocks.structure;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import javax.annotation.*;

public class DeepLightBlock extends HorizontalDirectionalBlock implements EntityBlock {
	
	public static final MapCodec<DeepLightBlock> CODEC = simpleCodec(DeepLightBlock::new);
	
	public DeepLightBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends DeepLightBlock> codec() {
		return CODEC;
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DeepLightBlockEntity(pos, state);
	}
}
