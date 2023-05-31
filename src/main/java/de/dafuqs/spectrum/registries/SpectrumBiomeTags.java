package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.tag.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.biome.*;

public class SpectrumBiomeTags {
	
	public static final TagKey<Biome> COLORED_TREES_GENERATING_IN = getReference("colored_trees_generating_in");
	
	private static TagKey<Biome> getReference(String id) {
		return TagKey.of(Registry.BIOME_KEY, SpectrumCommon.locate(id));
	}
	
}
