package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.particle.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.Nullable;

public class ShimmerstoneLightBlock extends DirectionalBlock {
	
	public static final MapCodec<ShimmerstoneLightBlock> CODEC = simpleCodec(ShimmerstoneLightBlock::new);
	
	protected static final VoxelShape SHAPE_UP = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 2.0D, 12.0D);
	protected static final VoxelShape SHAPE_DOWN = Block.box(4.0D, 14.0D, 4.0D, 12.0D, 16.0D, 12.0D);
	protected static final VoxelShape SHAPE_NORTH = Block.box(4.0D, 4.0D, 14.0D, 12.0D, 12.0D, 16.0D);
	protected static final VoxelShape SHAPE_SOUTH = Block.box(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 2.0D);
	protected static final VoxelShape SHAPE_EAST = Block.box(0.0D, 4.0D, 4.0D, 2.0D, 12.0D, 12.0D);
	protected static final VoxelShape SHAPE_WEST = Block.box(14.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D);
	
	public ShimmerstoneLightBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.FACING, Direction.UP).setValue(BlockStateProperties.INVERTED, false));
	}

	@Override
	public MapCodec<? extends ShimmerstoneLightBlock> codec() {
		return CODEC;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		switch (state.getValue(BlockStateProperties.FACING)) {
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
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		boolean inverted;
		if (ctx.getClickedFace().getStepY() != 0) {
			inverted = ctx.getHorizontalDirection().getStepX() != 0;
		} else {
			@Nullable Player player = ctx.getPlayer();
			inverted = player != null && player.isShiftKeyDown();
		}
		return this.defaultBlockState().setValue(BlockStateProperties.FACING, ctx.getClickedFace()).setValue(BlockStateProperties.INVERTED, inverted);
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(BlockStateProperties.FACING, rotation.rotate(state.getValue(BlockStateProperties.FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(BlockStateProperties.FACING)));
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		return direction.getOpposite() == state.getValue(BlockStateProperties.FACING) && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : state;
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		Direction direction = state.getValue(BlockStateProperties.FACING);
		BlockPos blockPos = pos.relative(direction.getOpposite());
		return world.getBlockState(blockPos).isFaceSturdy(world, blockPos, direction);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.FACING, BlockStateProperties.INVERTED);
	}
	
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);
		
		if (random.nextFloat() < 0.3) {
			Direction direction = state.getValue(BlockStateProperties.FACING);
			double d = direction.getStepX() == 0 ? 0.3D + random.nextFloat() * 0.4F : direction.getStepX() == 1 ? 0.15 : 0.85;
			double e = direction.getStepY() == 0 ? 0.3D + random.nextFloat() * 0.4F : direction.getStepY() == 1 ? 0.15 : 0.85;
			double f = direction.getStepZ() == 0 ? 0.3D + random.nextFloat() * 0.4F : direction.getStepZ() == 1 ? 0.15 : 0.85;
			world.addParticle(SpectrumParticleTypes.SHIMMERSTONE_SPARKLE_SMALL, (double) pos.getX() + d, (double) pos.getY() + e, (double) pos.getZ() + f, 0.0D, 0.02D, 0.0D);
		}
	}
	
}
