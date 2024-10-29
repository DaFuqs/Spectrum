package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.deeper_down.flora.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.registry.tag.*;
import net.minecraft.state.property.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;

public class TriStateVineFeature extends Feature<TriStateVineFeatureConfig> {

    public TriStateVineFeature(Codec<TriStateVineFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<TriStateVineFeatureConfig> context) {
        var world = context.getWorld();
        var origin = context.getOrigin();
        var random = context.getRandom();
        var chunkGen = context.getGenerator();
        var config = context.getConfig();

        var floorState = world.getBlockState(origin.up());
    
        if (!(floorState.isIn(BlockTags.DIRT) || floorState.isIn(SpectrumBlockTags.BASE_STONE_DEEPER_DOWN)))
            return false;

        var berryChance = config.berryChance();
        var vineBlock = config.vineBlock();

        if (!(vineBlock instanceof TriStateVineBlock))
            throw new IllegalStateException("TriStateVineFeatures must use TriStateVineBlocks!");

        if (berryChance > 0 && !vineBlock.getDefaultState().contains(Properties.BERRIES))
            throw new IllegalStateException("Attempted to generate fruits for a vine with no fruiting state!");

        var minHeight = config.minHeight().get(random);
        var overgrowth = config.overgrowth().get(random);

        // try out how far we can grow
        var stemHeight = 0;
        BlockPos.Mutable mutablePos = origin.mutableCopy();
        while (stemHeight < minHeight * 3) {
            mutablePos.move(Direction.DOWN);

            if (mutablePos.getY() < chunkGen.getMinimumY() || !isReplaceable(world, mutablePos))
                break;

            if (stemHeight > minHeight && random.nextFloat() > overgrowth)
                break;

            stemHeight++;
        }

        if (stemHeight <= config.cutoff())
            return false;
    
        generateStem(world, random, origin, vineBlock, stemHeight, berryChance);
        return true;
    }
    
    private static boolean isReplaceable(WorldAccess world, BlockPos pos) {
        return world.getBlockState(pos).isAir();
    }
    
    private void generateStem(WorldAccess world, Random random, BlockPos origin, Block vineBlock, int stemHeight, float berryChance) {
        var stemPointer = origin.mutableCopy();
        var stemState = vineBlock.getDefaultState().with(TriStateVineBlock.LIFE_STAGE, TriStateVineBlock.LifeStage.STALK);

        for (int height = 0; height <= stemHeight; height++) {
            if (height == stemHeight) {
                if (berryChance > 0 && random.nextFloat() <= berryChance) {
                    this.setBlockState(world, stemPointer, stemState.with(TriStateVineBlock.LIFE_STAGE, TriStateVineBlock.LifeStage.MATURE).with(Properties.BERRIES, true));
                }
                else {
                    this.setBlockState(world, stemPointer, stemState.with(TriStateVineBlock.LIFE_STAGE, TriStateVineBlock.LifeStage.MATURE));
                }
            }
            else {
                if (berryChance > 0 && random.nextFloat() <= berryChance) {
                    this.setBlockState(world, stemPointer, stemState.with(Properties.BERRIES, true));
                }
                else {
                    this.setBlockState(world, stemPointer, stemState);
                }
            }

            stemPointer.move(Direction.DOWN);
        }
    }
    
}
