package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public abstract class DetectorBlock extends Block {
	
	public static final IntProperty POWER = Properties.POWER;
	public static final BooleanProperty INVERTED = Properties.INVERTED;
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);
	
	public DetectorBlock(Settings settings) {
		super(settings);
		this.setDefaultState(((this.stateManager.getDefaultState()).with(POWER, 0)).with(INVERTED, false));
	}
	
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
	
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(POWER, INVERTED);
	}
	
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	public boolean hasSidedTransparency(BlockState state) {
		return true;
	}
	
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.get(POWER);
	}
	
	@Override
	@Deprecated
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		updateState(state, world, pos);
		world.createAndScheduleBlockTick(pos, state.getBlock(), getUpdateFrequencyTicks());
	}
	
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		updateState(state, world, pos);
		world.createAndScheduleBlockTick(pos, state.getBlock(), getUpdateFrequencyTicks());
	}
	
	abstract void updateState(BlockState state, World world, BlockPos pos);
	
	abstract int getUpdateFrequencyTicks();
	
	protected Box getBoxWithRadius(BlockPos blockPos, int radius) {
		return Box.of(Vec3d.ofCenter(blockPos), radius, radius, radius);
	}
	
}
