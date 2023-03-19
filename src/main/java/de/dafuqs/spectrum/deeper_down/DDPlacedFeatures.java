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
	
		registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("bristle_sprouts"), DDConfiguredFeatures.BRISTLE_SPROUTS);
		registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("snapping_ivy_patch"), DDConfiguredFeatures.SNAPPING_IVY_PATCH);
	
		registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("nephrite_blossom"), DDConfiguredFeatures.NEPHRITE_BLOSSOM_BULB);
		registerConfiguredFeature(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("jadeite_lotus"), DDConfiguredFeatures.JADEITE_LOTUS_BULB);
	}

}
