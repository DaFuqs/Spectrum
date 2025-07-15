package de.dafuqs.spectrum.worldgen.features;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.levelgen.feature.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * a BasaltColumnsFeature with configurable block state
 */
public class ColumnsFeature extends Feature<ColumnsFeatureConfig> {
	
	private static final ImmutableList<Block> CANNOT_REPLACE_BLOCKS = ImmutableList.of(Blocks.BEDROCK, Blocks.CHEST, Blocks.SPAWNER, SpectrumBlocks.DOWNSTONE);
	private static final int BIG_MAX_OFFSET = 5;
	private static final int BIG_COUNT = 50;
	private static final int SMALL_MAX_OFFSET = 8;
	private static final int SMALL_COUNT = 15;
	
	public ColumnsFeature(Codec<ColumnsFeatureConfig> codec) {
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<ColumnsFeatureConfig> context) {
		int i = context.chunkGenerator().getSeaLevel();
		BlockPos blockPos = context.origin();
		WorldGenLevel structureWorldAccess = context.level();
		RandomSource random = context.random();
		ColumnsFeatureConfig config = context.config();
		if (!canPlaceAt(structureWorldAccess, i, blockPos.mutable())) {
			return false;
		} else {
			int j = config.height().sample(random);
			boolean big = random.nextFloat() < 0.9F;
			int max_offset = Math.min(j, big ? BIG_MAX_OFFSET : SMALL_MAX_OFFSET);
			int count = big ? BIG_COUNT : SMALL_COUNT;
			boolean success = false;
			
			for (BlockPos blockPos2 : BlockPos.randomBetweenClosed(random, count, blockPos.getX() - max_offset, blockPos.getY(), blockPos.getZ() - max_offset, blockPos.getX() + max_offset, blockPos.getY(), blockPos.getZ() + max_offset)) {
				int m = j - blockPos2.distManhattan(blockPos);
				if (m >= 0) {
					success |= this.placeColumn(structureWorldAccess, i, blockPos2, m, config.reach().sample(random), config.blockState());
				}
			}
			
			return success;
		}
	}
	
	private boolean placeColumn(LevelAccessor world, int seaLevel, BlockPos pos, int height, int reach, BlockState blockState) {
		boolean success = false;
		Iterator<BlockPos> it = BlockPos.betweenClosed(pos.getX() - reach, pos.getY(), pos.getZ() - reach, pos.getX() + reach, pos.getY(), pos.getZ() + reach).iterator();
		
		while (true) {
			int manhattanDistanceFromOrigin;
			BlockPos currPos;
			do {
				if (!it.hasNext()) {
					return success;
				}
				
				BlockPos blockPos = it.next();
				manhattanDistanceFromOrigin = blockPos.distManhattan(pos);
				currPos = isReplaceable(world, seaLevel, blockPos) ? moveDownToGround(world, seaLevel, blockPos.mutable(), manhattanDistanceFromOrigin) : moveUpToAir(world, blockPos.mutable(), manhattanDistanceFromOrigin);
			} while (currPos == null);
			
			int j = height - manhattanDistanceFromOrigin / 2;
			for (BlockPos.MutableBlockPos mutable = currPos.mutable(); j >= 0; --j) {
				if (isReplaceable(world, seaLevel, mutable)) {
					this.setBlock(world, mutable, blockState);
					mutable.move(Direction.UP);
					success = true;
				} else {
					if (!world.getBlockState(mutable).is(blockState.getBlock())) {
						break;
					}
					mutable.move(Direction.UP);
				}
			}
		}
	}
	
	@Nullable
	private static BlockPos moveDownToGround(LevelAccessor world, int seaLevel, BlockPos.MutableBlockPos mutablePos, int distance) {
		while (mutablePos.getY() > world.getMinBuildHeight() + 1 && distance > 0) {
			--distance;
			if (canPlaceAt(world, seaLevel, mutablePos)) {
				return mutablePos;
			}
			
			mutablePos.move(Direction.DOWN);
		}
		
		return null;
	}
	
	private static boolean canPlaceAt(LevelAccessor world, int seaLevel, BlockPos.MutableBlockPos mutablePos) {
		if (!isReplaceable(world, seaLevel, mutablePos)) {
			return false;
		} else {
			BlockState blockState = world.getBlockState(mutablePos.move(Direction.DOWN));
			mutablePos.move(Direction.UP);
			return !blockState.isAir() && !blockState.is(SpectrumBlockTags.DEEPER_DOWN_FEATURE_REPLACEABLES) && !CANNOT_REPLACE_BLOCKS.contains(blockState.getBlock());
		}
	}
	
	@Nullable
	private static BlockPos moveUpToAir(LevelAccessor world, BlockPos.MutableBlockPos mutablePos, int distance) {
		while (mutablePos.getY() < world.getMaxBuildHeight() && distance > 0) {
			--distance;
			BlockState blockState = world.getBlockState(mutablePos);
			if (CANNOT_REPLACE_BLOCKS.contains(blockState.getBlock())) {
				return null;
			}
			
			if (blockState.isAir() || blockState.is(SpectrumBlockTags.DEEPER_DOWN_FEATURE_REPLACEABLES)) {
				return mutablePos;
			}
			
			mutablePos.move(Direction.UP);
		}
		
		return null;
	}
	
	private static boolean isReplaceable(LevelAccessor world, int seaLevel, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		return blockState.isAir() || blockState.is(SpectrumBlockTags.DEEPER_DOWN_FEATURE_REPLACEABLES) || !blockState.getFluidState().isEmpty() && pos.getY() <= seaLevel;
	}
	
}
