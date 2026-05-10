package de.dafuqs.spectrum.blocks.redstone;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class BlockDetectorBlock extends RedstoneInteractionBlock {
	
	public static final MapCodec<BlockDetectorBlock> CODEC = simpleCodec(BlockDetectorBlock::new);
	
	public BlockDetectorBlock(BlockBehaviour.Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends BlockDetectorBlock> codec() {
		return CODEC;
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (direction != Direction.DOWN
				&& state.getValue(ORIENTATION).front() == direction
				&& !state.getValue(TRIGGERED)
				&& neighborState.equals(getTargetBlockState(world, state, pos))) {
			
			this.scheduleTick(world, pos);
		}
		
		return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}
	
	protected BlockState getTargetBlockState(LevelAccessor world, BlockState state, BlockPos pos) {
		if (state.getValue(ORIENTATION).front() == Direction.DOWN) {
			return world.getBlockState(pos.relative(Direction.UP));
		} else {
			return world.getBlockState(pos.relative(Direction.DOWN));
		}
	}
	
	private void scheduleTick(LevelAccessor world, BlockPos pos) {
		if (!world.isClientSide() && !world.getBlockTicks().hasScheduledTick(pos, this)) {
			world.scheduleTick(pos, this, 2);
		}
	}
	
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (state.getValue(TRIGGERED)) {
			world.setBlock(pos, state.setValue(TRIGGERED, false), Block.UPDATE_CLIENTS);
		} else {
			world.setBlock(pos, state.setValue(TRIGGERED, true), Block.UPDATE_CLIENTS);
			world.scheduleTick(pos, this, 2);
		}
		
		this.updateNeighbors(world, pos, state);
	}
	
	protected void updateNeighbors(Level world, BlockPos pos, BlockState state) {
		Direction direction = state.getValue(ORIENTATION).front();
		BlockPos blockPos = pos.relative(direction.getOpposite());
		world.neighborChanged(blockPos, this, pos);
		world.updateNeighborsAtExceptFromFacing(blockPos, this, direction);
	}
	
	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}
	
	@Override
	public int getDirectSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return state.getSignal(world, pos, direction);
	}
	
	@Override
	public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return state.getValue(TRIGGERED) && state.getValue(ORIENTATION).front() == direction ? 15 : 0;
	}
	
	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!state.is(oldState.getBlock())) {
			if (!world.isClientSide() && state.getValue(TRIGGERED) && !world.getBlockTicks().hasScheduledTick(pos, this)) {
				BlockState blockState = state.setValue(TRIGGERED, false);
				world.setBlock(pos, blockState, Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
				this.updateNeighbors(world, pos, blockState);
			}
			
		}
	}
	
	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.is(newState.getBlock())) {
			if (!world.isClientSide() && state.getValue(TRIGGERED) && world.getBlockTicks().hasScheduledTick(pos, this)) {
				this.updateNeighbors(world, pos, state.setValue(TRIGGERED, false));
			}
		}
	}
	
}
