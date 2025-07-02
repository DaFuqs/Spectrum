package de.dafuqs.spectrum.blocks.weathering;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class WeatheringStairsBlock extends StairBlock implements Weathering {
	
	private final Weathering.WeatheringLevel weatheringLevel;
	
	public WeatheringStairsBlock(Weathering.WeatheringLevel weatheringLevel, BlockState baseBlockState, BlockBehaviour.Properties settings) {
		super(baseBlockState, settings);
		this.weatheringLevel = weatheringLevel;
	}

	@Override
	public MapCodec<? extends WeatheringStairsBlock> codec() {
		//TODO: Make the codec
		return null;
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (shouldTryWeather(world, pos)) {
			this.changeOverTime(state, world, pos, random);
		}
	}
	
	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return Weathering.getIncreasedWeatheredBlock(state.getBlock()).isPresent();
	}
	
	@Override
	public Weathering.WeatheringLevel getAge() {
		return this.weatheringLevel;
	}
	
}
