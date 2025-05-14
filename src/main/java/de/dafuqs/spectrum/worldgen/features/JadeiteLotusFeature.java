package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.blocks.jade_vines.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.feature.*;

public class JadeiteLotusFeature extends Feature<JadeiteLotusFeatureConfig> {

    public JadeiteLotusFeature(Codec<JadeiteLotusFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
	public boolean place(FeaturePlaceContext<JadeiteLotusFeatureConfig> context) {
		var world = context.level();
		var origin = context.origin();
		var random = context.random();
		var chunkGen = context.chunkGenerator();
		var inverted = context.config().inverted();
		
		var floorState = world.getBlockState(inverted ? origin.below() : origin.above());
		
		if (!(floorState.is(BlockTags.DIRT) || floorState.is(SpectrumBlockTags.BASE_STONE_DEEPER_DOWN)))
            return false;
    
        // try out how far we can grow
        // limit growth to a few blocks above the ground
		var stemHeight = Math.round(Mth.normal(random, 8, 10F) + 5);
		BlockPos.MutableBlockPos mutablePos = origin.mutable();
        for (int i = 0; i < stemHeight + 2; i++) {
            if (inverted) { // growing up
                mutablePos.move(Direction.UP);
				if (mutablePos.getY() > chunkGen.getGenDepth() || !isReplaceable(world, mutablePos)) {
                    stemHeight = i - 2 - random.nextInt(2);
                    break;
                }
            } else {
                mutablePos.move(Direction.DOWN);
				if (mutablePos.getY() < chunkGen.getMinY() || !isReplaceable(world, mutablePos)) {
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
	
	private static boolean isReplaceable(LevelAccessor world, BlockPos pos) {
		return world.getBlockState(pos).canBeReplaced();
    }
	
	private void generateStem(LevelAccessor world, BlockPos origin, int stemHeight, boolean inverted) {
		var stemPointer = origin.mutable();
        var topStem = false;
        
        for (int height = 0; height < stemHeight; height++) {
            if (height == 0) {
				this.setBlock(world, stemPointer, SpectrumBlocks.JADEITE_LOTUS_STEM.defaultBlockState().setValue(JadeiteLotusStemBlock.INVERTED, inverted));
                topStem = true;
            } else if (height == stemHeight - 1) {
				this.setBlock(world, stemPointer, SpectrumBlocks.JADEITE_LOTUS_FLOWER.defaultBlockState().setValue(JadeiteLotusFlowerBlock.FACING, inverted ? Direction.UP : Direction.DOWN));
            } else {
				this.setBlock(world, stemPointer, JadeiteLotusStemBlock.getStemVariant(topStem, inverted));
                topStem = !topStem;
            }
            stemPointer.move(0, inverted ? 1 : -1, 0);
        }
    }
    
}
