package de.dafuqs.spectrum.worldgen;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.dimension.DeeperDownSurfaceBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import static org.apache.logging.log4j.Level.INFO;

public class SpectrumSurfaceBuilders  {

    public static SurfaceBuilder<TernarySurfaceConfig> DEEPER_DOWN_SURFACE_BUILDER;


    private static <C extends SurfaceConfig, F extends SurfaceBuilder<C>> F register(String id, F surfaceBuilder) {
        return Registry.register(Registry.SURFACE_BUILDER, new Identifier(SpectrumCommon.MOD_ID, id), surfaceBuilder);
    }

    public static void register() {
        SpectrumCommon.log(INFO, "Registering the Deeper Down Surface Builder...");
        DEEPER_DOWN_SURFACE_BUILDER = register("deeper_down_surface_builder", new DeeperDownSurfaceBuilder(TernarySurfaceConfig.CODEC));
    }

}
