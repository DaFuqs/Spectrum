package de.dafuqs.spectrum.worldgen.features;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;
import org.jetbrains.annotations.*;

import java.util.*;


// a version of BasaltColumnsFeature with configurable block state
public class ColumnsFeature extends Feature<ColumnsFeatureConfig> {

    private static final ImmutableList<Block> CANNOT_REPLACE_BLOCKS = ImmutableList.of(Blocks.LAVA, Blocks.BEDROCK, Blocks.MAGMA_BLOCK, Blocks.SOUL_SAND, Blocks.NETHER_BRICKS, Blocks.NETHER_BRICK_FENCE, Blocks.NETHER_BRICK_STAIRS, Blocks.NETHER_WART, Blocks.CHEST, Blocks.SPAWNER);
    private static final int field_31495 = 5;
    private static final int field_31496 = 50;
    private static final int field_31497 = 8;
    private static final int field_31498 = 15;

    public ColumnsFeature(Codec<ColumnsFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<ColumnsFeatureConfig> context) {
        int i = context.getGenerator().getSeaLevel();
        BlockPos blockPos = context.getOrigin();
        StructureWorldAccess structureWorldAccess = context.getWorld();
        Random random = context.getRandom();
        ColumnsFeatureConfig config = context.getConfig();
        if (!canPlaceAt(structureWorldAccess, i, blockPos.mutableCopy())) {
            return false;
        } else {
            int j = config.getHeight().get(random);
            boolean bl = random.nextFloat() < 0.9F;
            int k = Math.min(j, bl ? field_31495 : field_31497);
            int l = bl ? field_31496 : field_31498;
            boolean bl2 = false;

            for (BlockPos blockPos2 : BlockPos.iterateRandomly(random, l, blockPos.getX() - k, blockPos.getY(), blockPos.getZ() - k, blockPos.getX() + k, blockPos.getY(), blockPos.getZ() + k)) {
                int m = j - blockPos2.getManhattanDistance(blockPos);
                if (m >= 0) {
                    bl2 |= this.placeColumn(structureWorldAccess, i, blockPos2, m, config.getReach().get(random), config.getBlockState());
                }
            }

            return bl2;
        }
    }

    private boolean placeColumn(WorldAccess world, int seaLevel, BlockPos pos, int height, int reach, BlockState blockState) {
        boolean bl = false;
        Iterator<BlockPos> var7 = BlockPos.iterate(pos.getX() - reach, pos.getY(), pos.getZ() - reach, pos.getX() + reach, pos.getY(), pos.getZ() + reach).iterator();

        while (true) {
            int i;
            BlockPos blockPos2;
            do {
                if (!var7.hasNext()) {
                    return bl;
                }

                BlockPos blockPos = var7.next();
                i = blockPos.getManhattanDistance(pos);
                blockPos2 = isAirOrFluid(world, seaLevel, blockPos) ? moveDownToGround(world, seaLevel, blockPos.mutableCopy(), i) : moveUpToAir(world, blockPos.mutableCopy(), i);
            } while (blockPos2 == null);

            int j = height - i / 2;

            for (BlockPos.Mutable mutable = blockPos2.mutableCopy(); j >= 0; --j) {
                if (isAirOrFluid(world, seaLevel, mutable)) {
                    this.setBlockState(world, mutable, blockState);
                    mutable.move(Direction.UP);
                    bl = true;
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
        if (!isAirOrFluid(world, seaLevel, mutablePos)) {
            return false;
        } else {
            BlockState blockState = world.getBlockState(mutablePos.move(Direction.DOWN));
            mutablePos.move(Direction.UP);
            return !blockState.isAir() && !CANNOT_REPLACE_BLOCKS.contains(blockState.getBlock());
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

            if (blockState.isAir()) {
                return mutablePos;
            }

            mutablePos.move(Direction.UP);
        }

        return null;
    }

    private static boolean isAirOrFluid(WorldAccess world, int seaLevel, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.isAir() || !blockState.getFluidState().isEmpty() && pos.getY() <= seaLevel;
    }

}
