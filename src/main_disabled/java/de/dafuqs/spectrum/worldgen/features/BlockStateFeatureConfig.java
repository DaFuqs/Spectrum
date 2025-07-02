package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;

public record BlockStateFeatureConfig(BlockState blockState) implements FeatureConfiguration {

    public static final Codec<BlockStateFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			BlockState.CODEC.fieldOf("state").forGetter((config) -> config.blockState)
    ).apply(instance, BlockStateFeatureConfig::new));

}
