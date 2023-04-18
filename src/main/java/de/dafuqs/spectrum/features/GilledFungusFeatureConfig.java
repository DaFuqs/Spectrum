package de.dafuqs.spectrum.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.block.*;
import net.minecraft.world.gen.feature.*;

public record GilledFungusFeatureConfig(BlockState validBaseBlock, BlockState capState, BlockState gillsState,
                                        BlockState stemState) implements FeatureConfig {

    public static final Codec<GilledFungusFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            BlockState.CODEC.fieldOf("valid_base_block").forGetter((config) -> config.validBaseBlock),
            BlockState.CODEC.fieldOf("cap_state").forGetter((config) -> config.capState),
            BlockState.CODEC.fieldOf("gills_state").forGetter((config) -> config.gillsState),
            BlockState.CODEC.fieldOf("stem_state").forGetter((config) -> config.stemState)
    ).apply(instance, GilledFungusFeatureConfig::new));

}
