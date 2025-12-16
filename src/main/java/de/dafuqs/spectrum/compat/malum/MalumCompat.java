package de.dafuqs.spectrum.compat.malum;

import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.compat.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;

public class MalumCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	@Override
    public void register() {
		// registering it late, since Malum might not have been initialized yet
		ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> {
			ItemColors.ITEM_COLORS.registerColorMapping(getItemFromRegistry("mnemonic_fragment"), DyeColor.PURPLE);
			ItemColors.ITEM_COLORS.registerColorMapping(getItemFromRegistry("null_slate"), DyeColor.BLACK);
			ItemColors.ITEM_COLORS.registerColorMapping(getItemFromRegistry("earthen_spirit"), DyeColor.BROWN);
			ItemColors.ITEM_COLORS.registerColorMapping(getItemFromRegistry("infernal_spirit"), DyeColor.ORANGE);
			ItemColors.ITEM_COLORS.registerColorMapping(getItemFromRegistry("aerial_spirit"), DyeColor.CYAN);
			ItemColors.ITEM_COLORS.registerColorMapping(getItemFromRegistry("aqueous_spirit"), DyeColor.LIGHT_BLUE);
			ItemColors.ITEM_COLORS.registerColorMapping(getItemFromRegistry("arcane_spirit"), DyeColor.LIGHT_GRAY);
			ItemColors.ITEM_COLORS.registerColorMapping(getItemFromRegistry("sacred_spirit"), DyeColor.WHITE);
			ItemColors.ITEM_COLORS.registerColorMapping(getItemFromRegistry("eldritch_spirit"), DyeColor.GRAY);
			ItemColors.ITEM_COLORS.registerColorMapping(getItemFromRegistry("wicked_spirit"), DyeColor.BLACK);
			ItemColors.ITEM_COLORS.registerColorMapping(getItemFromRegistry("blazing_quartz"), DyeColor.ORANGE);
			ItemColors.ITEM_COLORS.registerColorMapping(getItemFromRegistry("blighted_gunk"), DyeColor.LIGHT_GRAY);
			ItemColors.ITEM_COLORS.registerColorMapping(getItemFromRegistry("mass_of_blighted_gunk"), DyeColor.LIGHT_GRAY);
			ItemColors.ITEM_COLORS.registerColorMapping(getItemFromRegistry("cursed_sapball"), DyeColor.BROWN);
			ItemColors.ITEM_COLORS.registerColorMapping(getItemFromRegistry("processed_soulstone"), DyeColor.PURPLE);
		});
		
		
	}
	
	// Workaround to prevent loading classes from Porting Lib on Forge
	private static Item getItemFromRegistry(String path) {
		return Registries.ITEM.get(Identifier.of("malum", path));
	}

    @Environment(EnvType.CLIENT)
    @Override
    public void registerClient() {
    
    }
}
