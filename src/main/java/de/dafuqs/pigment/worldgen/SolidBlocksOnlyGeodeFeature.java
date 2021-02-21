package de.dafuqs.pigment.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.GeodeFeature;
import net.minecraft.world.gen.feature.GeodeFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SolidBlocksOnlyGeodeFeature extends GeodeFeature {

    public SolidBlocksOnlyGeodeFeature(Codec<GeodeFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<GeodeFeatureConfig> context) {
        if(isPositionInStone(context)) {
            return super.generate(context);
        } else {
            return false;
        }
    }

    private boolean isPositionInStone(FeatureContext<GeodeFeatureConfig> context) {
        BlockPos blockPos = context.getOrigin();
        StructureWorldAccess structureWorldAccess = context.getWorld();

        return !structureWorldAccess.getBlockState(blockPos).isAir() &&
                !structureWorldAccess.getBlockState(blockPos.add(-5, -5, -5)).isAir() &&
                !structureWorldAccess.getBlockState(blockPos.add(5, 5, 5)).isAir() &&
                !structureWorldAccess.getBlockState(blockPos.add(-5, -5, 5)).isAir() &&
                !structureWorldAccess.getBlockState(blockPos.add(5, -5, -5)).isAir();
    }

}
