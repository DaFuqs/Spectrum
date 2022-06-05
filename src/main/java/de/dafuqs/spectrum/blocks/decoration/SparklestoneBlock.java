package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;

public class SparklestoneBlock extends Block {
	
	public SparklestoneBlock(AbstractBlock.Settings settings) {
		super(settings);
	}
	
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		for (Direction direction : Direction.values()) {
			if (direction != Direction.DOWN) {
				BlockPos blockPos = pos.offset(direction);
				BlockState blockState = world.getBlockState(blockPos);
				if (!state.isOpaque() || !blockState.isSideSolidFullSquare(world, blockPos, direction.getOpposite())) {
					double d = direction.getOffsetX() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetX() * 0.6D;
					double e = direction.getOffsetY() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetY() * 0.6D;
					double f = direction.getOffsetZ() == 0 ? random.nextDouble() : 0.5D + (double) direction.getOffsetZ() * 0.6D;
					world.addParticle(SpectrumParticleTypes.SPARKLESTONE_SPARKLE, (double) pos.getX() + d, (double) pos.getY() + e, (double) pos.getZ() + f, 0.0D, 0.05D, 0.0D);
				}
			}
		}
	}
	
}