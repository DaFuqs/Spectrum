package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;

public class SpectrumContainers {

    public static Identifier ALTAR;
    public static Identifier CRAFTING_TABLET;
    public static Identifier RESTOCKING_CHEST;

    public static Identifier GENERIC_9x3;
    public static Identifier GENERIC_9x6;

    public static void register() {
        ALTAR = new Identifier(SpectrumCommon.MOD_ID, "altar");
        CRAFTING_TABLET = new Identifier(SpectrumCommon.MOD_ID, "crafting_tablet");

        GENERIC_9x3 = new Identifier(SpectrumCommon.MOD_ID, "generic_9x3");
        GENERIC_9x6 = new Identifier(SpectrumCommon.MOD_ID, "generic_9x6");
    }

}
