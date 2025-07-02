package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.core.registries.*;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;

public record GilledFungusFeatureConfig(Block validBase, Block cap, Block gills, Block stem, IntProvider baseStemHeight) implements FeatureConfiguration {
    
    public static final Codec<GilledFungusFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("valid_base_block").forGetter((config) -> config.validBase),
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("cap_block").forGetter((config) -> config.cap),
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("gills_block").forGetter((config) -> config.gills),
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("stem_block").forGetter((config) -> config.stem),
			IntProvider.POSITIVE_CODEC.optionalFieldOf("base_stem_height", UniformInt.of(5, 11)).forGetter((config) -> config.baseStemHeight)
	).apply(instance, GilledFungusFeatureConfig::new));
    
}
