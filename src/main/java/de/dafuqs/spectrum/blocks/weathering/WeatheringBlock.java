package de.dafuqs.spectrum.blocks.weathering;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class WeatheringBlock extends Block implements Weathering {

    private final Weathering.WeatheringLevel weatheringLevel;

    public WeatheringBlock(Weathering.WeatheringLevel weatheringLevel, AbstractBlock.Settings settings) {
        super(settings);
        this.weatheringLevel = weatheringLevel;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (shouldTryWeather(world, pos)) {
            this.tickDegradation(state, world, pos, random);
        }
    }

    public boolean hasRandomTicks(BlockState state) {
        return Weathering.getIncreasedWeatheredBlock(state.getBlock()).isPresent();
    }

    public Weathering.WeatheringLevel getDegradationLevel() {
        return this.weatheringLevel;
    }

}
