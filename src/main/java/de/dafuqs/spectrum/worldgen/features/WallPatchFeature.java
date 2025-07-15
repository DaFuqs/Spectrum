package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.levelgen.feature.*;

import java.util.*;

public class WallPatchFeature extends Feature<WallPatchFeatureConfig> {
	
	public WallPatchFeature(Codec<WallPatchFeatureConfig> codec) {
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<WallPatchFeatureConfig> context) {
		WorldGenLevel structureWorldAccess = context.level();
		BlockPos blockPos = context.origin();
		RandomSource random = context.random();
		WallPatchFeatureConfig config = context.config();
		if (!isAirOrWater(structureWorldAccess.getBlockState(blockPos))) {
			return false;
		} else {
			List<Direction> shuffledDirections = config.shuffleDirections(random);
			if (generate(structureWorldAccess, blockPos, config, random)) {
				return true;
			} else {
				BlockPos.MutableBlockPos mutable = blockPos.mutable();
				
				for (Direction direction : shuffledDirections) {
					mutable.set(blockPos);
					for (int i = 0; i < config.searchRange; ++i) {
						mutable.setWithOffset(blockPos, direction);
						BlockState blockState = structureWorldAccess.getBlockState(mutable);
						if (!isAirOrWater(blockState) && !blockState.is(config.block)) {
							break;
						}
						if (generate(structureWorldAccess, mutable, config, random)) {
							return true;
						}
					}
				}
				return false;
			}
		}
	}
	
	public static boolean generate(WorldGenLevel world, BlockPos pos, WallPatchFeatureConfig config, RandomSource random) {
		BlockPos.MutableBlockPos mutable = pos.mutable();
		
		BlockState posState;
		Direction direction;
		boolean success = false;
		
		for (BlockPos currPos : BlockPos.withinManhattan(pos, config.width.sample(random), config.height.sample(random), config.width.sample(random))) {
			if (!isAirOrWater(world.getBlockState(currPos))) {
				continue;
			}
			
			Iterator<Direction> directionIterator = config.shuffleDirections(random).iterator();
			boolean canBePlaced = false;
			do {
				direction = directionIterator.next();
				posState = world.getBlockState(mutable.setWithOffset(currPos, direction));
				if (posState.is(config.canPlaceOn)) {
					canBePlaced = true;
				}
			} while (!canBePlaced && directionIterator.hasNext());
			
			if (!canBePlaced) {
				continue;
			}
			
			BlockState stateToPlace = config.block.defaultBlockState();
			if (config.block instanceof DirectionalBlock) {
				stateToPlace = stateToPlace.setValue(BlockStateProperties.FACING, direction.getOpposite());
			}
			
			if (stateToPlace != null) {
				world.setBlock(currPos, stateToPlace, 3);
				success = true;
			}
		}
		
		return success;
	}
	
	private static boolean isAirOrWater(BlockState state) {
		return state.isAir() || state.is(Blocks.WATER);
	}
	
}
