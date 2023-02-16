package de.dafuqs.spectrum.deeper_down;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.util.registry.*;

public class DDPlacedFeatures extends WorldgenHelper {

    public static void register() {
        registerNoxshrooms();
    }

    private static void registerNoxshrooms() {
        registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("chestnut_noxfungus"), DDConfiguredFeatures.CHESTNUT_NOXFUNGUS);
        registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("ebony_noxfungus"), DDConfiguredFeatures.EBONY_NOXFUNGUS);
        registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("ivory_noxfungus"), DDConfiguredFeatures.IVORY_NOXFUNGUS);
        registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("slate_noxfungus"), DDConfiguredFeatures.SLATE_NOXFUNGUS);
    }


}
