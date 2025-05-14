package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.blocks.decoration.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.*;

public class JadeiteLotusFlowerBlock extends SpectrumFacingBlock {
	
	protected static final VoxelShape SHAPE_UP = Block.box(0, 0, 0, 16, 8, 16);
	protected static final VoxelShape SHAPE_DOWN = Block.box(0, 8, 0, 16, 16, 16);
	protected static final VoxelShape SHAPE_NORTH = Block.box(0.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D);
	protected static final VoxelShape SHAPE_SOUTH = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D);
	protected static final VoxelShape SHAPE_EAST = Block.box(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D);
	protected static final VoxelShape SHAPE_WEST = Block.box(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	
	public JadeiteLotusFlowerBlock(Properties settings) {
		super(settings);
		registerDefaultState(defaultBlockState());
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
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		var facing = state.getValue(FACING);
		var root = pos.relative(facing.getOpposite());
		var supportBlock = world.getBlockState(root);
		return (facing.getAxis().isVertical() && supportBlock.is(SpectrumBlocks.JADEITE_LOTUS_STEM)) || supportBlock.isFaceSturdy(world, root, facing, SupportType.CENTER);
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (!state.canSurvive(world, pos)) {
			world.scheduleTick(pos, this, 1);
		}
		
		return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		var amount = random.nextInt(18) + 9;
		for (int i = 0; i < amount; i++) {
			var xOffset = Mth.clamp(Mth.normal(random, 0.5F, 5.85F), -9F, 9F) + 0.5F;
			var yOffset = Mth.clamp(Mth.normal(random, 0.5F, 5.85F), -9F, 9F) + 0.5F;
			var zOffset = Mth.clamp(Mth.normal(random, 0.5F, 5.85F), -9F, 9F) + 0.5F;
			world.addAlwaysVisibleParticle(ParticleTypes.END_ROD, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, random.nextFloat() * 0.05 - 0.025, random.nextFloat() * 0.05 - 0.025, random.nextFloat() * 0.05 - 0.025);
		}
	}
	
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		super.tick(state, world, pos, random);
		if (!state.canSurvive(world, pos)) {
			world.destroyBlock(pos, true);
		}
	}
	
}
