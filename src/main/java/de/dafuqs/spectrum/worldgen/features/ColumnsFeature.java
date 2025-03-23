package de.dafuqs.spectrum.worldgen.features;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.SpectrumBlockTags;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;
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
	public boolean generate(FeatureContext<ColumnsFeatureConfig> context) {
		int i = context.getGenerator().getSeaLevel();
		BlockPos blockPos = context.getOrigin();
		StructureWorldAccess structureWorldAccess = context.getWorld();
		Random random = context.getRandom();
		ColumnsFeatureConfig config = context.getConfig();
		if (!canPlaceAt(structureWorldAccess, i, blockPos.mutableCopy())) {
			return false;
		} else {
			int j = config.height().get(random);
			boolean big = random.nextFloat() < 0.9F;
			int max_offset = Math.min(j, big ? BIG_MAX_OFFSET : SMALL_MAX_OFFSET);
			int count = big ? BIG_COUNT : SMALL_COUNT;
			boolean success = false;
			
			for (BlockPos blockPos2 : BlockPos.iterateRandomly(random, count, blockPos.getX() - max_offset, blockPos.getY(), blockPos.getZ() - max_offset, blockPos.getX() + max_offset, blockPos.getY(), blockPos.getZ() + max_offset)) {
                int m = j - blockPos2.getManhattanDistance(blockPos);
                if (m >= 0) {
					success |= this.placeColumn(structureWorldAccess, i, blockPos2, m, config.reach().get(random), config.blockState());
                }
            }
			
			return success;
        }
    }

    private boolean placeColumn(WorldAccess world, int seaLevel, BlockPos pos, int height, int reach, BlockState blockState) {
		boolean success = false;
		Iterator<BlockPos> it = BlockPos.iterate(pos.getX() - reach, pos.getY(), pos.getZ() - reach, pos.getX() + reach, pos.getY(), pos.getZ() + reach).iterator();

        while (true) {
			int manhattanDistanceFromOrigin;
			BlockPos currPos;
            do {
				if (!it.hasNext()) {
					return success;
                }
				
				BlockPos blockPos = it.next();
				manhattanDistanceFromOrigin = blockPos.getManhattanDistance(pos);
				currPos = isReplaceable(world, seaLevel, blockPos) ? moveDownToGround(world, seaLevel, blockPos.mutableCopy(), manhattanDistanceFromOrigin) : moveUpToAir(world, blockPos.mutableCopy(), manhattanDistanceFromOrigin);
			} while (currPos == null);
			
			int j = height - manhattanDistanceFromOrigin / 2;
			for (BlockPos.Mutable mutable = currPos.mutableCopy(); j >= 0; --j) {
                if (isReplaceable(world, seaLevel, mutable)) {
                    this.setBlockState(world, mutable, blockState);
                    mutable.move(Direction.UP);
					success = true;
                } else {
                    if (!world.getBlockState(mutable).isOf(blockState.getBlock())) {
                        break;
                    }
                    mutable.move(Direction.UP);
                }
            }
        }
    }

    @Nullable
    private static BlockPos moveDownToGround(WorldAccess world, int seaLevel, BlockPos.Mutable mutablePos, int distance) {
        while (mutablePos.getY() > world.getBottomY() + 1 && distance > 0) {
            --distance;
            if (canPlaceAt(world, seaLevel, mutablePos)) {
                return mutablePos;
            }

            mutablePos.move(Direction.DOWN);
        }

        return null;
    }

    private static boolean canPlaceAt(WorldAccess world, int seaLevel, BlockPos.Mutable mutablePos) {
        if (!isReplaceable(world, seaLevel, mutablePos)) {
            return false;
        } else {
            BlockState blockState = world.getBlockState(mutablePos.move(Direction.DOWN));
            mutablePos.move(Direction.UP);
            return !blockState.isAir() && !blockState.isIn(SpectrumBlockTags.DEEPER_DOWN_FEATURE_REPLACEABLES) && !CANNOT_REPLACE_BLOCKS.contains(blockState.getBlock());
        }
    }

    @Nullable
    private static BlockPos moveUpToAir(WorldAccess world, BlockPos.Mutable mutablePos, int distance) {
        while (mutablePos.getY() < world.getTopY() && distance > 0) {
            --distance;
            BlockState blockState = world.getBlockState(mutablePos);
            if (CANNOT_REPLACE_BLOCKS.contains(blockState.getBlock())) {
                return null;
            }

            if (blockState.isAir() || blockState.isIn(SpectrumBlockTags.DEEPER_DOWN_FEATURE_REPLACEABLES)) {
                return mutablePos;
            }

            mutablePos.move(Direction.UP);
        }

        return null;
    }

    private static boolean isReplaceable(WorldAccess world, int seaLevel, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.isAir() || blockState.isIn(SpectrumBlockTags.DEEPER_DOWN_FEATURE_REPLACEABLES) ||!blockState.getFluidState().isEmpty() && pos.getY() <= seaLevel;
    }

}
