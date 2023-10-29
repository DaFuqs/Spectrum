package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;

// TODO - review
public class BlockDetectorBlock extends RedstoneInteractionBlock {
	
	public BlockDetectorBlock(AbstractBlock.Settings settings) {
		super(settings);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (direction != Direction.DOWN
				&& state.get(ORIENTATION).getFacing() == direction
				&& !state.get(TRIGGERED)
				&& neighborState.equals(getTargetBlockState(world, state, pos))) {

			this.scheduleTick(world, pos);
		}

		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
	protected BlockState getTargetBlockState(WorldAccess world, BlockState state, BlockPos pos) {
		if (state.get(ORIENTATION).getFacing() == Direction.DOWN) {
			return world.getBlockState(pos.offset(Direction.UP));
		} else {
			return world.getBlockState(pos.offset(Direction.DOWN));
		}
	}
	
	private void scheduleTick(WorldAccess world, BlockPos pos) {
		if (!world.isClient() && !world.getBlockTickScheduler().isQueued(pos, this)) {
			world.scheduleBlockTick(pos, this, 2);
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(TRIGGERED)) {
			world.setBlockState(pos, state.with(TRIGGERED, false), Block.NOTIFY_LISTENERS);
		} else {
			world.setBlockState(pos, state.with(TRIGGERED, true), Block.NOTIFY_LISTENERS);
			world.createAndScheduleBlockTick(pos, this, 2);
		}

		this.updateNeighbors(world, pos, state);
	}

	protected void updateNeighbors(World world, BlockPos pos, BlockState state) {
		Direction direction = state.get(ORIENTATION).getFacing();
		BlockPos blockPos = pos.offset(direction.getOpposite());
		world.updateNeighbor(blockPos, this, pos);
		world.updateNeighborsExcept(blockPos, this, direction);
	}
	
	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}
	
	@Override
	public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.getWeakRedstonePower(world, pos, direction);
	}
	
	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.get(TRIGGERED) && state.get(ORIENTATION).getFacing() == direction ? 15 : 0;
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!state.isOf(oldState.getBlock())) {
			if (!world.isClient() && state.get(TRIGGERED) && !world.getBlockTickScheduler().isQueued(pos, this)) {
				BlockState blockState = state.with(TRIGGERED, false);
				world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
				this.updateNeighbors(world, pos, blockState);
			}
			
		}
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			if (!world.isClient && state.get(TRIGGERED) && world.getBlockTickScheduler().isQueued(pos, this)) {
				this.updateNeighbors(world, pos, state.with(TRIGGERED, false));
			}
		}
	}
	
}
