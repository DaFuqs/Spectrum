package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.*;
import net.minecraft.world.biome.*;

public class SpectrumBiomeTags {
	
	public static final TagKey<Biome> DD_BIOMES = getReference("in_deeper_down");
	public static final TagKey<Biome> HAS_PERMANENT_RAIN = getReference("has_permanent_rain");
	
	public static final TagKey<Biome> COLORED_TREES_GENERATING_IN = getReference("colored_trees_generating_in");
	public static final TagKey<Biome> MERMAIDS_BRUSHES_GENERATING_IN = getReference("mermaids_brushes_generating_in");
	public static final TagKey<Biome> QUITOXIC_REEDS_GENERATING_IN = getReference("quitoxic_reeds_generating_in");
	public static final TagKey<Biome> CLOVER_GENERATING_IN = getReference("clover_generating_in");
	public static final TagKey<Biome> DRAGONBONE_FOSSILS_GENERATING_IN = getReference("dragonbone_fossils_generating_in");
	
	private static TagKey<Biome> getReference(String id) {
		return TagKey.of(RegistryKeys.BIOME, SpectrumCommon.locate(id));
	}
	
}
