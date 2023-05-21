package de.dafuqs.spectrum.blocks.weathering;

import net.minecraft.block.*;
import net.minecraft.server.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;

public class WeatheringBlock extends Block implements Weathering {
	
	private final Weathering.WeatheringLevel weatheringLevel;
	
	public WeatheringBlock(Weathering.WeatheringLevel weatheringLevel, AbstractBlock.Settings settings) {
		super(settings);
		this.weatheringLevel = weatheringLevel;
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (shouldTryWeather(world, pos)) {
			this.tickDegradation(state, world, pos, random);
		}
	}
	
	@Override
	public boolean hasRandomTicks(BlockState state) {
		return Weathering.getIncreasedWeatheredBlock(state.getBlock()).isPresent();
	}
	
	@Override
	public Weathering.WeatheringLevel getDegradationLevel() {
		return this.weatheringLevel;
	}
	
}
