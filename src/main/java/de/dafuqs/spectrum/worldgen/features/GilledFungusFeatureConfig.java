package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.block.*;
import net.minecraft.registry.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.world.gen.feature.*;

public record GilledFungusFeatureConfig(Block validBase, Block cap, Block gills, Block stem, IntProvider baseStemHeight) implements FeatureConfig {
    
    public static final Codec<GilledFungusFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			Registries.BLOCK.getCodec().fieldOf("valid_base_block").forGetter((config) -> config.validBase),
			Registries.BLOCK.getCodec().fieldOf("cap_block").forGetter((config) -> config.cap),
			Registries.BLOCK.getCodec().fieldOf("gills_block").forGetter((config) -> config.gills),
			Registries.BLOCK.getCodec().fieldOf("stem_block").forGetter((config) -> config.stem),
			IntProvider.POSITIVE_CODEC.optionalFieldOf("base_stem_height", UniformIntProvider.create(5, 11)).forGetter((config) -> config.baseStemHeight)
	).apply(instance, GilledFungusFeatureConfig::new));
    
}
