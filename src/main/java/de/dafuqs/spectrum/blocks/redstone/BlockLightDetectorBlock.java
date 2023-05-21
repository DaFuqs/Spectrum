package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class BlockLightDetectorBlock extends DetectorBlock {
	
	public BlockLightDetectorBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	protected void updateState(BlockState state, World world, BlockPos pos) {
		int power = world.getLightLevel(pos);
		
		boolean bl = state.get(INVERTED);
		if (bl) {
			power = 15 - power;
		}
		
		if (state.get(POWER) != power) {
			world.setBlockState(pos, state.with(POWER, power), 3);
		}
	}
	
	@Override
	int getUpdateFrequencyTicks() {
		return 20;
	}
	
}
