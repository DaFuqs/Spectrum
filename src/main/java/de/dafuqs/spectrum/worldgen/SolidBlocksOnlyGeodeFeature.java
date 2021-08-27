package de.dafuqs.spectrum.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.GeodeFeature;
import net.minecraft.world.gen.feature.GeodeFeatureConfig;
import net.minecraft.world.gen.feature.RandomPatchFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;

/**
 * This modified geode feature prevents geodes generating in air
 * preventing them look really out of place
 */
public class SolidBlocksOnlyGeodeFeature extends GeodeFeature {

    public SolidBlocksOnlyGeodeFeature(Codec<GeodeFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<GeodeFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        int outerLayerDistance = (int) context.getConfig().layerThicknessConfig.outerLayer + 1;
        int innerLayerDistance = (int) context.getConfig().layerThicknessConfig.middleLayer + 1;

        int solidBlocks = 0;
        if(world.getBlockState(origin.up(innerLayerDistance)).isSolidBlock(world, origin.up(innerLayerDistance))) { solidBlocks++; }
        if(world.getBlockState(origin.up(outerLayerDistance)).isSolidBlock(world, origin.up(outerLayerDistance))) { solidBlocks++; }
        if(world.getBlockState(origin.down(outerLayerDistance)).isSolidBlock(world, origin.down(outerLayerDistance))) { solidBlocks++; }
        if(world.getBlockState(origin.north(outerLayerDistance)).isSolidBlock(world, origin.north(outerLayerDistance))) { solidBlocks++; }
        if(world.getBlockState(origin.east(outerLayerDistance)).isSolidBlock(world, origin.east(outerLayerDistance))) { solidBlocks++; }
        if(world.getBlockState(origin.south(outerLayerDistance)).isSolidBlock(world, origin.south(outerLayerDistance))) { solidBlocks++; }
        if(world.getBlockState(origin.west(outerLayerDistance)).isSolidBlock(world, origin.west(outerLayerDistance))) { solidBlocks++; }

        if(solidBlocks > 5) {
            super.generate(context);
        }

        return false;
    }

}
