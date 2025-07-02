package de.dafuqs.spectrum.compat.malum;

import com.sammy.malum.registry.common.item.*;
import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.compat.*;
import net.fabricmc.api.*;

public class MalumCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	@Override
    public void register() {
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.MNEMONIC_FRAGMENT.get(), InkColors.PURPLE);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.NULL_SLATE.get(), InkColors.BLACK);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.EARTHEN_SPIRIT.get(), InkColors.BROWN);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.INFERNAL_SPIRIT.get(), InkColors.ORANGE);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.AERIAL_SPIRIT.get(), InkColors.CYAN);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.AQUEOUS_SPIRIT.get(), InkColors.LIGHT_BLUE);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.ARCANE_SPIRIT.get(), InkColors.LIGHT_GRAY);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.SACRED_SPIRIT.get(), InkColors.WHITE);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.ELDRITCH_SPIRIT.get(), InkColors.GRAY);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.WICKED_SPIRIT.get(), InkColors.BLACK);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.BLAZING_QUARTZ.get(), InkColors.ORANGE);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.BLIGHTED_GUNK.get(), InkColors.LIGHT_GRAY);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.MASS_OF_BLIGHTED_GUNK.get(), InkColors.LIGHT_GRAY);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.CURSED_SAPBALL.get(), InkColors.BROWN);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.PROCESSED_SOULSTONE.get(), InkColors.PURPLE);
		
		
	}

    @Environment(EnvType.CLIENT)
    @Override
    public void registerClient() {
    
    }
}
