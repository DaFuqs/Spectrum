package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.blocks.conditional.ColoredLeavesBlock;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.util.DyeColor;

public class SpectrumComposting {
	
	public static void register() {
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.CLOVER.asItem(), 0.25F);
		CompostingChanceRegistry.INSTANCE.add(SpectrumBlocks.FOUR_LEAF_CLOVER.asItem(), 0.25F);
		
		for (DyeColor dyeColor : DyeColor.values()) {
			CompostingChanceRegistry.INSTANCE.add(ColoredLeavesBlock.byColor(dyeColor), 0.6F);
		}
	}
}
