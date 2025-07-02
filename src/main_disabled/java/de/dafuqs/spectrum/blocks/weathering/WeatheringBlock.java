package de.dafuqs.spectrum.blocks.weathering;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

public class WeatheringBlock extends Block implements Weathering {
	
	private final Weathering.WeatheringLevel weatheringLevel;
	
	public WeatheringBlock(Weathering.WeatheringLevel weatheringLevel, BlockBehaviour.Properties settings) {
		super(settings);
		this.weatheringLevel = weatheringLevel;
	}

	@Override
	public MapCodec<? extends WeatheringBlock> codec() {
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
