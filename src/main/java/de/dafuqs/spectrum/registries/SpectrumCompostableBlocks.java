package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import net.fabricmc.fabric.api.registry.*;
import net.minecraft.util.*;

public class SpectrumCompostableBlocks {
	
	public static void register() {
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.CLOVER.asItem(), 0.25F);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.FOUR_LEAF_CLOVER.asItem(), 0.25F);
		
		for (DyeColor dyeColor : DyeColor.values()) {
			CompostingChanceRegistry.INSTANCE.add(ColoredLeavesBlock.byColor(dyeColor), 0.6F);
		}
	}
}
