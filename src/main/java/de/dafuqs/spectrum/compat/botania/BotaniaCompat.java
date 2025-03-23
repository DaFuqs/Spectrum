package de.dafuqs.spectrum.compat.botania;

import de.dafuqs.fractal.api.*;
import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.api.item_group.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import vazkii.botania.common.item.*;

public class BotaniaCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	public static Item LEAST_BLACK_LOTUS = new LeastBlackLotusItem(new Item.Settings());
	public static Item BLACKEST_LOTUS = new BlackestLotusItem(new Item.Settings());
	
	@Override
	public void register() {
		SpectrumItems.register("least_black_lotus", LEAST_BLACK_LOTUS, DyeColor.BLACK);
		SpectrumItems.register("blackest_lotus", BLACKEST_LOTUS, DyeColor.BLACK);
		
		// registering it late, since Botania might not have been initialized yet
		ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> {
			ItemColors.ITEM_COLORS.registerColorMapping(BotaniaItems.overgrowthSeed, DyeColor.LIME);
			ItemColors.ITEM_COLORS.registerColorMapping(BotaniaItems.blackLotus, DyeColor.BLACK);
			ItemColors.ITEM_COLORS.registerColorMapping(BotaniaItems.blackerLotus, DyeColor.BLACK);
			ItemColors.ITEM_COLORS.registerColorMapping(BotaniaItems.terrasteel, DyeColor.LIME);
			
			ItemProviderRegistry.register(BotaniaItems.blackHoleTalisman, new ItemProvider() {
				@Override
				public int getItemCount(PlayerEntity player, ItemStack stack, Item requestedItem) {
					if (requestedItem instanceof BlockItem blockItem) {
						Block storedBlock = BlackHoleTalismanItem.getBlock(stack);
						if (blockItem.getBlock() == storedBlock) {
							return BlackHoleTalismanItem.getBlockCount(stack);
						}
					}
					return 0;
				}
				
				@Override
				public int provideItems(PlayerEntity player, ItemStack stack, Item requestedItem, int amount) {
					if (requestedItem instanceof BlockItem blockItem) {
						Block storedBlock = BlackHoleTalismanItem.getBlock(stack);
						if (blockItem.getBlock() == storedBlock) {
							int storedAmount = BlackHoleTalismanItem.getBlockCount(stack);
							int amountToRemove = Math.min(storedAmount, amount);
							BlackHoleTalismanItem.setCount(stack, storedAmount - amountToRemove);
							return amountToRemove;
						}
					}
					return 0;
				}
			});
		});
		
		ItemSubGroupEvents.modifyEntriesEvent(ItemGroupIDs.SUBTAB_EQUIPMENT).register(entries -> {
			entries.add(LEAST_BLACK_LOTUS);
			entries.add(BLACKEST_LOTUS);
		});
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void registerClient() {
	
	}
	
}
