package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.block.*;
import net.minecraft.world.gen.feature.*;

public record BlockStateFeatureConfig(BlockState blockState) implements FeatureConfig {

    public static final Codec<BlockStateFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            BlockState.CODEC.fieldOf("state").forGetter((config) -> config.blockState)
    ).apply(instance, BlockStateFeatureConfig::new));

}
