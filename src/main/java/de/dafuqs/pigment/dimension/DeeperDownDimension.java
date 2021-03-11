package de.dafuqs.pigment.dimension;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.worldgen.PigmentSurfaceBuilders;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import static org.apache.logging.log4j.Level.INFO;

public class DeeperDownDimension {

    public static final Identifier DEEPER_DOWN_DIMENSION_ID = new Identifier(PigmentCommon.MOD_ID, "deeper_down_dimension"); // the dimension
    public static final RegistryKey<World> DEEPER_DOWN_DIMENSION_KEY = RegistryKey.of(Registry.DIMENSION, new Identifier("deeper_down_dimension"));

    public static void setup() {
        PigmentCommon.log(INFO, "Registering the Deeper Down dimension...");
        DeeperDownBiomeSource.register();
        PigmentSurfaceBuilders.register();
    }

}
