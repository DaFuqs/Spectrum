package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.particle.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class ShimmerstoneLightBlock extends FacingBlock {
	
	protected static final VoxelShape SHAPE_UP = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 2.0D, 12.0D);
	protected static final VoxelShape SHAPE_DOWN = Block.createCuboidShape(4.0D, 14.0D, 4.0D, 12.0D, 16.0D, 12.0D);
	protected static final VoxelShape SHAPE_NORTH = Block.createCuboidShape(4.0D, 4.0D, 14.0D, 12.0D, 12.0D, 16.0D);
	protected static final VoxelShape SHAPE_SOUTH = Block.createCuboidShape(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 2.0D);
	protected static final VoxelShape SHAPE_EAST = Block.createCuboidShape(0.0D, 4.0D, 4.0D, 2.0D, 12.0D, 12.0D);
	protected static final VoxelShape SHAPE_WEST = Block.createCuboidShape(14.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D);
	
	public ShimmerstoneLightBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(Properties.FACING, Direction.UP).with(Properties.INVERTED, false));
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		switch (state.get(Properties.FACING)) {
			case UP -> {
				return SHAPE_UP;
			}
			case DOWN -> {
				return SHAPE_DOWN;
			}
			case NORTH -> {
				return SHAPE_NORTH;
			}
			case EAST -> {
				return SHAPE_EAST;
			}
			case SOUTH -> {
				return SHAPE_SOUTH;
			}
			default -> {
				return SHAPE_WEST;
			}
		}
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}
	
	@Override
	public BlockState getPlacementState(@NotNull ItemPlacementContext ctx) {
		boolean inverted = false;
		if (ctx.getSide().getOffsetY() != 0) {
			inverted = ctx.getHorizontalPlayerFacing().getOffsetX() != 0;
		} else {
			@Nullable PlayerEntity player = ctx.getPlayer();
			inverted = player != null && player.isSneaking();
		}
		return this.getDefaultState().with(Properties.FACING, ctx.getSide()).with(Properties.INVERTED, inverted);
	}
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(Properties.FACING, rotation.rotate(state.get(Properties.FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(Properties.FACING)));
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return direction.getOpposite() == state.get(Properties.FACING) && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : state;
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = state.get(Properties.FACING);
		BlockPos blockPos = pos.offset(direction.getOpposite());
		return world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, direction);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.FACING, Properties.INVERTED);
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		
		if (random.nextFloat() < 0.3) {
			Direction direction = state.get(Properties.FACING);
			double d = direction.getOffsetX() == 0 ? 0.3D + random.nextFloat() * 0.4F : direction.getOffsetX() == 1 ? 0.15 : 0.85;
			double e = direction.getOffsetY() == 0 ? 0.3D + random.nextFloat() * 0.4F : direction.getOffsetY() == 1 ? 0.15 : 0.85;
			double f = direction.getOffsetZ() == 0 ? 0.3D + random.nextFloat() * 0.4F : direction.getOffsetZ() == 1 ? 0.15 : 0.85;
			world.addParticle(SpectrumParticleTypes.SHIMMERSTONE_SPARKLE_SMALL, (double) pos.getX() + d, (double) pos.getY() + e, (double) pos.getZ() + f, 0.0D, 0.02D, 0.0D);
		}
	}
	
}
