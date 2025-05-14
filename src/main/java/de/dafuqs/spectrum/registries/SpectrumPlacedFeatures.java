package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.fabricmc.fabric.api.biome.v1.*;
import net.fabricmc.fabric.api.tag.convention.v2.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.dimension.*;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.placement.*;

public class SpectrumPlacedFeatures {
	
	public static ResourceKey<PlacedFeature> of(String id) {
		return ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate(id));
	}
	
	public static void addBiomeModifications() {
		// Geodes
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IS_OVERWORLD), GenerationStep.Decoration.UNDERGROUND_STRUCTURES, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("citrine_geode")));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IS_OVERWORLD), GenerationStep.Decoration.UNDERGROUND_STRUCTURES, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("topaz_geode")));
		
		// Ores
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IS_OVERWORLD), GenerationStep.Decoration.UNDERGROUND_ORES, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("shimmerstone_ore")));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IS_OVERWORLD), GenerationStep.Decoration.UNDERGROUND_ORES, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("azurite_ore")));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IS_NETHER), GenerationStep.Decoration.UNDERGROUND_ORES, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("stratine_ore")));
		BiomeModifications.addFeature(BiomeSelectors.tag(ConventionalBiomeTags.IS_END), GenerationStep.Decoration.UNDERGROUND_ORES, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("paltaeria_ore")));
		
		BiomeModifications.addFeature(BiomeSelectors.tag(SpectrumBiomeTags.COLORED_TREES_GENERATING_IN), GenerationStep.Decoration.VEGETAL_DECORATION, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("colored_tree_patch")));
		
		// Plants
		BiomeModifications.addFeature(BiomeSelectors.tag(SpectrumBiomeTags.MERMAIDS_BRUSHES_GENERATING_IN), GenerationStep.Decoration.VEGETAL_DECORATION, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("mermaids_brushes")));
		BiomeModifications.addFeature(BiomeSelectors.tag(SpectrumBiomeTags.CLOVER_GENERATING_IN), GenerationStep.Decoration.VEGETAL_DECORATION, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("clover_patch")));

		if (SpectrumCommon.CONFIG.QuitoxicSpawnTag) {
			BiomeModifications.addFeature(BiomeSelectors.tag(SpectrumBiomeTags.QUITOXIC_REEDS_GENERATING_IN), GenerationStep.Decoration.VEGETAL_DECORATION, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("quitoxic_reeds")));
		}
		else {
			BiomeModifications.addFeature(context -> {
				if (!context.canGenerateIn(LevelStem.OVERWORLD))
					return false;
				
				if (context.hasTag(ConventionalBiomeTags.IS_AQUATIC))
					return false;

				//Either the biome is hot, lush, and wet, or it is a straight-up swamp.
				return ((context.hasTag(ConventionalBiomeTags.IS_HOT) && context.hasTag(ConventionalBiomeTags.IS_VEGETATION_DENSE_OVERWORLD))
						|| context.hasTag(ConventionalBiomeTags.IS_SWAMP))
						&& context.hasTag(ConventionalBiomeTags.IS_WET_OVERWORLD);
				
			}, GenerationStep.Decoration.VEGETAL_DECORATION, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("quitoxic_reeds")));
		}

		// Dragonbone in the Overworld
		BiomeModifications.addFeature(BiomeSelectors.tag(SpectrumBiomeTags.DRAGONBONE_FOSSILS_GENERATING_IN), GenerationStep.Decoration.UNDERGROUND_DECORATION, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("dragon_fossil_overworld_buried")));
		BiomeModifications.addFeature(BiomeSelectors.tag(SpectrumBiomeTags.DRAGONBONE_FOSSILS_GENERATING_IN), GenerationStep.Decoration.VEGETAL_DECORATION, ResourceKey.create(Registries.PLACED_FEATURE, SpectrumCommon.locate("dragon_fossil_overworld_exposed")));
	}
	
}
