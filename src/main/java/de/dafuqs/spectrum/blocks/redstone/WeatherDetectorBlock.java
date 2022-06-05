package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WeatherDetectorBlock extends DetectorBlock {
	
	public WeatherDetectorBlock(Settings settings) {
		super(settings);
	}
	
	protected void updateState(BlockState state, World world, BlockPos pos) {
		int power = 0;
		
		if (world.hasRain(pos.up())) {
			if (world.isThundering()) {
				power = 15;
			} else if (world.isRaining()) {
				power = 8;
			}
		}
		
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
