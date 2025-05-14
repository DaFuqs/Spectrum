package de.dafuqs.spectrum.compat.botania;

import de.dafuqs.fractal.api.*;
import de.dafuqs.spectrum.api.color.*;
import de.dafuqs.spectrum.api.energy.color.*;
import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.api.item_group.*;
import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import vazkii.botania.common.item.*;

import static de.dafuqs.spectrum.registries.SpectrumItems.*;

@SuppressWarnings("unused")
public class BotaniaCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	public static Item LEAST_BLACK_LOTUS = SpectrumItems.register(simple(item("least_black_lotus", new LeastBlackLotusItem(new Item.Properties()), InkColors.BLACK)));
	public static Item BLACKEST_LOTUS = SpectrumItems.register(simple(item("blackest_lotus", new BlackestLotusItem(new Item.Properties()), InkColors.BLACK)));
	
	@Override
	public void register() {
		SpectrumItems.ITEM_REGISTRAR.flush();
		
		// registering it late, since Botania might not have been initialized yet
		ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> {
			ItemColors.ITEM_COLORS.registerColorMapping(BotaniaItems.overgrowthSeed, InkColors.LIME);
			ItemColors.ITEM_COLORS.registerColorMapping(BotaniaItems.blackLotus, InkColors.BLACK);
			ItemColors.ITEM_COLORS.registerColorMapping(BotaniaItems.blackerLotus, InkColors.BLACK);
			ItemColors.ITEM_COLORS.registerColorMapping(BotaniaItems.terrasteel, InkColors.LIME);
			
			ItemProviderRegistry.register(BotaniaItems.blackHoleTalisman, new ItemProvider() {
				@Override
				public int getItemCount(Player player, ItemStack stack, Item requestedItem) {
					if (requestedItem instanceof BlockItem blockItem) {
						Block storedBlock = BlackHoleTalismanItem.getBlock(stack);
						if (blockItem.getBlock() == storedBlock) {
							return BlackHoleTalismanItem.getBlockCount(stack);
						}
					}
					return 0;
				}
				
				@Override
				public int provideItems(Player player, ItemStack stack, Item requestedItem, int amount) {
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
			entries.accept(LEAST_BLACK_LOTUS);
			entries.accept(BLACKEST_LOTUS);
		});
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void registerClient() {
	
	}
	
}
