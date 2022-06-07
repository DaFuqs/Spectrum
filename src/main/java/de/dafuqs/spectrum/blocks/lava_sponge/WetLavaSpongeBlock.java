package de.dafuqs.spectrum.blocks.lava_sponge;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WetSpongeBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.Random;

public class WetLavaSpongeBlock extends WetSpongeBlock {
	
	public WetLavaSpongeBlock(Settings settings) {
		super(settings);
	}
	
	// faster than fire (30+ 0-10)
	// even more in the nether
	private static int getRandomTickTime(World world) {
		if (world.getDimension().isUltrawarm()) {
			return 10 + world.random.nextInt(5);
		} else {
			return 20 + world.random.nextInt(10);
		}
	}
	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.createAndScheduleBlockTick(pos, this, getRandomTickTime(world));
		
		if (world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) {
			int xOffset = 2 - random.nextInt(5);
			int yOffset = 1 - random.nextInt(3);
			int zOffset = 2 - random.nextInt(5);
			
			BlockPos targetPos = pos.add(xOffset, yOffset, zOffset);
			if (world.getBlockState(targetPos).isAir() && world.getBlockState(targetPos.down()).getMaterial().isSolid()) {
				world.setBlockState(targetPos, Blocks.FIRE.getDefaultState());
			}
		}
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		world.createAndScheduleBlockTick(pos, this, getRandomTickTime(world));
	}
	
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		Direction direction = Direction.random(random);
		if (direction != Direction.UP) {
			BlockPos blockPos = pos.offset(direction);
			BlockState blockState = world.getBlockState(blockPos);
			if (!state.isOpaque() || !blockState.isSideSolidFullSquare(world, blockPos, direction.getOpposite())) {
				double d = pos.getX();
				double e = pos.getY();
				double f = pos.getZ();
				if (direction == Direction.DOWN) {
					e -= 0.05D;
					d += random.nextDouble();
					f += random.nextDouble();
				} else {
					e += random.nextDouble() * 0.8D;
					if (direction.getAxis() == Direction.Axis.X) {
						f += random.nextDouble();
						if (direction == Direction.EAST) {
							++d;
						} else {
							d += 0.05D;
						}
					} else {
						d += random.nextDouble();
						if (direction == Direction.SOUTH) {
							++f;
						} else {
							f += 0.05D;
						}
					}
				}
				
				world.addParticle(ParticleTypes.DRIPPING_LAVA, d, e, f, 0.0D, 0.0D, 0.0D);
			}
		}
	}
	
}
