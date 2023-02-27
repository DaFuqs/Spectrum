package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;

import java.util.*;

public class WeightedRandomFeature extends Feature<WeightedRandomFeatureConfig> {

    public WeightedRandomFeature(Codec<WeightedRandomFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(FeatureContext<WeightedRandomFeatureConfig> context) {
        Random random = context.getRandom();
        StructureWorldAccess structureWorldAccess = context.getWorld();
        BlockPos blockPos = context.getOrigin();

        WeightedRandomFeatureConfig weightedRandomFeatureConfig = context.getConfig();
        Optional<PlacedFeature> randomPlacedFeature = weightedRandomFeatureConfig.features().getDataOrEmpty(context.getRandom());
        return randomPlacedFeature.map(placedFeature -> placedFeature.generateUnregistered(structureWorldAccess, context.getGenerator(), random, blockPos)).orElse(false);
    }

}
