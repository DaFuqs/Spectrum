package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class WeightedRandomFeature extends Feature<WeightedRandomFeatureConfig> {
	
	public WeightedRandomFeature(Codec<WeightedRandomFeatureConfig> codec) {
		super(codec);
	}
	
	public boolean generate(FeatureContext<WeightedRandomFeatureConfig> context) {
		Random random = context.getRandom();
		StructureWorldAccess structureWorldAccess = context.getWorld();
		BlockPos blockPos = context.getOrigin();
		
		WeightedRandomFeatureConfig weightedRandomFeatureConfig = context.getConfig();
		PlacedFeature randomPlacedFeature = weightedRandomFeatureConfig.getWeightedRandomFeature(context.getRandom());
		return randomPlacedFeature.generateUnregistered(structureWorldAccess, context.getGenerator(), random, blockPos);
	}
	
}
