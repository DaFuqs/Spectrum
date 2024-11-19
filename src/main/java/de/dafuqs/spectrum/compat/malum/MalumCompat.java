package de.dafuqs.spectrum.compat.malum;

import com.sammy.malum.registry.common.item.*;
import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.compat.*;
import net.fabricmc.api.*;
import net.minecraft.util.*;

public class MalumCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	@Override
    public void register() {
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.MNEMONIC_FRAGMENT.get(), DyeColor.PURPLE);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.NULL_SLATE.get(), DyeColor.BLACK);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.EARTHEN_SPIRIT.get(), DyeColor.BROWN);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.INFERNAL_SPIRIT.get(), DyeColor.ORANGE);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.AERIAL_SPIRIT.get(), DyeColor.CYAN);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.AQUEOUS_SPIRIT.get(), DyeColor.LIGHT_BLUE);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.ARCANE_SPIRIT.get(), DyeColor.LIGHT_GRAY);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.SACRED_SPIRIT.get(), DyeColor.WHITE);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.ELDRITCH_SPIRIT.get(), DyeColor.GRAY);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.WICKED_SPIRIT.get(), DyeColor.BLACK);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.BLAZING_QUARTZ.get(), DyeColor.ORANGE);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.BLIGHTED_GUNK.get(), DyeColor.LIGHT_GRAY);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.MASS_OF_BLIGHTED_GUNK.get(), DyeColor.LIGHT_GRAY);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.CURSED_SAPBALL.get(), DyeColor.BROWN);
		ItemColors.ITEM_COLORS.registerColorMapping(ItemRegistry.PROCESSED_SOULSTONE.get(), DyeColor.PURPLE);
		
		
	}

    @Environment(EnvType.CLIENT)
    @Override
    public void registerClient() {
    
    }
}
