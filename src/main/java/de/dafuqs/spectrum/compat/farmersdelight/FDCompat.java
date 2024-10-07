package de.dafuqs.spectrum.compat.farmersdelight;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.compat.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.loot.v2.*;
import net.minecraft.util.*;

public class FDCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	private static final Identifier AMARANTH_LOOT_TABLE_ID = SpectrumCommon.locate("blocks/amaranth");
	
	public void register() {
		LootTableEvents.REPLACE.register((resourceManager, lootManager, id, original, source) -> {
			if (AMARANTH_LOOT_TABLE_ID.equals(id)) {
				return lootManager.getLootTable(SpectrumCommon.locate("mod_integration/farmers_delight/amaranth"));
			}
			return original;
		});
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void registerClient() {
	
	}
	
}
