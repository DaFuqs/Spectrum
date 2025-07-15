package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.*;

/**
 * A configurable GlowstoneBlobFeature that can grow both up- and downward
 */
public class CrystalFormationFeature extends Feature<CrystalFormationFeatureFeatureConfig> {
	
	public CrystalFormationFeature(Codec<CrystalFormationFeatureFeatureConfig> configCodec) {
		super(configCodec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<CrystalFormationFeatureFeatureConfig> context) {
		WorldGenLevel structureWorldAccess = context.level();
		BlockPos blockPos = context.origin();
		RandomSource random = context.random();
		if (!structureWorldAccess.isEmptyBlock(blockPos)) {
			return false;
		} else {
			CrystalFormationFeatureFeatureConfig config = context.config();
			
			boolean upwards = false;
			if (config.canGrowUpwards() && structureWorldAccess.getBlockState(blockPos.below()).is(config.canStartOnBlocks())) {
				upwards = true;
			} else if (!config.canGrowDownwards() || !structureWorldAccess.getBlockState(blockPos.above()).is(config.canStartOnBlocks())) {
				return false;
			}
			
			BlockStateProvider stateProvider = config.blockStateProvider();
			int iterations = config.iterationCountProvider().sample(random);
			
			structureWorldAccess.setBlock(blockPos, stateProvider.getState(random, blockPos), 2);
			
			for (int i = 0; i < iterations; ++i) {
				BlockPos offsetPos = blockPos.offset(random.nextInt(8) - random.nextInt(8), upwards ? random.nextInt(12) : -random.nextInt(12), random.nextInt(8) - random.nextInt(8));
				if (structureWorldAccess.getBlockState(offsetPos).isAir()) {
					int directionTries = 0;
					for (Direction direction : Direction.values()) {
						if (structureWorldAccess.getBlockState(offsetPos.relative(direction)).is(config.canExtendOnBlocks())) {
							++directionTries;
						}
						if (directionTries > 1) {
							break;
						}
					}
					if (directionTries == 1) {
						structureWorldAccess.setBlock(offsetPos, stateProvider.getState(random, blockPos), 2);
					}
				}
			}
			
			return true;
		}
	}
	
}
