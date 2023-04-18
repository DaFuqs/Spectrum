package de.dafuqs.spectrum.features;

import com.mojang.serialization.*;
import net.minecraft.block.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;

public class SolidBlockCheckGeodeFeature extends GeodeFeature {

    private static final int MAX_NON_SOLID_BLOCKS = 3;

    public SolidBlockCheckGeodeFeature(Codec<GeodeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<GeodeFeatureConfig> context) {
        int airBlocks = 0;

        StructureWorldAccess world = context.getWorld();
        BlockPos sourcePos = context.getOrigin();
        int distance = (int) context.getConfig().layerThicknessConfig.outerLayer;
        for (Direction direction : Direction.values()) {
            BlockPos offsetPos = sourcePos.offset(direction, distance);
            BlockState blockStateAtPos = world.getBlockState(offsetPos);

            if (blockStateAtPos.isAir() || !blockStateAtPos.isFullCube(world, offsetPos)) {
                airBlocks++;
                if (airBlocks > MAX_NON_SOLID_BLOCKS) {
                    return false;
                }
            }
        }

        // one additional check double as high to prevent them sticking out of the ground a bit more often
        BlockPos upperPos = sourcePos.up(distance + 4);
        BlockState blockStateAtPos = world.getBlockState(upperPos);
        if (blockStateAtPos.isAir() || !blockStateAtPos.isFullCube(world, upperPos)) {
            airBlocks++;
            if (airBlocks > MAX_NON_SOLID_BLOCKS) {
                return false;
            }
        }

        return super.generate(context);
    }

}
