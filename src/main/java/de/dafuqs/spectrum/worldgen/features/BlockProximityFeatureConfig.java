package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.util.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.placement.*;

public record BlockProximityFeatureConfig(HolderSet<Block> blocksToCheckFor, Integer blockScanRange, Holder<PlacedFeature> closeToBlockFeature, Holder<PlacedFeature> fallbackFeature) implements FeatureConfiguration {
	
	public static final Codec<BlockProximityFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks_to_find").forGetter(BlockProximityFeatureConfig::blocksToCheckFor),
			ExtraCodecs.NON_NEGATIVE_INT.fieldOf("block_scan_range").forGetter(BlockProximityFeatureConfig::blockScanRange),
			PlacedFeature.CODEC.fieldOf("feature").forGetter(BlockProximityFeatureConfig::closeToBlockFeature),
			PlacedFeature.CODEC.fieldOf("fallback_feature").forGetter(BlockProximityFeatureConfig::fallbackFeature)
	).apply(instance, BlockProximityFeatureConfig::new));
	
}
