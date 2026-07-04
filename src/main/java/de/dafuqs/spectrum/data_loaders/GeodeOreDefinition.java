package de.dafuqs.spectrum.data_loaders;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import de.dafuqs.spectrum.api.predicate.block.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.levelgen.feature.configurations.*;

import java.util.*;

public record GeodeOreDefinition(BrokenBlockPredicate blocks, List<OreConfiguration.TargetBlockState> oreConfiguration, int minDistance, int maxDistance, int tries) {
	public static final Codec<GeodeOreDefinition> CODEC = RecordCodecBuilder.create((i -> i.group(
			BrokenBlockPredicate.CODEC.fieldOf("geode_inner_layer_block_predicate").forGetter(c -> c.blocks),
			Codec.list(OreConfiguration.TargetBlockState.CODEC).fieldOf("ore_configuration").forGetter(c -> c.oreConfiguration),
			Codec.INT.fieldOf("min_distance_from_center").forGetter(c -> c.minDistance),
			Codec.INT.fieldOf("max_distance_from_center").forGetter(c -> c.maxDistance),
			Codec.INT.fieldOf("tries_per_range_increment").forGetter(c -> c.tries)
	).apply(i, GeodeOreDefinition::new)));
}