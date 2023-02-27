package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.block.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.world.gen.feature.*;

public record ColumnsFeatureConfig(BlockState blockState, IntProvider reach,
                                   IntProvider height) implements FeatureConfig {

    public static final Codec<ColumnsFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            BlockState.CODEC.fieldOf("state").forGetter((config) -> config.blockState),
            IntProvider.createValidatingCodec(1, 10).fieldOf("height").forGetter((config) -> config.height),
            IntProvider.createValidatingCodec(1, 10).fieldOf("height").forGetter((config) -> config.height)
    ).apply(instance, ColumnsFeatureConfig::new));

}
