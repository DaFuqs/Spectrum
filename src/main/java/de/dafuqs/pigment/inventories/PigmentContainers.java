package de.dafuqs.pigment.inventories;

import de.dafuqs.pigment.PigmentCommon;
import net.minecraft.util.Identifier;

public class PigmentContainers {

    public static Identifier ALTAR;

    public static void register() {
        ALTAR = new Identifier(PigmentCommon.MOD_ID, "altar");
    }

}
