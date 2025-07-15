package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.*;

public class WeightedRandomFeature extends Feature<WeightedRandomFeatureConfig> {
	
	public WeightedRandomFeature(Codec<WeightedRandomFeatureConfig> codec) {
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<WeightedRandomFeatureConfig> context) {
		RandomSource random = context.random();
		WorldGenLevel structureWorldAccess = context.level();
		BlockPos blockPos = context.origin();
		
		WeightedRandomFeatureConfig weightedRandomFeatureConfig = context.config();
		Optional<PlacedFeature> randomPlacedFeature = weightedRandomFeatureConfig.features().getRandomValue(context.random());
		return randomPlacedFeature.map(placedFeature -> placedFeature.place(structureWorldAccess, context.chunkGenerator(), random, blockPos)).orElse(false);
	}
	
}
