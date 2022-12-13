package de.dafuqs.spectrum.registries;

import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;

public class SpectrumStrippableBlocks {
	
	public static void register() {
		StrippableBlockRegistry.register(SpectrumBlocks.SLATE_NOXCAP_STEM, SpectrumBlocks.STRIPPED_SLATE_NOXCAP_STEM);
		StrippableBlockRegistry.register(SpectrumBlocks.EBONY_NOXCAP_STEM, SpectrumBlocks.STRIPPED_EBONY_NOXCAP_STEM);
		StrippableBlockRegistry.register(SpectrumBlocks.IVORY_NOXCAP_STEM, SpectrumBlocks.STRIPPED_IVORY_NOXCAP_STEM);
		StrippableBlockRegistry.register(SpectrumBlocks.CHESTNUT_NOXCAP_STEM, SpectrumBlocks.STRIPPED_CHESTNUT_NOXCAP_STEM);
	}
	
}
