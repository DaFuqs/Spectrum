package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.registries.*;

public class SpectrumFlammableBlocks {
	
	private static void add(DeferredBlock<?> block, int igniteOdds, int burnOdds) {
		add(block.get(), igniteOdds, burnOdds);
	}
	
	private static void add(Block block, int igniteOdds, int burnOdds) {
		((FireBlock) Blocks.FIRE).setFlammable(block, igniteOdds, burnOdds);
	}
	
	public static void register() {
		// ignite odds, burn odds
		add(SpectrumBlocks.CLOVER, 60, 100);
		
		for (InkColor color : InkColors.all()) {
			add(ColoredLogBlock.byColor(color), 5, 5);
			add(ColoredWoodBlock.byColor(color), 5, 5);
			add(ColoredLeavesBlock.byColor(color), 30, 60);
			add(ColoredPlankBlock.byColor(color), 5, 20);
			add(ColoredSlabBlock.byColor(color), 5, 20);
			add(ColoredFenceBlock.byColor(color), 5, 20);
			add(ColoredFenceGateBlock.byColor(color), 5, 20);
			add(ColoredStairsBlock.byColor(color), 5, 20);
			add(ColoredSporeBlossomBlock.byColor(color), 60, 100);
			add(ColoredStrippedLogBlock.byColor(color), 5, 20);
			add(ColoredStrippedWoodBlock.byColor(color), 5, 20);
		}
		
		add(SpectrumBlocks.VEGETAL_BLOCK, 30, 60);
		
		add(SpectrumBlocks.SWEET_PEA, 60, 100);
		add(SpectrumBlocks.APRICOTTI, 60, 100);
		add(SpectrumBlocks.HUMMING_BELL, 60, 100);
		add(SpectrumBlocks.BLOOD_ORCHID, 60, 100);
		add(SpectrumBlocks.RESONANT_LILY, 60, 100);
		
		add(SpectrumBlocks.NEPHRITE_BLOSSOM_STEM, 5, 5);
		add(SpectrumBlocks.NEPHRITE_BLOSSOM_LEAVES, 30, 60);
		add(SpectrumBlocks.JADE_VINES, 5, 5);
		add(SpectrumBlocks.JADEITE_LOTUS_STEM, 5, 5);
		add(SpectrumBlocks.JADEITE_LOTUS_FLOWER, 30, 60);
		add(SpectrumBlocks.JADE_VINE_PETAL_BLOCK, 30, 60);
		add(SpectrumBlocks.JADE_VINE_PETAL_CARPET, 60, 20);
		add(SpectrumBlocks.JADEITE_PETAL_BLOCK, 30, 60);
		add(SpectrumBlocks.JADEITE_PETAL_CARPET, 60, 20);
		
		add(SpectrumBlocks.NIGHTDEW, 15, 60);
		add(SpectrumBlocks.ABYSSAL_VINES, 15, 60);
		
		add(SpectrumBlocks.SLATE_NOXCAP_STEM, 5, 5);
		add(SpectrumBlocks.STRIPPED_SLATE_NOXCAP_STEM, 5, 5);
		add(SpectrumBlocks.SLATE_NOXCAP_HYPHAE, 5, 5);
		add(SpectrumBlocks.STRIPPED_SLATE_NOXCAP_HYPHAE, 5, 5);
		add(SpectrumBlocks.SLATE_NOXCAP_BLOCK, 5, 5);
		add(SpectrumBlocks.SLATE_NOXCAP_GILLS, 5, 5);
		add(SpectrumBlocks.SLATE_NOXWOOD_PLANKS, 5, 20);
		add(SpectrumBlocks.SLATE_NOXWOOD_SLAB, 5, 20);
		add(SpectrumBlocks.SLATE_NOXWOOD_FENCE, 5, 20);
		add(SpectrumBlocks.SLATE_NOXWOOD_FENCE_GATE, 5, 20);
		add(SpectrumBlocks.SLATE_NOXWOOD_STAIRS, 5, 20);
		add(SpectrumBlocks.SLATE_NOXWOOD_PILLAR, 5, 20);
		add(SpectrumBlocks.SLATE_NOXWOOD_LIGHT, 5, 20);
		
		
		add(SpectrumBlocks.EBONY_NOXCAP_STEM, 5, 5);
		add(SpectrumBlocks.STRIPPED_EBONY_NOXCAP_STEM, 5, 5);
		add(SpectrumBlocks.EBONY_NOXCAP_HYPHAE, 5, 5);
		add(SpectrumBlocks.STRIPPED_EBONY_NOXCAP_HYPHAE, 5, 5);
		add(SpectrumBlocks.EBONY_NOXCAP_BLOCK, 5, 5);
		add(SpectrumBlocks.EBONY_NOXCAP_GILLS, 5, 5);
		add(SpectrumBlocks.EBONY_NOXWOOD_PLANKS, 5, 20);
		add(SpectrumBlocks.EBONY_NOXWOOD_SLAB, 5, 20);
		add(SpectrumBlocks.EBONY_NOXWOOD_FENCE, 5, 20);
		add(SpectrumBlocks.EBONY_NOXWOOD_FENCE_GATE, 5, 20);
		add(SpectrumBlocks.EBONY_NOXWOOD_STAIRS, 5, 20);
		add(SpectrumBlocks.EBONY_NOXWOOD_PILLAR, 5, 20);
		add(SpectrumBlocks.EBONY_NOXWOOD_LIGHT, 5, 20);
		
		add(SpectrumBlocks.IVORY_NOXCAP_STEM, 5, 5);
		add(SpectrumBlocks.STRIPPED_IVORY_NOXCAP_STEM, 5, 5);
		add(SpectrumBlocks.IVORY_NOXCAP_HYPHAE, 5, 5);
		add(SpectrumBlocks.STRIPPED_IVORY_NOXCAP_HYPHAE, 5, 5);
		add(SpectrumBlocks.IVORY_NOXCAP_BLOCK, 5, 5);
		add(SpectrumBlocks.IVORY_NOXCAP_GILLS, 5, 5);
		add(SpectrumBlocks.IVORY_NOXWOOD_PLANKS, 5, 20);
		add(SpectrumBlocks.IVORY_NOXWOOD_SLAB, 5, 20);
		add(SpectrumBlocks.IVORY_NOXWOOD_FENCE, 5, 20);
		add(SpectrumBlocks.IVORY_NOXWOOD_FENCE_GATE, 5, 20);
		add(SpectrumBlocks.IVORY_NOXWOOD_STAIRS, 5, 20);
		add(SpectrumBlocks.IVORY_NOXWOOD_PILLAR, 5, 20);
		add(SpectrumBlocks.IVORY_NOXWOOD_LIGHT, 5, 20);
		
		add(SpectrumBlocks.CHESTNUT_NOXCAP_STEM, 5, 5);
		add(SpectrumBlocks.STRIPPED_CHESTNUT_NOXCAP_STEM, 5, 5);
		add(SpectrumBlocks.CHESTNUT_NOXCAP_HYPHAE, 5, 5);
		add(SpectrumBlocks.STRIPPED_CHESTNUT_NOXCAP_HYPHAE, 5, 5);
		add(SpectrumBlocks.CHESTNUT_NOXCAP_BLOCK, 5, 5);
		add(SpectrumBlocks.CHESTNUT_NOXCAP_GILLS, 5, 5);
		add(SpectrumBlocks.CHESTNUT_NOXWOOD_PLANKS, 5, 20);
		add(SpectrumBlocks.CHESTNUT_NOXWOOD_SLAB, 5, 20);
		add(SpectrumBlocks.CHESTNUT_NOXWOOD_FENCE, 5, 20);
		add(SpectrumBlocks.CHESTNUT_NOXWOOD_FENCE_GATE, 5, 20);
		add(SpectrumBlocks.CHESTNUT_NOXWOOD_STAIRS, 5, 20);
		add(SpectrumBlocks.CHESTNUT_NOXWOOD_PILLAR, 5, 20);
		add(SpectrumBlocks.CHESTNUT_NOXWOOD_LIGHT, 5, 20);
		
		add(SpectrumBlocks.WEEPING_GALA_LOG, 2, 2);
		add(SpectrumBlocks.WEEPING_GALA_LEAVES, 10, 20);
		add(SpectrumBlocks.WEEPING_GALA_PLANKS, 2, 8);
		add(SpectrumBlocks.WEEPING_GALA_SLAB, 2, 8);
		add(SpectrumBlocks.WEEPING_GALA_FENCE, 2, 8);
		add(SpectrumBlocks.WEEPING_GALA_FENCE_GATE, 2, 8);
		add(SpectrumBlocks.WEEPING_GALA_STAIRS, 2, 8);
		add(SpectrumBlocks.STRIPPED_WEEPING_GALA_LOG, 2, 8);
		add(SpectrumBlocks.STRIPPED_WEEPING_GALA_WOOD, 2, 8);
		add(SpectrumBlocks.WEEPING_GALA_PILLAR, 2, 8);
		add(SpectrumBlocks.WEEPING_GALA_LIGHT, 2, 8);
		add(SpectrumBlocks.WEEPING_GALA_LAMP, 2, 8);
		
		add(SpectrumBlocks.MOSS_BALL, 30, 60);
		add(SpectrumBlocks.GIANT_MOSS_BALL, 30, 60);
	}
	
}
