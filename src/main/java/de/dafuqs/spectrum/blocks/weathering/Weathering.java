package de.dafuqs.spectrum.blocks.weathering;

import com.google.common.base.*;
import com.google.common.collect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;

import java.util.Optional;
import java.util.function.Supplier;

public interface Weathering extends Degradable<Weathering.WeatheringLevel> {

    Supplier<BiMap<Block, Block>> WEATHERING_LEVEL_INCREASES = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder()
            .put(SpectrumBlocks.POLISHED_SHALE_CLAY, SpectrumBlocks.EXPOSED_POLISHED_SHALE_CLAY)
            .put(SpectrumBlocks.EXPOSED_POLISHED_SHALE_CLAY, SpectrumBlocks.WEATHERED_POLISHED_SHALE_CLAY)
            .put(SpectrumBlocks.POLISHED_SHALE_CLAY_STAIRS, SpectrumBlocks.EXPOSED_POLISHED_SHALE_CLAY_STAIRS)
            .put(SpectrumBlocks.EXPOSED_POLISHED_SHALE_CLAY_STAIRS, SpectrumBlocks.WEATHERED_POLISHED_SHALE_CLAY_STAIRS)
            .put(SpectrumBlocks.POLISHED_SHALE_CLAY_SLAB, SpectrumBlocks.EXPOSED_POLISHED_SHALE_CLAY_SLAB)
            .put(SpectrumBlocks.EXPOSED_POLISHED_SHALE_CLAY_SLAB, SpectrumBlocks.WEATHERED_POLISHED_SHALE_CLAY_SLAB)
            .put(SpectrumBlocks.SHALE_CLAY_BRICKS, SpectrumBlocks.EXPOSED_SHALE_CLAY_BRICKS)
            .put(SpectrumBlocks.EXPOSED_SHALE_CLAY_BRICKS, SpectrumBlocks.WEATHERED_SHALE_CLAY_BRICKS)
            .put(SpectrumBlocks.SHALE_CLAY_BRICK_STAIRS, SpectrumBlocks.EXPOSED_SHALE_CLAY_BRICK_STAIRS)
            .put(SpectrumBlocks.EXPOSED_SHALE_CLAY_BRICK_STAIRS, SpectrumBlocks.WEATHERED_SHALE_CLAY_BRICK_STAIRS)
            .put(SpectrumBlocks.SHALE_CLAY_BRICK_SLAB, SpectrumBlocks.EXPOSED_SHALE_CLAY_BRICK_SLAB)
            .put(SpectrumBlocks.EXPOSED_SHALE_CLAY_BRICK_SLAB, SpectrumBlocks.WEATHERED_SHALE_CLAY_BRICK_SLAB)
            .put(SpectrumBlocks.SHALE_CLAY_TILES, SpectrumBlocks.EXPOSED_SHALE_CLAY_TILES)
            .put(SpectrumBlocks.EXPOSED_SHALE_CLAY_TILES, SpectrumBlocks.WEATHERED_SHALE_CLAY_TILES)
            .put(SpectrumBlocks.SHALE_CLAY_TILE_STAIRS, SpectrumBlocks.EXPOSED_SHALE_CLAY_TILE_STAIRS)
            .put(SpectrumBlocks.EXPOSED_SHALE_CLAY_TILE_STAIRS, SpectrumBlocks.WEATHERED_SHALE_CLAY_TILE_STAIRS)
            .put(SpectrumBlocks.SHALE_CLAY_TILE_SLAB, SpectrumBlocks.EXPOSED_SHALE_CLAY_TILE_SLAB)
            .put(SpectrumBlocks.EXPOSED_SHALE_CLAY_TILE_SLAB, SpectrumBlocks.WEATHERED_SHALE_CLAY_TILE_SLAB)
            .build());
    Supplier<BiMap<Block, Block>> WEATHERING_LEVEL_DECREASES = Suppliers.memoize(() -> WEATHERING_LEVEL_INCREASES.get().inverse());

    static Optional<Block> getDecreasedWeatheredBlock(Block block) {
        return Optional.ofNullable(WEATHERING_LEVEL_DECREASES.get().get(block));
    }

    static Block getUnaffectedWeatheredBlock(Block block) {
        Block returnBlock = block;
        for (Block block3 = WEATHERING_LEVEL_DECREASES.get().get(block); block3 != null; block3 = WEATHERING_LEVEL_DECREASES.get().get(block3)) {
            returnBlock = block3;
        }
        return returnBlock;
    }

    static Optional<BlockState> getDecreasedWeatheredState(BlockState state) {
        return getDecreasedWeatheredBlock(state.getBlock()).map((block) -> block.getStateWithProperties(state));
	}
	
	static Optional<Block> getIncreasedWeatheredBlock(Block block) {
		return Optional.ofNullable(WEATHERING_LEVEL_INCREASES.get().get(block));
	}
	
	static BlockState getUnaffectedWeatheredState(BlockState state) {
		return getUnaffectedWeatheredBlock(state.getBlock()).getStateWithProperties(state);
	}
	
	@Override
	default Optional<BlockState> getDegradationResult(BlockState state) {
		return getIncreasedWeatheredBlock(state.getBlock()).map((block) -> block.getStateWithProperties(state));
	}
	
	@Override
	default float getDegradationChanceMultiplier() {
		return this.getDegradationLevel() == WeatheringLevel.UNAFFECTED ? 0.75F : 1.0F;
	}
	
	default boolean shouldTryWeather(World world, BlockPos pos) {
		float chance = world.isSkyVisible(pos) ? 0.5F : 0.0F;
		if (world.isRaining() && world.getBiome(pos).value().getPrecipitation() != Biome.Precipitation.NONE) {
			chance += 0.5;
		}
		return world.random.nextFloat() < chance;
	}
	
	enum WeatheringLevel {
		UNAFFECTED,
		EXPOSED,
		WEATHERED
	}
}
