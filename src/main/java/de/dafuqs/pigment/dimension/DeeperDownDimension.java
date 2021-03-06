package de.dafuqs.pigment.dimension;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.worldgen.PigmentSurfaceBuilders;
import net.minecraft.util.Identifier;

import static org.apache.logging.log4j.Level.INFO;

public class DeeperDownDimension {

    public static final Identifier DEEPER_DOWN_DIMENSION_ID = new Identifier(PigmentCommon.MOD_ID, "deeper_down_dimension"); // the dimension

    public static void setup() {
        PigmentCommon.log(INFO, "Registering the Deeper Down dimension...");
        DeeperDownBiomeSource.register();
        PigmentSurfaceBuilders.register();
    }

}
