package de.dafuqs.spectrum.blocks.decoration;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class GemstoneChimeBlock extends Block {
	
	protected static final VoxelShape SHAPE = VoxelShapes.union(
			Block.createCuboidShape(5.0D, 3.0D, 5.0D, 11.0D, 13.0D, 11.0D),
			Block.createCuboidShape(7.0D, 13.0D, 7.0D, 9.0D, 16.0D, 9.0D)
	);
	
	protected final SoundEvent soundEvent;
	protected final ParticleEffect particleEffect;
	
	public GemstoneChimeBlock(Settings settings, SoundEvent soundEvent, ParticleEffect particleEffect) {
		super(settings);
		this.soundEvent = soundEvent;
		this.particleEffect = particleEffect;
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		
		world.addParticle(particleEffect,
				pos.getX() + 0.25 + random.nextDouble() * 0.5,
				pos.getY() + 0.15 + random.nextDouble() * 0.5,
				pos.getZ() + 0.25 + random.nextDouble() * 0.5,
				0, -0.02 - random.nextDouble() * 0.025, 0);
		
		if (random.nextFloat() < 0.2) {
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), soundEvent, SoundCategory.BLOCKS, 0.7F + random.nextFloat() * 0.4F, 0.75F + random.nextFloat() * 0.5F, true);
		}
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = Direction.UP;
		return Block.sideCoversSmallSquare(world, pos.offset(direction), direction.getOpposite());
	}
	
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}
	
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return direction == Direction.UP && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	public ParticleEffect getParticleEffect() {
		return this.particleEffect;
	}
	
}
