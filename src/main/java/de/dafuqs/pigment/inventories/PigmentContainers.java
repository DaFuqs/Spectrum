package de.dafuqs.pigment.inventories;

import de.dafuqs.pigment.PigmentCommon;
import net.minecraft.util.Identifier;

public class PigmentContainers {

    public static Identifier ALTAR;
    public static Identifier CRAFTING_TABLET;

    public static void register() {
        ALTAR = new Identifier(PigmentCommon.MOD_ID, "altar");
        CRAFTING_TABLET = new Identifier(PigmentCommon.MOD_ID, "crafting_tablet");
    }

}
