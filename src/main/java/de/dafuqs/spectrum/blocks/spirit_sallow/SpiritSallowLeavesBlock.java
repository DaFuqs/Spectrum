package de.dafuqs.spectrum.blocks.spirit_sallow;

import de.dafuqs.spectrum.particle.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;

public class SpiritSallowLeavesBlock extends LeavesBlock {
	
	public SpiritSallowLeavesBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		
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
