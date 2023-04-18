package de.dafuqs.spectrum.features;

import com.mojang.serialization.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;

public class AirCheckDiskFeature extends OreFeature {

    public AirCheckDiskFeature(Codec<OreFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<OreFeatureConfig> context) {
        BlockPos blockPos = context.getOrigin();
        StructureWorldAccess structureWorldAccess = context.getWorld();

        if (structureWorldAccess.getBlockState(blockPos).isAir()) {
            return false;
        }

        return super.generate(context);
    }

}
