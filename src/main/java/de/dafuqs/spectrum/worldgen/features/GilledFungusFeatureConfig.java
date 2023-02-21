package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.block.*;
import net.minecraft.world.gen.feature.*;

public class GilledFungusFeatureConfig implements FeatureConfig {

    public static final Codec<GilledFungusFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(BlockState.CODEC.fieldOf("valid_base_block").forGetter((config) -> {
            return config.validBaseBlock;
        }), BlockState.CODEC.fieldOf("cap_state").forGetter((config) -> {
            return config.capState;
        }), BlockState.CODEC.fieldOf("gills_state").forGetter((config) -> {
            return config.gillsState;
        }), BlockState.CODEC.fieldOf("stem_state").forGetter((config) -> {
            return config.stemState;
        })).apply(instance, GilledFungusFeatureConfig::new);
    });

    public final BlockState validBaseBlock;
    public final BlockState capState;
    public final BlockState gillsState;
    public final BlockState stemState;

    public GilledFungusFeatureConfig(BlockState validBaseBlock, BlockState capState, BlockState gillsState, BlockState stemState) {
        this.validBaseBlock = validBaseBlock;
        this.capState = capState;
        this.gillsState = gillsState;
        this.stemState = stemState;
    }

}
