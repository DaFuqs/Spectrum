package de.dafuqs.spectrum.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.block.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.feature.*;

public record GilledFungusFeatureConfig(Block validBase, Block cap, Block gills, Block stem) implements FeatureConfig {
    
    public static final Codec<GilledFungusFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Registry.BLOCK.getCodec().fieldOf("valid_base_block").forGetter((config) -> config.validBase),
            Registry.BLOCK.getCodec().fieldOf("cap_block").forGetter((config) -> config.cap),
            Registry.BLOCK.getCodec().fieldOf("gills_block").forGetter((config) -> config.gills),
            Registry.BLOCK.getCodec().fieldOf("stem_block").forGetter((config) -> config.stem)
    ).apply(instance, GilledFungusFeatureConfig::new));
    
}
