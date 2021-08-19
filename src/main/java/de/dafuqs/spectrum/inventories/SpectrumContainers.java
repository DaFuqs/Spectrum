package de.dafuqs.spectrum.inventories;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.util.Identifier;

public class SpectrumContainers {

    public static Identifier PEDESTAL;
    public static Identifier CRAFTING_TABLET;
    public static Identifier RESTOCKING_CHEST;
    public static Identifier BEDROCK_ANVIL;

    public static Identifier GENERIC_9x3;
    public static Identifier GENERIC_9x6;

    public static void register() {
        PEDESTAL = new Identifier(SpectrumCommon.MOD_ID, "pedestal");
        CRAFTING_TABLET = new Identifier(SpectrumCommon.MOD_ID, "crafting_tablet");
        RESTOCKING_CHEST = new Identifier(SpectrumCommon.MOD_ID, "restocking_chest");
        BEDROCK_ANVIL = new Identifier(SpectrumCommon.MOD_ID, "bedrock_anvil");

        GENERIC_9x3 = new Identifier(SpectrumCommon.MOD_ID, "generic_9x3");
        GENERIC_9x6 = new Identifier(SpectrumCommon.MOD_ID, "generic_9x6");
    }

}
