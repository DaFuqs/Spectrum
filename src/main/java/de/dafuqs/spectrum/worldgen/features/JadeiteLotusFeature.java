package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.jade_vines.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.registry.tag.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;

public class JadeiteLotusFeature extends Feature<JadeiteLotusFeatureConfig> {

    public JadeiteLotusFeature(Codec<JadeiteLotusFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<JadeiteLotusFeatureConfig> context) {
        var world = context.getWorld();
        var origin = context.getOrigin();
        var random = context.getRandom();
        var chunkGen = context.getGenerator();
        var inverted = context.getConfig().inverted();
    
        var floorState = world.getBlockState(inverted ? origin.down() : origin.up());
    
        if (!(floorState.isIn(BlockTags.DIRT) || floorState.isIn(SpectrumBlockTags.BASE_STONE_DEEPER_DOWN)))
            return false;
    
        // try out how far we can grow
        // limit growth to a few blocks above the ground
        var stemHeight = Math.round(MathHelper.nextGaussian(random, 8, 10F) + 5);
        BlockPos.Mutable mutablePos = origin.mutableCopy();
        for (int i = 0; i < stemHeight + 2; i++) {
            if (inverted) { // growing up
                mutablePos.move(Direction.UP);
                if (mutablePos.getY() > chunkGen.getWorldHeight() || !isReplaceable(world, mutablePos)) {
                    stemHeight = i - 2 - random.nextInt(2);
                    break;
                }
            } else {
                mutablePos.move(Direction.DOWN);
                if (mutablePos.getY() < chunkGen.getMinimumY() || !isReplaceable(world, mutablePos)) {
                    stemHeight = i - 2 - random.nextInt(2);
                    break;
                }
            }
        }
    
        if (stemHeight < 4)
            return false;
    
        generateStem(world, origin, stemHeight, inverted);
    
        return true;
    }
    
    private static boolean isReplaceable(WorldAccess world, BlockPos pos) {
        return world.getBlockState(pos).isReplaceable();
    }
    
    private void generateStem(WorldAccess world, BlockPos origin, int stemHeight, boolean inverted) {
        var stemPointer = origin.mutableCopy();
        var topStem = false;
        
        for (int height = 0; height < stemHeight; height++) {
            if (height == 0) {
                this.setBlockState(world, stemPointer, SpectrumBlocks.JADEITE_LOTUS_STEM.getDefaultState().with(JadeiteLotusStemBlock.INVERTED, inverted));
                topStem = true;
            } else if (height == stemHeight - 1) {
                this.setBlockState(world, stemPointer, SpectrumBlocks.JADEITE_LOTUS_FLOWER.getDefaultState().with(JadeiteLotusFlowerBlock.FACING, inverted ? Direction.UP : Direction.DOWN));
            } else {
                this.setBlockState(world, stemPointer, JadeiteLotusStemBlock.getStemVariant(topStem, inverted));
                topStem = !topStem;
            }
            stemPointer.move(0, inverted ? 1 : -1, 0);
        }
    }
    
}
