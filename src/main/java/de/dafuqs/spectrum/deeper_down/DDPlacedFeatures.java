package de.dafuqs.spectrum.deeper_down;

import de.dafuqs.spectrum.*;
import net.minecraft.util.registry.*;

public class DDPlacedFeatures {

    public static void register() {
        registerNoxshrooms();
    }

    private static void registerNoxshrooms() {
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("chestnut_noxfungus"), DDConfiguredFeatures.CHESTNUT_NOXFUNGUS);
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("ebony_noxfungus"), DDConfiguredFeatures.EBONY_NOXFUNGUS);
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("ivory_noxfungus"), DDConfiguredFeatures.IVORY_NOXFUNGUS);
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("slate_noxfungus"), DDConfiguredFeatures.SLATE_NOXFUNGUS);
    }


}
