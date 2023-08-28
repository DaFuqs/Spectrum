package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.blocks.decoration.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class PreservationBlockDetectorBlock extends SpectrumFacingBlock implements BlockEntityProvider, OperatorBlock {
	
	public PreservationBlockDetectorBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.SOUTH));
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(FACING) == direction && world.getBlockEntity(pos) instanceof PreservationBlockDetectorBlockEntity blockEntity) {
			blockEntity.triggerForNeighbor(neighborState);
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite().getOpposite());
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PreservationBlockDetectorBlockEntity(pos, state);
	}
	
}
