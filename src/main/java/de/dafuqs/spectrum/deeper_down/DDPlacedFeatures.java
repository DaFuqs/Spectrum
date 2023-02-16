package de.dafuqs.spectrum.deeper_down;

import de.dafuqs.spectrum.*;
import net.minecraft.util.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.placementmodifier.*;

import static de.dafuqs.spectrum.helpers.WorldgenHelper.*;

public class DDPlacedFeatures {

    public static void register() {
        registerGeodes();
        registerNoxshrooms();
    }

    private static void registerNoxshrooms() {
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("chestnut_noxfungus"), DDConfiguredFeatures.CHESTNUT_NOXFUNGUS);
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("ebony_noxfungus"), DDConfiguredFeatures.EBONY_NOXFUNGUS);
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("ivory_noxfungus"), DDConfiguredFeatures.IVORY_NOXFUNGUS);
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, SpectrumCommon.locate("slate_noxfungus"), DDConfiguredFeatures.SLATE_NOXFUNGUS);
    }

    public static void registerGeodes() {
        Identifier MOONSTONE_GEODE_IDENTIFIER = SpectrumCommon.locate("moonstone_geode");
        registerConfiguredAndPlacedFeature(
                MOONSTONE_GEODE_IDENTIFIER,
                DDConfiguredFeatures.MOONSTONE_GEODE,
                RarityFilterPlacementModifier.of(SpectrumCommon.CONFIG.MoonstoneGeodeChunkChance),
                HeightRangePlacementModifier.uniform(YOffset.aboveBottom(16), YOffset.aboveBottom(128)),
                SquarePlacementModifier.of(),
                BiomePlacementModifier.of()
        );
    }

}
