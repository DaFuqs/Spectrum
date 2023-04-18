package de.dafuqs.spectrum.features;

import com.mojang.serialization.*;
import net.minecraft.block.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;

import java.util.*;

public class WallPatchFeature extends Feature<WallPatchFeatureConfig> {
	
	public WallPatchFeature(Codec<WallPatchFeatureConfig> codec) {
		super(codec);
	}
	
	public boolean generate(FeatureContext<WallPatchFeatureConfig> context) {
		StructureWorldAccess structureWorldAccess = context.getWorld();
		BlockPos blockPos = context.getOrigin();
		Random random = context.getRandom();
		WallPatchFeatureConfig config = context.getConfig();
		if (!isAirOrWater(structureWorldAccess.getBlockState(blockPos))) {
			return false;
		} else {
			List<Direction> shuffledDirections = config.shuffleDirections(random);
			if (generate(structureWorldAccess, blockPos, config, random)) {
				return true;
			} else {
				BlockPos.Mutable mutable = blockPos.mutableCopy();
				
				for (Direction direction : shuffledDirections) {
					mutable.set(blockPos);
					for (int i = 0; i < config.searchRange; ++i) {
						mutable.set(blockPos, direction);
						BlockState blockState = structureWorldAccess.getBlockState(mutable);
						if (!isAirOrWater(blockState) && !blockState.isOf(config.block)) {
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
	
	public static boolean generate(StructureWorldAccess world, BlockPos pos, WallPatchFeatureConfig config, Random random) {
		BlockPos.Mutable mutable = pos.mutableCopy();
		
		BlockState posState;
		Direction direction;
		boolean success = false;
		
		for (BlockPos currPos : BlockPos.iterateOutwards(pos, config.width.get(random), config.height.get(random), config.width.get(random))) {
			if (!isAirOrWater(world.getBlockState(currPos))) {
				continue;
			}
			
			Iterator<Direction> directionIterator = config.shuffleDirections(random).iterator();
			boolean canBePlaced = false;
			do {
				direction = directionIterator.next();
				posState = world.getBlockState(mutable.set(currPos, direction));
				if (posState.isIn(config.canPlaceOn)) {
					canBePlaced = true;
				}
			} while (!canBePlaced && directionIterator.hasNext());
			
			if (!canBePlaced) {
				continue;
			}
			
			BlockState stateToPlace = config.block.getDefaultState();
			if (config.block instanceof FacingBlock) {
				stateToPlace = stateToPlace.with(Properties.FACING, direction.getOpposite());
			}
			
			if (stateToPlace != null) {
				world.setBlockState(currPos, stateToPlace, 3);
				success = true;
			}
		}
		
		return success;
	}
	
	private static boolean isAirOrWater(BlockState state) {
		return state.isAir() || state.isOf(Blocks.WATER);
	}
	
}
