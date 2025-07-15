package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;

public record ColumnsFeatureConfig(BlockState blockState, IntProvider reach,
								   IntProvider height) implements FeatureConfiguration {
	
	public static final Codec<ColumnsFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			BlockState.CODEC.fieldOf("state").forGetter((config) -> config.blockState),
			IntProvider.codec(1, 10).fieldOf("reach").forGetter((config) -> config.reach),
			IntProvider.codec(1, 10).fieldOf("height").forGetter((config) -> config.height)
	).apply(instance, ColumnsFeatureConfig::new));
	
}
