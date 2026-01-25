package de.dafuqs.spectrum.compat.malum;

import com.sammy.malum.registry.common.block.*;
import com.sammy.malum.registry.common.item.*;
import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.compat.*;

public class MalumCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	@Override
    public void register() {
		ItemColors.ITEM_COLORS.registerColorMapping(MalumItems.MNEMONIC_FRAGMENT.get(), InkColors.PURPLE);
		ItemColors.ITEM_COLORS.registerColorMapping(MalumItems.NULL_SLATE.get(), InkColors.BLACK);
		ItemColors.ITEM_COLORS.registerColorMapping(MalumItems.EARTHEN_SPIRIT.get(), InkColors.BROWN);
		ItemColors.ITEM_COLORS.registerColorMapping(MalumItems.INFERNAL_SPIRIT.get(), InkColors.ORANGE);
		ItemColors.ITEM_COLORS.registerColorMapping(MalumItems.AERIAL_SPIRIT.get(), InkColors.CYAN);
		ItemColors.ITEM_COLORS.registerColorMapping(MalumItems.AQUEOUS_SPIRIT.get(), InkColors.LIGHT_BLUE);
		ItemColors.ITEM_COLORS.registerColorMapping(MalumItems.ARCANE_SPIRIT.get(), InkColors.LIGHT_GRAY);
		ItemColors.ITEM_COLORS.registerColorMapping(MalumItems.SACRED_SPIRIT.get(), InkColors.WHITE);
		ItemColors.ITEM_COLORS.registerColorMapping(MalumItems.ELDRITCH_SPIRIT.get(), InkColors.GRAY);
		ItemColors.ITEM_COLORS.registerColorMapping(MalumItems.WICKED_SPIRIT.get(), InkColors.BLACK);
		ItemColors.ITEM_COLORS.registerColorMapping(MalumItems.BLAZING_QUARTZ.get(), InkColors.ORANGE);
		ItemColors.ITEM_COLORS.registerColorMapping(MalumItems.BLIGHTED_GUNK.get(), InkColors.LIGHT_GRAY);
		ItemColors.ITEM_COLORS.registerColorMapping(MalumItems.MASS_OF_BLIGHTED_GUNK.get(), InkColors.LIGHT_GRAY);
		ItemColors.ITEM_COLORS.registerColorMapping(MalumItems.CURSED_SAPBALL.get(), InkColors.BROWN);
	}
	
	@Override
    public void registerClient() {
    
    }
}
