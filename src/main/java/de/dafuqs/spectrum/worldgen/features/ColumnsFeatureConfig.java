package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.block.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.world.gen.feature.*;

public class ColumnsFeatureConfig implements FeatureConfig {

    public static final Codec<ColumnsFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(BlockState.CODEC.fieldOf("state").forGetter((config) -> {
            return config.blockState;
        }), IntProvider.createValidatingCodec(1, 10).fieldOf("height").forGetter((config) -> {
            return config.height;
        }), IntProvider.createValidatingCodec(1, 10).fieldOf("height").forGetter((config) -> {
            return config.height;
        })).apply(instance, ColumnsFeatureConfig::new);
    });

    private final BlockState blockState;
    private final IntProvider reach;
    private final IntProvider height;

    public ColumnsFeatureConfig(BlockState blockState, IntProvider reach, IntProvider height) {
        this.blockState = blockState;
        this.reach = reach;
        this.height = height;
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public IntProvider getReach() {
        return this.reach;
    }

    public IntProvider getHeight() {
        return this.height;
    }
}
