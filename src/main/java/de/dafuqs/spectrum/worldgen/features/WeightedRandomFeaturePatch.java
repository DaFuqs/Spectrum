package de.dafuqs.spectrum.worldgen.features;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import de.dafuqs.spectrum.blocks.SpectrumWoodenButtonBlock;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.worldgen.SpectrumConfiguredFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.*;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.RandomPatchFeature;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class WeightedRandomFeaturePatch extends Feature<WeightedRandomFeaturePatchConfig> {
	
	private static final List<PlacementModifier> placementModifiers = List.of(
			SquarePlacementModifier.of(),
			(VegetationPlacedFeatures.NOT_IN_SURFACE_WATER_MODIFIER),
			(PlacedFeatures.OCEAN_FLOOR_HEIGHTMAP),
			(BiomePlacementModifier.of()),
			(BlockFilterPlacementModifier.of(BlockPredicate.wouldSurvive(SpectrumBlocks.RED_SAPLING.getDefaultState(), BlockPos.ORIGIN)))
	);
	
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
		List<BlockPos> generatedPos = new ArrayList<>();

		for(int j = 0; j < context.getConfig().tries; ++j) {
			BlockPos randomOffsetPos = new BlockPos(
					blockPos.getX() + random.nextInt(weightedRandomFeatureConfig.spreadX + 1) - random.nextInt(weightedRandomFeatureConfig.spreadX + 1),
					blockPos.getY() + random.nextInt(weightedRandomFeatureConfig.spreadY + 1) - random.nextInt(weightedRandomFeatureConfig.spreadY + 1),
					blockPos.getZ() + random.nextInt(weightedRandomFeatureConfig.spreadZ + 1) - random.nextInt(weightedRandomFeatureConfig.spreadZ + 1));
			
			// TODO
			/*for(PlacementModifier placementModifier : placementModifiers) {
				randomOffsetPos = placementModifier.getPositions(context, random, randomOffsetPos)
			}
			
			.getPositions(new DecoratorContext(context.getWorld(), context.getGenerator(), Optional.empty()), random, randomOffsetPos).toList();
			if(availablePos.size() > 0) {
				BlockPos targetPos = availablePos.get(0);
			
				// generate the structures very close to one another, but leave a bit of wiggle room
				if (structureWorldAccess.isAir(targetPos) || structureWorldAccess.getBlockState(targetPos).getMaterial().isReplaceable()) {
					boolean canGenerate = true;
					for (BlockPos existingPos : generatedPos) {
						if (targetPos.getSquaredDistance(existingPos, true) < 4) {
							canGenerate = false;
							break;
						}
					}
					
					if (canGenerate) {
						boolean couldGenerate = weightedRandomFeatureConfig.weightedRandomConfig.getEntryWithWeight(random).generate(structureWorldAccess, chunkGenerator, random, targetPos);
						if (couldGenerate) {
							++i;
							generatedPos.add(new BlockPos(targetPos.getX(), targetPos.getY(), targetPos.getZ()));
						}
					}
				}

			}*/
		}

		return i > 0;
	}

}
