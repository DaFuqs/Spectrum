package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.dd_deco.*;
import net.fabricmc.fabric.api.biome.v1.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.*;

import java.util.*;

public class SpectrumPlacedFeatures {
	
	// OVERWORLD
	public static final RegistryKey<PlacedFeature> CLOVER_PATCH = of("clover_patch");

	// DEEPER DOWN
	public static final RegistryKey<PlacedFeature> BRISTLE_SPROUT_PATCH = of("bristle_sprouts");
	public static final Map<Dragonjag.Variant, RegistryKey<PlacedFeature>> DRAGONJAGS = new HashMap<>() {{
		put(Dragonjag.Variant.PINK,of("dragonjags/pink"));
		put(Dragonjag.Variant.RED, of("dragonjags/red"));
		put(Dragonjag.Variant.BLACK, of("dragonjags/black"));
		put(Dragonjag.Variant.YELLOW, of("dragonjags/yellow"));
		put(Dragonjag.Variant.PURPLE, of("dragonjags/purple"));
	}};
	
	public static RegistryKey<PlacedFeature> of(String id) {
		return RegistryKey.of(RegistryKeys.PLACED_FEATURE, SpectrumCommon.locate(id));
	}
	
	public static void addBiomeModifications() {
		// Geodes
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_OVERWORLD), GenerationStep.Feature.UNDERGROUND_STRUCTURES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, SpectrumCommon.locate("citrine_geode")));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_OVERWORLD), GenerationStep.Feature.UNDERGROUND_STRUCTURES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, SpectrumCommon.locate("topaz_geode")));
		
		// Ores
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_OVERWORLD), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, SpectrumCommon.locate("shimmerstone_ore")));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_OVERWORLD), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, SpectrumCommon.locate("azurite_ore")));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_NETHER), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, SpectrumCommon.locate("stratine_ore")));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IN_THE_END), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(RegistryKeys.PLACED_FEATURE, SpectrumCommon.locate("paltaeria_ore")));
		
		BiomeModifications.addFeature(BiomeSelectors.tag(SpectrumBiomeTags.COLORED_TREES_GENERATING_IN), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(RegistryKeys.PLACED_FEATURE, SpectrumCommon.locate("colored_tree_patch")));
		
		// Plants
		BiomeModifications.addFeature(BiomeSelectors.tag(BiomeTags.IS_OCEAN), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(RegistryKeys.PLACED_FEATURE, SpectrumCommon.locate("mermaids_brushes")));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.SWAMP), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(RegistryKeys.PLACED_FEATURE, SpectrumCommon.locate("quitoxic_reeds")));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.PLAINS), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(RegistryKeys.PLACED_FEATURE, SpectrumCommon.locate("clover_patch")));
	}
	
}
