package de.dafuqs.spectrum.deeper_down;

import net.minecraft.util.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.biome.*;

public class DDBiomeKeys {

    public static final RegistryKey<Biome> CRYSTAL_GARDEN = register("crystal_garden");
    public static final RegistryKey<Biome> DEEP_BARRENS = register("deep_barrens");
    public static final RegistryKey<Biome> DRAGONROT_SWAMP = register("dragonrot_swamp");
    public static final RegistryKey<Biome> FORLORM_CAVES = register("forlorm_caves");
    public static final RegistryKey<Biome> RAZOR_EDGE = register("razor_edge");

    private static RegistryKey<Biome> register(String name) {
        return RegistryKey.of(Registry.BIOME_KEY, new Identifier(name));
    }

}
