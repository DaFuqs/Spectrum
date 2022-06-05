package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class SparklestoneLightBlock extends Block {
	
	public static final DirectionProperty FACING = Properties.FACING;
	
	protected static final VoxelShape SHAPE_UP = Block.createCuboidShape(5.0D, 0.0D, 4.0D, 11.0D, 2.0D, 12.0D);
	protected static final VoxelShape SHAPE_DOWN = Block.createCuboidShape(5.0D, 14.0D, 4.0D, 11.0D, 16.0D, 12.0D);
	protected static final VoxelShape SHAPE_NORTH = Block.createCuboidShape(5.0D, 4.0D, 14.0D, 11.0D, 12.0D, 16.0D);
	protected static final VoxelShape SHAPE_SOUTH = Block.createCuboidShape(5.0D, 4.0D, 0.0D, 11.0D, 12.0D, 2.0D);
	protected static final VoxelShape SHAPE_EAST = Block.createCuboidShape(0.0D, 4.0D, 5.0D, 2.0D, 12.0D, 11.0D);
	protected static final VoxelShape SHAPE_WEST = Block.createCuboidShape(14.0D, 4.0D, 5.0D, 16.0D, 12.0D, 11.0D);
	
	public SparklestoneLightBlock(Settings settings) {
		super(settings);
		this.setDefaultState(((this.stateManager.getDefaultState()).with(FACING, Direction.UP)));
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		switch (state.get(FACING)) {
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
	
	public BlockState getPlacementState(@NotNull ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getSide());
	}
	
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}
	
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}
	
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		
		if (random.nextFloat() < 0.3) {
			Direction direction = state.get(FACING);
			double d = direction.getOffsetX() == 0 ? 0.3D + random.nextFloat() * 0.4F : direction.getOffsetX() == 1 ? 0.15 : 0.85;
			double e = direction.getOffsetY() == 0 ? 0.3D + random.nextFloat() * 0.4F : direction.getOffsetY() == 1 ? 0.15 : 0.85;
			double f = direction.getOffsetZ() == 0 ? 0.3D + random.nextFloat() * 0.4F : direction.getOffsetZ() == 1 ? 0.15 : 0.85;
			world.addParticle(SpectrumParticleTypes.SPARKLESTONE_SPARKLE_SMALL, (double) pos.getX() + d, (double) pos.getY() + e, (double) pos.getZ() + f, 0.0D, 0.02D, 0.0D);
		}
	}
	
}
