package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;

public class SpectrumContainers {

    public static Identifier ALTAR;

    public static void register() {
        ALTAR = new Identifier(SpectrumCommon.MOD_ID, "altar");
    }

}
