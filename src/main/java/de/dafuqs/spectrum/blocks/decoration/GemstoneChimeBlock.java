package de.dafuqs.spectrum.blocks.decoration;

import net.minecraft.block.*;
import net.minecraft.block.piston.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.particle.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;

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
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = Direction.UP;
		return Block.sideCoversSmallSquare(world, pos.offset(direction), direction.getOpposite());
	}
	
	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return direction == Direction.UP && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}
	
	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	public ParticleEffect getParticleEffect() {
		return this.particleEffect;
	}
	
}
