package de.dafuqs.spectrum.blocks.spirit_sallow;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.particle.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class SpiritSallowLeavesBlock extends LeavesBlock {
	
	public static final MapCodec<SpiritSallowLeavesBlock> CODEC = simpleCodec(SpiritSallowLeavesBlock::new);
	
	public SpiritSallowLeavesBlock(Properties settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends SpiritSallowLeavesBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);
		
		if (random.nextBoolean() /*!state.get(LeavesBlock.PERSISTENT) && state.get(LeavesBlock.DISTANCE) > 1 && world.getBlockState(pos.up()).isAir()*/) {
			double startX = pos.getX() + random.nextFloat();
			double startY = pos.getY() + 1.01;
			double startZ = pos.getZ() + random.nextFloat();
			
			double velocityX = 0.02 - random.nextFloat() * 0.04;
			double velocityY = 0.005 + random.nextFloat() * 0.01;
			double velocityZ = 0.02 - random.nextFloat() * 0.04;
			
			world.addParticle(SpectrumParticleTypes.SPIRIT_SALLOW, startX, startY, startZ, velocityX, velocityY, velocityZ);
		}
		
	}
	
	
}
