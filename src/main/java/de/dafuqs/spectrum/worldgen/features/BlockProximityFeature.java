package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.levelgen.feature.*;

public class BlockProximityFeature extends Feature<BlockProximityFeatureConfig> {
	
	public BlockProximityFeature(Codec<BlockProximityFeatureConfig> codec) {
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<BlockProximityFeatureConfig> context) {
		BlockProximityFeatureConfig config = context.config();
		RandomSource random = context.random();
		BlockPos pos = context.origin();
		WorldGenLevel level = context.level();
		
		if (closeToBlock(level, pos, config.blockScanRange(), config.blocksToCheckFor())) {
			return config.closeToBlockFeature().value().place(level, context.chunkGenerator(), random, pos);
		} else {
			return config.fallbackFeature().value().place(level, context.chunkGenerator(), random, pos);
		}
	}
	
	protected boolean closeToBlock(WorldGenLevel world, BlockPos pos, int searchRange, HolderSet<Block> blocksToSearchFor) {
		for (BlockPos currentPos : BlockPos.withinManhattan(pos, searchRange, searchRange, searchRange)) {
			if (world.getBlockState(currentPos).is(blocksToSearchFor)) {
				return true;
			}
		}
		return false;
	}
	
}
