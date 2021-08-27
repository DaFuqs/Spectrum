package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedRandomFeaturePatch extends Feature<WeightedRandomFeaturePatchConfig> {

	public WeightedRandomFeaturePatch(Codec<WeightedRandomFeaturePatchConfig> codec) {
		super(codec);
	}

	public boolean generate(FeatureContext<WeightedRandomFeaturePatchConfig> context) {
		Random random = context.getRandom();
		WeightedRandomFeaturePatchConfig weightedRandomFeatureConfig = context.getConfig();
		StructureWorldAccess structureWorldAccess = context.getWorld();
		BlockPos blockPos = context.getOrigin();
		ChunkGenerator chunkGenerator = context.getGenerator();

		int i = 0;
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		List<BlockPos> generatedPos = new ArrayList<>();

		for(int j = 0; j < weightedRandomFeatureConfig.tries; ++j) {
			mutable.set(blockPos, random.nextInt(weightedRandomFeatureConfig.spreadX + 1) - random.nextInt(weightedRandomFeatureConfig.spreadX + 1), random.nextInt(weightedRandomFeatureConfig.spreadY + 1) - random.nextInt(weightedRandomFeatureConfig.spreadY + 1), random.nextInt(weightedRandomFeatureConfig.spreadZ + 1) - random.nextInt(weightedRandomFeatureConfig.spreadZ + 1));

			// generate the structures very close to one another, but leave a bit of wiggle room
			if (structureWorldAccess.isAir(mutable) || structureWorldAccess.getBlockState(mutable).getMaterial().isReplaceable()) {
				boolean canGenerate = true;
				for(BlockPos existingPos : generatedPos) {
					if(mutable.getSquaredDistance(existingPos, true) < 4) {
						canGenerate = false;
						break;
					}
				}

				if(canGenerate) {
					boolean couldGenerate = weightedRandomFeatureConfig.weightedRandomConfig.getEntryWithWeight(random).generate(structureWorldAccess, chunkGenerator, random, mutable);
					if (couldGenerate) {
						++i;
						generatedPos.add(new BlockPos(mutable.getX(), mutable.getY(), mutable.getZ()));
					}
				}

			}
		}

		return i > 0;
	}

}
