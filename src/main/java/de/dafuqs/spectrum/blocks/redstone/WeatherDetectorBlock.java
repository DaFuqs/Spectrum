package de.dafuqs.spectrum.blocks.redstone;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.state.*;

public class WeatherDetectorBlock extends DetectorBlock {
	
	public static final MapCodec<WeatherDetectorBlock> CODEC = simpleCodec(WeatherDetectorBlock::new);
	
	public WeatherDetectorBlock(Properties settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends WeatherDetectorBlock> codec() {
		return CODEC;
	}
	
	@Override
	protected void updateState(BlockState state, Level world, BlockPos pos) {
		int power = 0;
		
		if (world.isThundering()) {
			Biome.Precipitation precipitation = world.getBiome(pos).value().getPrecipitationAt(pos);
			switch (precipitation) {
				case RAIN -> power = 15;
				case SNOW -> power = 8;
			}
		} else if (world.isRaining()) {
			Biome.Precipitation precipitation = world.getBiome(pos).value().getPrecipitationAt(pos);
			switch (precipitation) {
				case RAIN, SNOW -> power = 8;
			}
		}
		
		power = state.getValue(INVERTED) ? 15 - power : power;
		if (state.getValue(POWER) != power) {
			world.setBlock(pos, state.setValue(POWER, power), 3);
		}
	}
	
	@Override
	int getUpdateFrequencyTicks() {
		return 20;
	}
	
}
