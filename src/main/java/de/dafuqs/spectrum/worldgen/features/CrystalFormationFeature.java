package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;
import net.minecraft.world.gen.stateprovider.*;

public class CrystalFormationFeature extends Feature<CrystalFormationFeatureFeatureConfig> {

    public CrystalFormationFeature(Codec<CrystalFormationFeatureFeatureConfig> configCodec) {
        super(configCodec);
    }

    public boolean generate(FeatureContext<CrystalFormationFeatureFeatureConfig> context) {
        StructureWorldAccess structureWorldAccess = context.getWorld();
        BlockPos blockPos = context.getOrigin();
        Random random = context.getRandom();
        if (!structureWorldAccess.isAir(blockPos)) {
            return false;
        } else {
            CrystalFormationFeatureFeatureConfig config = context.getConfig();
            if (!structureWorldAccess.getBlockState(blockPos.up()).isIn(config.canStartOnBlocks) && !structureWorldAccess.getBlockState(blockPos.down()).isIn(config.canStartOnBlocks)) {
                return false;
            } else {
                BlockStateProvider stateProvider = config.blockStateProvider;
                int iterations = config.iterationCountProvider.get(random);

                structureWorldAccess.setBlockState(blockPos, stateProvider.getBlockState(random, blockPos), 2);

                for (int i = 0; i < iterations; ++i) {
                    BlockPos offsetPos = blockPos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(8) - random.nextInt(8), random.nextInt(8) - random.nextInt(8));
                    if (structureWorldAccess.getBlockState(offsetPos).isAir()) {
                        int directionTries = 0;
                        for (Direction direction : Direction.values()) {
                            if (structureWorldAccess.getBlockState(offsetPos.offset(direction)).isIn(config.canExtendOnBlocks)) {
                                ++directionTries;
                            }
                            if (directionTries > 1) {
                                break;
                            }
                        }
                        if (directionTries == 1) {
                            structureWorldAccess.setBlockState(offsetPos, stateProvider.getBlockState(random, blockPos), 2);
                        }
                    }
                }

                return true;
            }
        }
    }

}
