package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;
import org.jetbrains.annotations.*;

// a version of BasaltPillarFeature with configurable block state
public class PillarFeature extends Feature<BlockStateFeatureConfig> {

    public PillarFeature(Codec<BlockStateFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<BlockStateFeatureConfig> context) {
        BlockPos blockPos = context.getOrigin();
        StructureWorldAccess structureWorldAccess = context.getWorld();
        Random random = context.getRandom();
        if (structureWorldAccess.isAir(blockPos) && !structureWorldAccess.isAir(blockPos.up())) {
            BlockState blockState = context.getConfig().blockState();

            BlockPos.Mutable mutable = blockPos.mutableCopy();
            BlockPos.Mutable mutable2 = blockPos.mutableCopy();
            boolean bl = true;
            boolean bl2 = true;
            boolean bl3 = true;
            boolean bl4 = true;

            while (structureWorldAccess.isAir(mutable)) {
                if (structureWorldAccess.isOutOfHeightLimit(mutable)) {
                    return true;
                }

                structureWorldAccess.setBlockState(mutable, blockState, 2);
                bl = bl && this.stopOrPlace(structureWorldAccess, random, mutable2.set(mutable, Direction.NORTH), blockState);
                bl2 = bl2 && this.stopOrPlace(structureWorldAccess, random, mutable2.set(mutable, Direction.SOUTH), blockState);
                bl3 = bl3 && this.stopOrPlace(structureWorldAccess, random, mutable2.set(mutable, Direction.WEST), blockState);
                bl4 = bl4 && this.stopOrPlace(structureWorldAccess, random, mutable2.set(mutable, Direction.EAST), blockState);
                mutable.move(Direction.DOWN);
            }

            mutable.move(Direction.UP);
            this.tryPlace(structureWorldAccess, random, mutable2.set(mutable, Direction.NORTH), blockState);
            this.tryPlace(structureWorldAccess, random, mutable2.set(mutable, Direction.SOUTH), blockState);
            this.tryPlace(structureWorldAccess, random, mutable2.set(mutable, Direction.WEST), blockState);
            this.tryPlace(structureWorldAccess, random, mutable2.set(mutable, Direction.EAST), blockState);
            mutable.move(Direction.DOWN);
            BlockPos.Mutable mutable3 = new BlockPos.Mutable();

            for (int x = -3; x < 4; ++x) {
                for (int z = -3; z < 4; ++z) {
                    int k = MathHelper.abs(x) * MathHelper.abs(z);
                    if (random.nextInt(10) < 10 - k) {
                        mutable3.set(mutable.add(x, 0, z));
                        int l = 3;

                        while (structureWorldAccess.isAir(mutable2.set(mutable3, Direction.DOWN))) {
                            mutable3.move(Direction.DOWN);
                            --l;
                            if (l <= 0) {
                                break;
                            }
                        }

                        if (!structureWorldAccess.isAir(mutable2.set(mutable3, Direction.DOWN))) {
                        }
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private void tryPlace(WorldAccess world, @NotNull Random random, BlockPos pos, BlockState blockState) {
        if (random.nextBoolean()) {
            world.setBlockState(pos, blockState, 2);
        }
    }

    private boolean stopOrPlace(WorldAccess world, @NotNull Random random, BlockPos pos, BlockState blockState) {
        if (random.nextInt(10) != 0) {
            world.setBlockState(pos, blockState, 2);
            return true;
        }
        return false;
    }

}
