package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.blocks.conditional.colored_tree.*;
import de.dafuqs.spectrum.helpers.*;
import net.fabricmc.fabric.api.registry.*;
import net.minecraft.util.*;

public class SpectrumStrippableBlocks {
	
	public static void register() {
		StrippableBlockRegistry.register(SpectrumBlocks.SLATE_NOXCAP_STEM, SpectrumBlocks.STRIPPED_SLATE_NOXCAP_STEM);
		StrippableBlockRegistry.register(SpectrumBlocks.EBONY_NOXCAP_STEM, SpectrumBlocks.STRIPPED_EBONY_NOXCAP_STEM);
		StrippableBlockRegistry.register(SpectrumBlocks.IVORY_NOXCAP_STEM, SpectrumBlocks.STRIPPED_IVORY_NOXCAP_STEM);
		StrippableBlockRegistry.register(SpectrumBlocks.CHESTNUT_NOXCAP_STEM, SpectrumBlocks.STRIPPED_CHESTNUT_NOXCAP_STEM);
		StrippableBlockRegistry.register(SpectrumBlocks.SLATE_NOXCAP_HYPHAE, SpectrumBlocks.STRIPPED_SLATE_NOXCAP_HYPHAE);
		StrippableBlockRegistry.register(SpectrumBlocks.EBONY_NOXCAP_HYPHAE, SpectrumBlocks.STRIPPED_EBONY_NOXCAP_HYPHAE);
		StrippableBlockRegistry.register(SpectrumBlocks.IVORY_NOXCAP_HYPHAE, SpectrumBlocks.STRIPPED_IVORY_NOXCAP_HYPHAE);
		StrippableBlockRegistry.register(SpectrumBlocks.CHESTNUT_NOXCAP_HYPHAE, SpectrumBlocks.STRIPPED_CHESTNUT_NOXCAP_HYPHAE);
		
		StrippableBlockRegistry.register(SpectrumBlocks.WEEPING_GALA_LOG, SpectrumBlocks.STRIPPED_WEEPING_GALA_LOG);
		StrippableBlockRegistry.register(SpectrumBlocks.WEEPING_GALA_WOOD, SpectrumBlocks.STRIPPED_WEEPING_GALA_WOOD);
		
		for (DyeColor dyeColor : ColorHelper.VANILLA_DYE_COLORS) {
			StrippableBlockRegistry.register(ColoredLogBlock.byColor(dyeColor), ColoredStrippedLogBlock.byColor(dyeColor));
			StrippableBlockRegistry.register(ColoredWoodBlock.byColor(dyeColor), ColoredStrippedWoodBlock.byColor(dyeColor));
		}
	}
	
}
