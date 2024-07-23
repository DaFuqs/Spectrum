package de.dafuqs.spectrum.registries;

import net.fabricmc.fabric.api.registry.*;

public class SpectrumWaxableBlocks {

	public static void register() {
		OxidizableBlocksRegistry.registerWaxableBlockPair(SpectrumBlocks.HUMMINGSTONE, SpectrumBlocks.WAXED_HUMMINGSTONE);
	}

}
