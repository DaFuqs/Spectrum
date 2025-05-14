package de.dafuqs.spectrum.blocks.decoration;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.particle.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class ShimmerstoneBlock extends Block {
	
	public static final MapCodec<ShimmerstoneBlock> CODEC = simpleCodec(ShimmerstoneBlock::new);
	
	public ShimmerstoneBlock(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends ShimmerstoneBlock> codec() {
		return CODEC;
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		if (random.nextBoolean()) {
			for (Direction direction : Direction.values()) {
				if (direction != Direction.DOWN) {
					BlockPos blockPos = pos.relative(direction);
					BlockState blockState = world.getBlockState(blockPos);
					if (!state.canOcclude() || !blockState.isFaceSturdy(world, blockPos, direction.getOpposite())) {
						double d = direction.getStepX() == 0 ? random.nextDouble() : 0.5D + (double) direction.getStepX() * 0.6D;
						double e = direction.getStepY() == 0 ? random.nextDouble() : 0.5D + (double) direction.getStepY() * 0.6D;
						double f = direction.getStepZ() == 0 ? random.nextDouble() : 0.5D + (double) direction.getStepZ() * 0.6D;
						world.addParticle(SpectrumParticleTypes.SHIMMERSTONE_SPARKLE, (double) pos.getX() + d, (double) pos.getY() + e, (double) pos.getZ() + f, 0.0D, 0.05D, 0.0D);
					}
				}
			}
		}
	}
}