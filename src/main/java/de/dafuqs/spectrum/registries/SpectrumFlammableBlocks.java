package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.blocks.conditional.colored_tree.ColoredLeavesBlock;
import de.dafuqs.spectrum.blocks.conditional.colored_tree.ColoredLogBlock;
import de.dafuqs.spectrum.blocks.decoration.*;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.util.DyeColor;

public class SpectrumFlammableBlocks {
	
	public static void register() {
		
		// ignite odds, burn odds
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CLOVER, 60, 100);
		
		for (DyeColor dyeColor : DyeColor.values()) {
			FlammableBlockRegistry.getDefaultInstance().add(ColoredLogBlock.byColor(dyeColor), 5, 5);
			FlammableBlockRegistry.getDefaultInstance().add(ColoredLeavesBlock.byColor(dyeColor), 30, 60);
			FlammableBlockRegistry.getDefaultInstance().add(ColoredPlankBlock.byColor(dyeColor), 5, 20);
			FlammableBlockRegistry.getDefaultInstance().add(ColoredSlabBlock.byColor(dyeColor), 5, 20);
			FlammableBlockRegistry.getDefaultInstance().add(ColoredFenceBlock.byColor(dyeColor), 5, 20);
			FlammableBlockRegistry.getDefaultInstance().add(ColoredFenceGateBlock.byColor(dyeColor), 5, 20);
			FlammableBlockRegistry.getDefaultInstance().add(ColoredStairsBlock.byColor(dyeColor), 5, 20);
		}
		
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.SLATE_NOXCAP_STEM, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.STRIPPED_SLATE_NOXCAP_STEM, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.SLATE_NOXCAP_CAP, 5, 5);
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
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.EBONY_NOXCAP_CAP, 5, 5);
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
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.IVORY_NOXCAP_CAP, 5, 5);
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
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXCAP_CAP, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXCAP_GILLS, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXWOOD_PLANKS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXWOOD_SLAB, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXWOOD_FENCE, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXWOOD_FENCE_GATE, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXWOOD_STAIRS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXWOOD_BEAM, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CHESTNUT_NOXWOOD_LIGHT, 5, 20);
		
	}
	
}
