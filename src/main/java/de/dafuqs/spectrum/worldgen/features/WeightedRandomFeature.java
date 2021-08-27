package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class WeightedRandomFeature extends Feature<WeightedRandomFeatureConfig> {

	public WeightedRandomFeature(Codec<WeightedRandomFeatureConfig> codec) {
		super(codec);
	}

	public boolean generate(FeatureContext<WeightedRandomFeatureConfig> context) {
		Random random = context.getRandom();
		WeightedRandomFeatureConfig weightedRandomFeatureConfig = context.getConfig();
		StructureWorldAccess structureWorldAccess = context.getWorld();
		BlockPos blockPos = context.getOrigin();
		ChunkGenerator chunkGenerator = context.getGenerator();

		ConfiguredFeature<?, ?> configuredFeature = weightedRandomFeatureConfig.weightedRandomConfig.getEntryWithWeight(random);
		return configuredFeature.generate(structureWorldAccess, chunkGenerator, random, blockPos);
	}
}
