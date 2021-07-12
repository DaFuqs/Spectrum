package de.dafuqs.pigment.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.GeodeFeature;
import net.minecraft.world.gen.feature.GeodeFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

/**
 * This modified geode feature prevents geodes generating 90 % in air
 * making them look really out of place
 */
public class SolidBlocksOnlyGeodeFeature extends GeodeFeature {

    public SolidBlocksOnlyGeodeFeature(Codec<GeodeFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<GeodeFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        int outerLayerDistance = (int) context.getConfig().layerThicknessConfig.outerLayer;

        int airBlocks = 0;
        if(world.isAir(origin.up(outerLayerDistance))) { airBlocks++; }
        if(world.isAir(origin.down(outerLayerDistance))) { airBlocks++; }
        if(world.isAir(origin.north(outerLayerDistance))) { airBlocks++; }
        if(world.isAir(origin.east(outerLayerDistance))) { airBlocks++; }
        if(world.isAir(origin.south(outerLayerDistance))) { airBlocks++; }
        if(world.isAir(origin.west(outerLayerDistance))) { airBlocks++; }

        if(airBlocks < 2) {
            super.generate(context);
        }

        return false;
    }

}
