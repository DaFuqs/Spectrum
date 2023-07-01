package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.registry.tag.*;
import net.minecraft.registry.*;
import net.minecraft.world.biome.*;

public class SpectrumBiomeTags {
	
	public static final TagKey<Biome> COLORED_TREES_GENERATING_IN = getReference("colored_trees_generating_in");
	public static final TagKey<Biome> DD_BIOMES = getReference("in_deeper_down");
	
	private static TagKey<Biome> getReference(String id) {
		return TagKey.of(RegistryKeys.BIOME, SpectrumCommon.locate(id));
	}
	
}
