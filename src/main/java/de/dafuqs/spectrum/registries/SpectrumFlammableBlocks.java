package de.dafuqs.spectrum.registries;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.util.DyeColor;

public class SpectrumFlammableBlocks {
	
	public static void register() {
		
		FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.CLOVER, 60, 100);
		// FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.FOUR_LEAF_CLOVER, 60, 100); // nope C:
		
		for (DyeColor dyeColor : DyeColor.values()) {
			FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.getColoredLogBlock(dyeColor), 5, 5);
			FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.getColoredLeavesBlock(dyeColor), 30, 60);
			FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.getColoredPlanksBlock(dyeColor), 5, 20);
			FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.getColoredSlabsBlock(dyeColor), 5, 20);
			FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.getColoredFenceBlock(dyeColor), 5, 20);
			FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.getColoredFenceGateBlock(dyeColor), 5, 20);
			FlammableBlockRegistry.getDefaultInstance().add(SpectrumBlocks.getColoredStairsBlock(dyeColor), 5, 20);
		}
	}
	
}
