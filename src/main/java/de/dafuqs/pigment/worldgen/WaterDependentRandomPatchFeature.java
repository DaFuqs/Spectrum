package de.dafuqs.pigment.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.placer.ColumnPlacer;

import java.util.Random;

public class WaterDependentRandomPatchFeature extends Feature<WaterDependentRandomPatchFeatureConfig> {

    public WaterDependentRandomPatchFeature(Codec<WaterDependentRandomPatchFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<WaterDependentRandomPatchFeatureConfig> context) {
        WaterDependentRandomPatchFeatureConfig randomPatchFeatureConfig = context.getConfig();
        Random random = context.getRandom();
        BlockPos blockPos = context.getOrigin();
        StructureWorldAccess structureWorldAccess = context.getWorld();

        BlockState blockState;
        if(structureWorldAccess.isWater(blockPos)) {
            blockState = randomPatchFeatureConfig.inWaterStateProvider.getBlockState(random, blockPos);
        } else {
            blockState = randomPatchFeatureConfig.outOfWaterStateProvider.getBlockState(random, blockPos);
        }

        BlockPos blockPos3;
        if (randomPatchFeatureConfig.project) {
            blockPos3 = structureWorldAccess.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, blockPos);
        } else {
            blockPos3 = blockPos;
        }

        int i = 0;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for(int j = 0; j < randomPatchFeatureConfig.tries; ++j) {
            mutable.set(blockPos3, random.nextInt(randomPatchFeatureConfig.spreadX + 1) - random.nextInt(randomPatchFeatureConfig.spreadX + 1), random.nextInt(randomPatchFeatureConfig.spreadY + 1) - random.nextInt(randomPatchFeatureConfig.spreadY + 1), random.nextInt(randomPatchFeatureConfig.spreadZ + 1) - random.nextInt(randomPatchFeatureConfig.spreadZ + 1));
            BlockPos blockPos4 = mutable.down();
            BlockState blockState2 = structureWorldAccess.getBlockState(blockPos4);
            if ((structureWorldAccess.isAir(mutable) || randomPatchFeatureConfig.canReplace && structureWorldAccess.getBlockState(mutable).getMaterial().isReplaceable()) && blockState.canPlaceAt(structureWorldAccess, mutable) && (randomPatchFeatureConfig.whitelist.isEmpty() || randomPatchFeatureConfig.whitelist.contains(blockState2.getBlock())) && !randomPatchFeatureConfig.blacklist.contains(blockState2)) {
                randomPatchFeatureConfig.blockPlacer.generate(structureWorldAccess, mutable, blockState, random);
                ++i;
            }
        }

        return i > 0;
    }
}