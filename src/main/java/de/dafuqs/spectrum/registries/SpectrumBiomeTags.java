package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.SpectrumCommon;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class SpectrumBiomeTags {
    
    public static final TagKey<Biome> HAS_ANCIENT_RUINS_BIOMES = getReference("has_structure/ancient_ruins");

    private static TagKey<Biome> getReference(String id) {
        return TagKey.of(Registry.BIOME_KEY, SpectrumCommon.locate(id));
    }
    
}
