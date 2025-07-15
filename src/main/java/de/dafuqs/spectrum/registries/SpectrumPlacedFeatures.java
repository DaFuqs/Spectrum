package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.levelgen.placement.*;

public class SpectrumPlacedFeatures {
	
	public static ResourceKey<PlacedFeature> of(String id) {
		return ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate(id));
	}
	
	public static void addBiomeModifications() {
		// TODO: migrate to neoforge format documented @ https://docs.neoforged.net/docs/worldgen/biomemodifier#applying-biome-modifiers
		
		/*
		// Geodes
		addFeature(Tags.Biomes.IS_OVERWORLD, GenerationStep.Decoration.UNDERGROUND_STRUCTURES, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("citrine_geode")));
		addFeature(Tags.Biomes.IS_OVERWORLD, GenerationStep.Decoration.UNDERGROUND_STRUCTURES, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("topaz_geode")));
		
		// Ores
		addFeature(Tags.Biomes.IS_OVERWORLD, GenerationStep.Decoration.UNDERGROUND_ORES, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("shimmerstone_ore")));
		addFeature(Tags.Biomes.IS_OVERWORLD, GenerationStep.Decoration.UNDERGROUND_ORES, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("azurite_ore")));
		addFeature(Tags.Biomes.IS_NETHER, GenerationStep.Decoration.UNDERGROUND_ORES, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("stratine_ore")));
		addFeature(Tags.Biomes.IS_END, GenerationStep.Decoration.UNDERGROUND_ORES, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("paltaeria_ore")));
		
		addFeature(SpectrumBiomeTags.COLORED_TREES_GENERATING_IN, GenerationStep.Decoration.VEGETAL_DECORATION, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("colored_tree_patch")));
		
		// Plants
		addFeature(SpectrumBiomeTags.MERMAIDS_BRUSHES_GENERATING_IN, GenerationStep.Decoration.VEGETAL_DECORATION, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("mermaids_brushes")));
		addFeature(SpectrumBiomeTags.CLOVER_GENERATING_IN, GenerationStep.Decoration.VEGETAL_DECORATION, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("clover_patch")));
		addFeature(SpectrumBiomeTags.QUITOXIC_REEDS_GENERATING_IN), GenerationStep.Decoration.VEGETAL_DECORATION, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("quitoxic_reeds")));

		// Dragonbone in the Overworld
		addFeature(SpectrumBiomeTags.DRAGONBONE_FOSSILS_GENERATING_IN), GenerationStep.Decoration.UNDERGROUND_DECORATION, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("dragon_fossil_overworld_buried")));
		addFeature(SpectrumBiomeTags.DRAGONBONE_FOSSILS_GENERATING_IN), GenerationStep.Decoration.VEGETAL_DECORATION, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("dragon_fossil_overworld_exposed")));*/
	}
	
}
