package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import net.minecraft.block.*;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;
import org.apache.commons.lang3.mutable.*;

import java.util.*;

/**
 * Similar to FossilFeature, but does not bury itself into the ground
 */
public class ExposedFossilFeature extends Feature<FossilFeatureConfig> {
    
    public ExposedFossilFeature(Codec<FossilFeatureConfig> codec) {
        super(codec);
    }
    
    public boolean generate(FeatureContext<FossilFeatureConfig> context) {
        Random random = context.getRandom();
        StructureWorldAccess structureWorldAccess = context.getWorld();
        BlockPos origin = context.getOrigin();
        BlockRotation blockRotation = BlockRotation.random(random);
        FossilFeatureConfig fossilFeatureConfig = context.getConfig();
        int fossilStructuresCount = random.nextInt(fossilFeatureConfig.fossilStructures.size());
        StructureTemplateManager structureTemplateManager = structureWorldAccess.toServerWorld().getServer().getStructureTemplateManager();
        StructureTemplate structureTemplate = structureTemplateManager.getTemplateOrBlank(fossilFeatureConfig.fossilStructures.get(fossilStructuresCount));
        StructureTemplate structureTemplate2 = structureTemplateManager.getTemplateOrBlank(fossilFeatureConfig.overlayStructures.get(fossilStructuresCount));
        ChunkPos originChunkPos = new ChunkPos(origin);
        BlockBox blockBox = new BlockBox(originChunkPos.getStartX() - 16, structureWorldAccess.getBottomY(), originChunkPos.getStartZ() - 16, originChunkPos.getEndX() + 16, structureWorldAccess.getTopY(), originChunkPos.getEndZ() + 16);
        StructurePlacementData structurePlacementData = (new StructurePlacementData()).setRotation(blockRotation).setBoundingBox(blockBox).setRandom(random);
        Vec3i rotatedSize = structureTemplate.getRotatedSize(blockRotation);
        BlockPos afterOffsetPos = origin.add(-rotatedSize.getX() / 2, 0, -rotatedSize.getZ() / 2);
        
        BlockPos transformedPos = structureTemplate.offsetByTransformedSize(afterOffsetPos, BlockMirror.NONE, blockRotation);
        if (getEmptyCorners(structureWorldAccess, structureTemplate.calculateBoundingBox(structurePlacementData, transformedPos)) > fossilFeatureConfig.maxEmptyCorners) {
            return false;
        } else {
            structurePlacementData.clearProcessors();
            List<StructureProcessor> processors = (fossilFeatureConfig.fossilProcessors.value()).getList();
            Objects.requireNonNull(structurePlacementData);
            processors.forEach(structurePlacementData::addProcessor);
            structureTemplate.place(structureWorldAccess, transformedPos, transformedPos, structurePlacementData, random, 4);
            structurePlacementData.clearProcessors();
            processors = (fossilFeatureConfig.overlayProcessors.value()).getList();
            Objects.requireNonNull(structurePlacementData);
            processors.forEach(structurePlacementData::addProcessor);
            structureTemplate2.place(structureWorldAccess, transformedPos, transformedPos, structurePlacementData, random, 4);
            return true;
        }
    }
    
    private static int getEmptyCorners(StructureWorldAccess world, BlockBox box) {
        MutableInt mutableInt = new MutableInt(0);
        box.forEachVertex((pos) -> {
            BlockState blockState = world.getBlockState(pos);
            if (blockState.isAir() || blockState.getBlock() instanceof FluidBlock) {
                mutableInt.add(1);
            }
            
        });
        return mutableInt.getValue();
    }
    
}
