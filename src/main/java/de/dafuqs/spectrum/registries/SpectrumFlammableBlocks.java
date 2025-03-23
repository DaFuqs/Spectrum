package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import de.dafuqs.spectrum.blocks.decoration.*;
import de.dafuqs.spectrum.helpers.*;
import net.fabricmc.fabric.api.registry.*;
import net.minecraft.util.*;

public class SpectrumFlammableBlocks {
	
	public static void register() {
		
		// ignite odds, burn odds
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CLOVER, 60, 100);
		
		for (DyeColor dyeColor : ColorHelper.VANILLA_DYE_COLORS) {
			FlammableBlockRegistry.getDefaultInstance().add(ColoredLogBlock.byColor(dyeColor), 5, 5);
			FlammableBlockRegistry.getDefaultInstance().add(ColoredWoodBlock.byColor(dyeColor), 5, 5);
			FlammableBlockRegistry.getDefaultInstance().add(ColoredLeavesBlock.byColor(dyeColor), 30, 60);
			FlammableBlockRegistry.getDefaultInstance().add(ColoredPlankBlock.byColor(dyeColor), 5, 20);
			FlammableBlockRegistry.getDefaultInstance().add(ColoredSlabBlock.byColor(dyeColor), 5, 20);
			FlammableBlockRegistry.getDefaultInstance().add(ColoredFenceBlock.byColor(dyeColor), 5, 20);
			FlammableBlockRegistry.getDefaultInstance().add(ColoredFenceGateBlock.byColor(dyeColor), 5, 20);
			FlammableBlockRegistry.getDefaultInstance().add(ColoredStairsBlock.byColor(dyeColor), 5, 20);
			FlammableBlockRegistry.getDefaultInstance().add(ColoredSporeBlossomBlock.byColor(dyeColor), 60, 100);
			FlammableBlockRegistry.getDefaultInstance().add(ColoredStrippedLogBlock.byColor(dyeColor), 5, 20);
			FlammableBlockRegistry.getDefaultInstance().add(ColoredStrippedWoodBlock.byColor(dyeColor), 5, 20);
		}
		
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.VEGETAL_BLOCK, 30, 60);
		
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.SWEET_PEA, 60, 100);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.APRICOTTI, 60, 100);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.HUMMING_BELL, 60, 100);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.BLOOD_ORCHID, 60, 100);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.RESONANT_LILY, 60, 100);
		
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.NEPHRITE_BLOSSOM_STEM, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.NEPHRITE_BLOSSOM_LEAVES, 30, 60);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.JADE_VINES, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.JADEITE_LOTUS_STEM, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.JADEITE_LOTUS_FLOWER, 30, 60);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.JADE_VINE_PETAL_BLOCK, 30, 60);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.JADE_VINE_PETAL_CARPET, 60, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.JADEITE_PETAL_BLOCK, 30, 60);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.JADEITE_PETAL_CARPET, 60, 20);
		
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.NIGHTDEW, 15, 60);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.ABYSSAL_VINES, 15, 60);
		
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.SLATE_NOXCAP_STEM, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.STRIPPED_SLATE_NOXCAP_STEM, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.SLATE_NOXCAP_HYPHAE, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.STRIPPED_SLATE_NOXCAP_HYPHAE, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.SLATE_NOXCAP_BLOCK, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.SLATE_NOXCAP_GILLS, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.SLATE_NOXWOOD_PLANKS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.SLATE_NOXWOOD_SLAB, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.SLATE_NOXWOOD_FENCE, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.SLATE_NOXWOOD_FENCE_GATE, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.SLATE_NOXWOOD_STAIRS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.SLATE_NOXWOOD_BEAM, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.SLATE_NOXWOOD_LIGHT, 5, 20);
		
		
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.EBONY_NOXCAP_STEM, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.STRIPPED_EBONY_NOXCAP_STEM, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.EBONY_NOXCAP_HYPHAE, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.STRIPPED_EBONY_NOXCAP_HYPHAE, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.EBONY_NOXCAP_BLOCK, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.EBONY_NOXCAP_GILLS, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.EBONY_NOXWOOD_PLANKS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.EBONY_NOXWOOD_SLAB, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.EBONY_NOXWOOD_FENCE, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.EBONY_NOXWOOD_FENCE_GATE, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.EBONY_NOXWOOD_STAIRS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.EBONY_NOXWOOD_BEAM, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.EBONY_NOXWOOD_LIGHT, 5, 20);
		
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.IVORY_NOXCAP_STEM, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.STRIPPED_IVORY_NOXCAP_STEM, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.IVORY_NOXCAP_HYPHAE, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.STRIPPED_IVORY_NOXCAP_HYPHAE, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.IVORY_NOXCAP_BLOCK, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.IVORY_NOXCAP_GILLS, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.IVORY_NOXWOOD_PLANKS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.IVORY_NOXWOOD_SLAB, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.IVORY_NOXWOOD_FENCE, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.IVORY_NOXWOOD_FENCE_GATE, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.IVORY_NOXWOOD_STAIRS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.IVORY_NOXWOOD_BEAM, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.IVORY_NOXWOOD_LIGHT, 5, 20);
		
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXCAP_STEM, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.STRIPPED_CHESTNUT_NOXCAP_STEM, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXCAP_HYPHAE, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.STRIPPED_CHESTNUT_NOXCAP_HYPHAE, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXCAP_BLOCK, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXCAP_GILLS, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXWOOD_PLANKS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXWOOD_SLAB, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXWOOD_FENCE, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXWOOD_FENCE_GATE, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXWOOD_STAIRS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXWOOD_BEAM, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXWOOD_LIGHT, 5, 20);
		
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.WEEPING_GALA_LOG, 2, 2);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.WEEPING_GALA_LEAVES, 10, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.WEEPING_GALA_PLANKS, 2, 8);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.WEEPING_GALA_SLAB, 2, 8);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.WEEPING_GALA_FENCE, 2, 8);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.WEEPING_GALA_FENCE_GATE, 2, 8);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.WEEPING_GALA_STAIRS, 2, 8);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.STRIPPED_WEEPING_GALA_LOG, 2, 8);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.STRIPPED_WEEPING_GALA_WOOD, 2, 8);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.WEEPING_GALA_PILLAR, 2, 8);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.WEEPING_GALA_LIGHT, 2, 8);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.WEEPING_GALA_LAMP, 2, 8);
		
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.MOSS_BALL, 30, 60);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.GIANT_MOSS_BALL, 30, 60);
	}
	
}
