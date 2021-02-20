package de.dafuqs.pigment;

import com.google.common.collect.Maps;
import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.dimension.DeeperDownBiomeProvider;
import de.dafuqs.pigment.dimension.DeeperDownChunkGenerator;
import de.dafuqs.pigment.worldgen.PigmentFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Map;
import java.util.Optional;

import static org.apache.logging.log4j.Level.INFO;

public class DeeperDownDimension {

    //public static final RegistryKey<ChunkGeneratorSettings> OVERWORLD;

    public static final Identifier DEEPER_DOWN_DIMENSION_ID = new Identifier(PigmentCommon.MOD_ID, "deeper_down"); // the dimension
    public static final RegistryKey<World> DEEPER_DOWN_NOISE_SETTINGS = RegistryKey.of(Registry.DIMENSION, DEEPER_DOWN_DIMENSION_ID); // height, default blocks, ...

    public static void register(){
        PigmentCommon.log(INFO, "Registering the Deeper Down...");
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier(PigmentCommon.MOD_ID, "deeper_down_chunk_generator"), DeeperDownChunkGenerator.CODEC);
    }

    // TODO
    // ChunkGeneratorSettings.class
   /* private static ChunkGeneratorSettings createDeeperDownSettings() {
        BlockState defaultBlock = Blocks.GRIMSTONE.getDefaultState();
        BlockState defaultFluid = Blocks.WATER.getDefaultState();
        int height = 128;

        Map<StructureFeature<?>, StructureConfig> map = Maps.newHashMap(StructuresConfig.DEFAULT_STRUCTURES);
        //map.put(PigmentFeatures.MOONSTONE_GEODE, new StructureConfig(25, 10, 34222645));

        return new ChunkGeneratorSettings(
                new StructuresConfig(false),
                        GenerationShapeConfig.create(0, height,
                new NoiseSamplingConfig(1.0D, 3.0D, 80.0D, 60.0D),
                new SlideConfig(120, 3, 0),
                new SlideConfig(320, 4, -1), 1, 2, 0.0D, 0.019921875D, false, false, false, false), defaultBlock, defaultFluid, 0, 0, 32, false, true, true);

    }*/

}
