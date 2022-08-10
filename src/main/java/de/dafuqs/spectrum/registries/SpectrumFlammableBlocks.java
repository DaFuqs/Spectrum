package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.blocks.conditional.ColoredLeavesBlock;
import de.dafuqs.spectrum.blocks.conditional.ColoredLogBlock;
import de.dafuqs.spectrum.blocks.decoration.*;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.util.DyeColor;

public class SpectrumFlammableBlocks {
	
	public static void register() {
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
	}
	
}
