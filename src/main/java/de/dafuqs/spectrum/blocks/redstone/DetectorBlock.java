package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.world.*;
import net.minecraft.state.StateManager.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

public abstract class DetectorBlock extends Block {
	
	public static final IntProperty POWER = Properties.POWER;
	public static final BooleanProperty INVERTED = Properties.INVERTED;
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);
	
	protected static final double DETECTION_DIAMETER = 20.0D;
	
	public DetectorBlock(Settings settings) {
		super(settings);
		this.setDefaultState(((this.stateManager.getDefaultState()).with(POWER, 0)).with(INVERTED, false));
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (player.canModifyBlocks()) {
			if (world.isClient) {
				return ActionResult.SUCCESS;
			} else {
				BlockState blockState = state.cycle(INVERTED);
				world.setBlockState(pos, blockState, 4);
				updateState(blockState, world, pos);
				return ActionResult.CONSUME;
			}
		} else {
			return super.onUse(state, world, pos, player, hand, hit);
		}
	}
	
	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(POWER, INVERTED);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public boolean hasSidedTransparency(BlockState state) {
		return true;
	}
	
	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.get(POWER);
	}
	
	@Override
	@Deprecated
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		updateState(state, world, pos);
		world.scheduleBlockTick(pos, state.getBlock(), getUpdateFrequencyTicks());
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		updateState(state, world, pos);
		world.scheduleBlockTick(pos, state.getBlock(), getUpdateFrequencyTicks());
	}
	
	abstract void updateState(BlockState state, World world, BlockPos pos);
	
	abstract int getUpdateFrequencyTicks();
	
	protected Box getDetectionBox(BlockPos pos) {
		return Box.of(Vec3d.ofCenter(pos), DETECTION_DIAMETER, DETECTION_DIAMETER, DETECTION_DIAMETER);
	}
	
}
