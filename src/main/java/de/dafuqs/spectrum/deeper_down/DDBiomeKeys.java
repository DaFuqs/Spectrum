package de.dafuqs.spectrum.deeper_down;

import de.dafuqs.spectrum.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.biome.*;

public class DDBiomeKeys {

    public static final RegistryKey<Biome> CRYSTAL_GARDENS = getReference("crystal_gardens");
    public static final RegistryKey<Biome> DEEP_BARRENS = getReference("deep_barrens");
    public static final RegistryKey<Biome> DRAGONROT_SWAMP = getReference("dragonrot_swamp");
    public static final RegistryKey<Biome> FORLORM_CAVES = getReference("forlorm_caves");
    public static final RegistryKey<Biome> RAZOR_EDGE = getReference("razor_edge");

    private static RegistryKey<Biome> getReference(String name) {
        return RegistryKey.of(Registry.BIOME_KEY, SpectrumCommon.locate(name));
    }

}
