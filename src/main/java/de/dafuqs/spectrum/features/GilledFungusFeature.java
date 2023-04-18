package de.dafuqs.spectrum.features;

import com.mojang.serialization.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;

public class GilledFungusFeature extends Feature<GilledFungusFeatureConfig> {

    public GilledFungusFeature(Codec<GilledFungusFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<GilledFungusFeatureConfig> context) {
        StructureWorldAccess structureWorldAccess = context.getWorld();
        BlockPos blockPos = context.getOrigin();
        GilledFungusFeatureConfig hugeFungusFeatureConfig = context.getConfig();
        Block validBaseBlock = hugeFungusFeatureConfig.validBaseBlock().getBlock();
        BlockState baseBlock = structureWorldAccess.getBlockState(blockPos.down());

        if (!baseBlock.isOf(validBaseBlock)) {
            return false;
        }

        Random random = context.getRandom();
        ChunkGenerator chunkGenerator = context.getGenerator();

        int stemHeight = MathHelper.nextInt(random, 4, 9);
        if (random.nextInt(12) == 0) {
            stemHeight *= 2;
        }
        if (blockPos.getY() + stemHeight + 1 >= chunkGenerator.getWorldHeight()) {
            return false;
        }

        structureWorldAccess.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 4);
        this.generateStem(structureWorldAccess, hugeFungusFeatureConfig, blockPos, stemHeight);
        this.generateHat(structureWorldAccess, random, hugeFungusFeatureConfig, blockPos, stemHeight);
        return true;
    }

    private static boolean isReplaceable(WorldAccess world, BlockPos pos, boolean replacePlants) {
        return world.testBlockState(pos, (state) -> {
            Material material = state.getMaterial();
            return state.getMaterial().isReplaceable() || replacePlants && material == Material.PLANT;
        });
    }

    private void generateStem(WorldAccess world, GilledFungusFeatureConfig config, BlockPos pos, int stemHeight) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        BlockState blockState = config.stemState();
        int i = 0;
        for (int x = -i; x <= i; ++x) {
            for (int z = -i; z <= i; ++z) {
                for (int y = 0; y < stemHeight; ++y) {
                    mutable.set(pos, x, y, z);
                    if (isReplaceable(world, mutable, true)) {
                        this.setBlockState(world, mutable, blockState);
                    }
                }
            }
        }
    }

    private void generateHat(WorldAccess world, Random random, GilledFungusFeatureConfig config, BlockPos pos, int stemHeight) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int hatWidth = Math.min(random.nextInt(2 + stemHeight / 4) + 3, 4);
        int currentHatWidth = hatWidth;
        int outerThreshold = hatWidth / 2;

        for (int y = 0; y <= hatWidth; ++y) {
            for (int x = -currentHatWidth; x <= currentHatWidth; ++x) {
                for (int z = -currentHatWidth; z <= currentHatWidth; ++z) {

                    boolean isCorner = Math.abs(x) == currentHatWidth && Math.abs(z) == currentHatWidth;
                    if (isCorner) {
                        continue;
                    }

                    mutable.set(pos, x, stemHeight + y, z);
                    if (isReplaceable(world, mutable, false)) {
                        boolean isInnerCorner = Math.abs(x) == currentHatWidth - 1 && Math.abs(z) == currentHatWidth - 1;
                        boolean isInner = Math.abs(x) < currentHatWidth && Math.abs(z) < currentHatWidth;
                        boolean isLowestLevel = y == 0;

                        if (x == 0 && z == 0) {
                            this.setBlockState(world, mutable, currentHatWidth < 2 ? config.capState() : config.stemState());
                        } else if (isInner && !isInnerCorner) {
                            if (!isLowestLevel || Math.abs(x) > outerThreshold || Math.abs(z) > outerThreshold) {
                                BlockState gillsState = config.gillsState().with(PillarBlock.AXIS, Math.abs(x) < Math.abs(z) ? Direction.Axis.X : Direction.Axis.Z);
                                this.setBlockState(world, mutable, gillsState);
                            }
                        } else {
                            this.setBlockState(world, mutable, config.capState());
                        }
                    }
                }
            }
            currentHatWidth -= 1;
        }
    }

}
