package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.server.world.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;

public class BlockDetectorBlock extends FacingBlock {
	
	public static final BooleanProperty POWERED = Properties.POWERED;
	
	public BlockDetectorBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.SOUTH).with(POWERED, false));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, POWERED);
	}
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(POWERED)) {
			world.setBlockState(pos, state.with(POWERED, false), Block.NOTIFY_LISTENERS);
		} else {
			world.setBlockState(pos, state.with(POWERED, true), Block.NOTIFY_LISTENERS);
			world.scheduleBlockTick(pos, this, 2);
		}
		
		this.updateNeighbors(world, pos, state);
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (direction != Direction.DOWN && state.get(FACING) == direction && !state.get(POWERED) && neighborState.equals(getTargetBlockState(world, state, pos))) {
			this.scheduleTick(world, pos);
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
	protected BlockState getTargetBlockState(WorldAccess world, BlockState state, BlockPos pos) {
		if (state.get(FACING) == Direction.DOWN) {
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
	
	protected void updateNeighbors(World world, BlockPos pos, BlockState state) {
		Direction direction = state.get(FACING);
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
		return state.get(POWERED) && state.get(FACING) == direction ? 15 : 0;
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!state.isOf(oldState.getBlock())) {
			if (!world.isClient() && state.get(POWERED) && !world.getBlockTickScheduler().isQueued(pos, this)) {
				BlockState blockState = state.with(POWERED, false);
				world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
				this.updateNeighbors(world, pos, blockState);
			}
			
		}
	}
	
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			if (!world.isClient && state.get(POWERED) && world.getBlockTickScheduler().isQueued(pos, this)) {
				this.updateNeighbors(world, pos, state.with(POWERED, false));
			}
		}
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite().getOpposite());
	}
	
}
