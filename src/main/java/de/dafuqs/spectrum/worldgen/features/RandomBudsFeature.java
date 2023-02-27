package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import net.minecraft.block.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;

import java.util.*;

public class RandomBudsFeature extends Feature<RandomBudsFeaturesConfig> {

    public RandomBudsFeature(Codec configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext context) {
        StructureWorldAccess structureWorldAccess = context.getWorld();
        BlockPos blockPos = context.getOrigin();
        Random random = context.getRandom();
        RandomBudsFeaturesConfig randomBudsFeaturesConfig = (RandomBudsFeaturesConfig) context.getConfig();
        if (!isAirOrWater(structureWorldAccess.getBlockState(blockPos))) {
            return false;
        } else {
            List<Direction> directions = shuffleDirections(randomBudsFeaturesConfig, random);
            if (generate(structureWorldAccess, blockPos, structureWorldAccess.getBlockState(blockPos), randomBudsFeaturesConfig, random, directions)) {
                return true;
            } else {
                BlockPos.Mutable mutable = blockPos.mutableCopy();

                for (Direction direction : directions) {
                    mutable.set(blockPos);
                    List<Direction> list2 = shuffleDirections(randomBudsFeaturesConfig, random, direction.getOpposite());

                    for (int i = 0; i < randomBudsFeaturesConfig.searchRange; ++i) {
                        mutable.set(blockPos, direction);
                        BlockState blockState = structureWorldAccess.getBlockState(mutable);
                        if (!isAirOrWater(blockState) && !blockState.isOf(Blocks.GLOW_LICHEN)) {
                            break;
                        }

                        if (generate(structureWorldAccess, mutable, blockState, randomBudsFeaturesConfig, random, list2)) {
                            return true;
                        }
                    }
                }

                return false;
            }
        }
    }

    public static boolean generate(StructureWorldAccess world, BlockPos pos, BlockState state, RandomBudsFeaturesConfig config, Random random, List<Direction> directions) {
        BlockPos.Mutable mutable = pos.mutableCopy();

        Iterator<Direction> var7 = directions.iterator();
        Direction direction;
        BlockState blockState;
        do {
            if (!var7.hasNext()) {
                return false;
            }

            direction = var7.next();
            blockState = world.getBlockState(mutable.set(pos, direction));
        } while (!blockState.isIn(config.canPlaceOn));

        BlockState stateToPlace = config.blocks.get(random.nextInt(config.blocks.size())).getDefaultState().with(Properties.FACING, Direction.random(random));
        if (stateToPlace.canPlaceAt(world, pos)) {
            world.setBlockState(pos, stateToPlace, 3);
            world.getChunk(pos).markBlockForPostProcessing(pos);
            return true;
        }
        return false;
    }

    private static boolean isAirOrWater(BlockState state) {
        return state.isAir() || state.isOf(Blocks.WATER);
    }

    public static List<Direction> shuffleDirections(RandomBudsFeaturesConfig config, Random random) {
        return Util.copyShuffled(config.directions.stream(), random);
    }

    public static List<Direction> shuffleDirections(RandomBudsFeaturesConfig config, Random random, Direction excluded) {
        return Util.copyShuffled(config.directions.stream().filter((direction) -> direction != excluded), random);
    }

}
