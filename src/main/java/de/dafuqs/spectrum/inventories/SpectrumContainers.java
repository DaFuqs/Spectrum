package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;

public class SpectrumContainers {

    public static Identifier ALTAR;
    public static Identifier CRAFTING_TABLET;
    public static Identifier RESTOCKING_CHEST;

    public static void register() {
        ALTAR = new Identifier(SpectrumCommon.MOD_ID, "altar");
        CRAFTING_TABLET = new Identifier(SpectrumCommon.MOD_ID, "crafting_tablet");
        RESTOCKING_CHEST = new Identifier(SpectrumCommon.MOD_ID, "restocking_chest");
    }

}
