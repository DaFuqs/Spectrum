package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class SpectrumBiomeTags {
	
	public static final TagKey<Biome> HAS_ANCIENT_RUINS = getReference("has_structure/ancient_ruins");
	public static final TagKey<Biome> QUITOXIC_REEDS_GENERATING_IN = getReference("quitoxic_reeds_generating_in");
	public static final TagKey<Biome> COLORED_TREES_GENERATING_IN = getReference("colored_trees_generating_in");
	
	private static TagKey<Biome> getReference(String id) {
		return TagKey.of(Registry.BIOME_KEY, SpectrumCommon.locate(id));
	}
	
}
