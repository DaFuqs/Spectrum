package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.block.*;
import net.minecraft.world.gen.feature.*;

public class BlockStateFeatureConfig implements FeatureConfig {

    public static final Codec<BlockStateFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(BlockState.CODEC.fieldOf("state").forGetter((config) -> {
            return config.blockState;
        })).apply(instance, BlockStateFeatureConfig::new);
    });

    public final BlockState blockState;

    public BlockStateFeatureConfig(BlockState blockState) {
        this.blockState = blockState;
    }

}
