package de.dafuqs.spectrum.blocks.redstone;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class WeatherDetectorBlock extends DetectorBlock {
	
	public WeatherDetectorBlock(Settings settings) {
		super(settings);
	}
	
	protected void updateState(BlockState state, World world, BlockPos pos) {
		int power = 0;
		
		if (world.isThundering()) {
			Biome.Precipitation precipitation = world.getBiome(pos).value().getPrecipitation();
			switch (precipitation) {
				case RAIN -> {
					power = 15;
				}
				case SNOW -> {
					power = 8;
				}
			}
		} else if (world.isRaining()) {
			Biome.Precipitation precipitation = world.getBiome(pos).value().getPrecipitation();
			switch (precipitation) {
				case RAIN, SNOW -> {
					power = 8;
				}
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
