package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.biome.*;

@SuppressWarnings("unused")
public class SpectrumBiomes {
	
	public static final ResourceKey<Biome> BLACK_LANGAST = getKey("black_langast");
	public static final ResourceKey<Biome> CRYSTAL_GARDENS = getKey("crystal_gardens");
	public static final ResourceKey<Biome> DEEP_BARRENS = getKey("deep_barrens");
	public static final ResourceKey<Biome> DEEP_DRIPSTONE_CAVES = getKey("deep_dripstone_caves");
	public static final ResourceKey<Biome> HOWLING_SPIRES = getKey("howling_spires");
	public static final ResourceKey<Biome> DRAGONROT_SWAMP = getKey("dragonrot_swamp");
	public static final ResourceKey<Biome> NOXSHROOM_FOREST = getKey("noxshroom_forest");
	public static final ResourceKey<Biome> RAZOR_EDGE = getKey("razor_edge");
	
	private static ResourceKey<Biome> getKey(String name) {
		return ResourceKey.create(Registries.BIOME, SpectrumCommon.locate(name));
	}
	
	public static void register() {
	
	}
	
}
