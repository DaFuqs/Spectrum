package de.dafuqs.spectrum.registries;

import net.fabricmc.fabric.api.registry.*;
import net.minecraft.item.*;

public class SpectrumTillableBlocks {
	
	public static void register() {
		TillableBlockRegistry.register(SpectrumBlocks.SLUSH, HoeItem::canTillFarmland, SpectrumBlocks.TILLED_SLUSH.getDefaultState());
		TillableBlockRegistry.register(SpectrumBlocks.SHALE_CLAY, HoeItem::canTillFarmland, SpectrumBlocks.TILLED_SHALE_CLAY.getDefaultState());
	}
	
}
