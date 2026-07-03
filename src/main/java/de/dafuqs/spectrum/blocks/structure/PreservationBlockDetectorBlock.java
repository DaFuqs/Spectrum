package de.dafuqs.spectrum.blocks.structure;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.decoration.*;
import net.minecraft.core.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jspecify.annotations.Nullable;

public class PreservationBlockDetectorBlock extends SpectrumFacingBlock implements EntityBlock, GameMasterBlock {
	
	public static final MapCodec<PreservationBlockDetectorBlock> CODEC = simpleCodec(PreservationBlockDetectorBlock::new);
	
	public PreservationBlockDetectorBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH));
	}
	
	@Override
	public MapCodec<? extends PreservationBlockDetectorBlock> codec() {
		return CODEC;
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (state.getValue(FACING) == direction && world.getBlockEntity(pos) instanceof PreservationBlockDetectorBlockEntity blockEntity) {
			blockEntity.triggerForNeighbor(neighborState);
		}
		return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getNearestLookingDirection().getOpposite().getOpposite());
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PreservationBlockDetectorBlockEntity(pos, state);
	}
	
}
