package de.dafuqs.spectrum.deeper_down;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.util.registry.*;

public class DDPlacedFeatures extends WorldgenHelper {

    public static void register() {
        registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("chestnut_noxfungus"), DDConfiguredFeatures.CHESTNUT_NOXFUNGUS);
        registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("ebony_noxfungus"), DDConfiguredFeatures.EBONY_NOXFUNGUS);
        registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("ivory_noxfungus"), DDConfiguredFeatures.IVORY_NOXFUNGUS);
        registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("slate_noxfungus"), DDConfiguredFeatures.SLATE_NOXFUNGUS);

        registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("green_dragonjags"), DDConfiguredFeatures.GREEN_DRAGONJAGS);
        registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("red_dragonjags"), DDConfiguredFeatures.RED_DRAGONJAGS);
        registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("purple_dragonjags"), DDConfiguredFeatures.PURPLE_DRAGONJAGS);
        registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("pink_dragonjags"), DDConfiguredFeatures.PINK_DRAGONJAGS);
        registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("black_dragonjags"), DDConfiguredFeatures.BLACK_DRAGONJAGS);
    }

}
