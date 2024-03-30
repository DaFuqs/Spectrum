package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.registry.*;
import net.minecraft.world.biome.*;

public class SpectrumBiomes {
	
	public static final RegistryKey<Biome> BLACK_LANGAST = getKey("black_langast");
	public static final RegistryKey<Biome> CRYSTAL_GARDENS = getKey("crystal_gardens");
	public static final RegistryKey<Biome> DEEP_BARRENS = getKey("deep_barrens");
	public static final RegistryKey<Biome> DEEP_DRIPSTONE_CAVES = getKey("deep_dripstone_caves");
	public static final RegistryKey<Biome> HOWLING_SPIRES = getKey("howling_spires");
	public static final RegistryKey<Biome> DRAGONROT_SWAMP = getKey("dragonrot_swamp");
	public static final RegistryKey<Biome> NOXSHROOM_FOREST = getKey("noxshroom_forest");
	public static final RegistryKey<Biome> RAZOR_EDGE = getKey("razor_edge");
	
	private static RegistryKey<Biome> getKey(String name) {
		return RegistryKey.of(RegistryKeys.BIOME, SpectrumCommon.locate(name));
	}
	
	public static void register() {
	
	}
	
}
