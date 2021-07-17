package de.dafuqs.pigment.worldgen;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.dimension.DeeperDownSurfaceBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import static org.apache.logging.log4j.Level.INFO;

public class PigmentSurfaceBuilders  {

    public static SurfaceBuilder<TernarySurfaceConfig> DEEPER_DOWN_SURFACE_BUILDER;


    private static <C extends SurfaceConfig, F extends SurfaceBuilder<C>> F register(String id, F surfaceBuilder) {
        return Registry.register(Registry.SURFACE_BUILDER, new Identifier(PigmentCommon.MOD_ID, id), surfaceBuilder);
    }

    public static void register() {
        PigmentCommon.log(INFO, "Registering the Deeper Down Surface Builder...");
        DEEPER_DOWN_SURFACE_BUILDER = register("deeper_down_surface_builder", new DeeperDownSurfaceBuilder(TernarySurfaceConfig.CODEC));
    }

}
