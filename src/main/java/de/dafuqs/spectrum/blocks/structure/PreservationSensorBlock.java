package de.dafuqs.spectrum.blocks.structure;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.redstone.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;

public class PreservationSensorBlock extends BaseEntityBlock {
	
	public static final MapCodec<PreservationSensorBlock> CODEC = simpleCodec(PreservationSensorBlock::new);
	
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	
	public PreservationSensorBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, false));
	}
	
	@Override
	public MapCodec<? extends PreservationSensorBlock> codec() {
		return CODEC;
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PreservationSensorBlockEntity(pos, state);
	}
	
	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		var isPowered = world.hasNeighborSignal(pos) || world.hasNeighborSignal(pos.above());
		boolean wasPowered = state.getValue(POWERED);
		
		if (isPowered && !wasPowered) {
			if (!world.isClientSide()) {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity instanceof PreservationSensorBlockEntity sensorBlockEntity) {
					sensorBlockEntity.activateController();
				}
			}
			world.setBlock(pos, state.setValue(POWERED, true), Block.UPDATE_CLIENTS);
		} else if (!isPowered && wasPowered) {
			world.setBlock(pos, state.setValue(POWERED, false), Block.UPDATE_CLIENTS);
		}
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
		builder.add(FACING, POWERED);
	}
}
