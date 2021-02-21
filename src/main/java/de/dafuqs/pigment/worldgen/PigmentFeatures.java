package de.dafuqs.pigment.worldgen;

import com.google.common.collect.ImmutableList;
import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.PigmentBlocks;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import static net.minecraft.world.gen.feature.ConfiguredFeatures.OAK;

public class PigmentFeatures {

    public static Feature<GeodeFeatureConfig> SOLID_BLOCKS_GEODE;

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
        return Registry.register(Registry.FEATURE, new Identifier(PigmentCommon.MOD_ID, name), feature);
    }

    public static void register() {
        SOLID_BLOCKS_GEODE = register("solid_blocks_geode", new SolidBlocksOnlyGeodeFeature(GeodeFeatureConfig.CODEC));;
    }

}
