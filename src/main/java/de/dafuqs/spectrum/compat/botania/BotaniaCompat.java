package de.dafuqs.spectrum.compat.botania;

import de.dafuqs.spectrum.api.interaction.*;
import de.dafuqs.spectrum.compat.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import vazkii.botania.common.item.*;

public class BotaniaCompat extends SpectrumIntegrationPacks.ModIntegrationPack {
	
	//public static Item BLACKEST_LOTUS = new BlackestLotusItem(new FabricItemSettings());
	
	@Override
	public void register() {
		//SpectrumItems.register("blackest_lotus", BLACKEST_LOTUS, DyeColor.BLACK);
		
		// registering it late, since Botania might not have been initialized yet
		ServerLifecycleEvents.SERVER_STARTED.register(server -> ItemProviderRegistry.register(BotaniaItems.blackHoleTalisman, new ItemProvider() {
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
		}));
	}
	
	@Override
	public void registerClient() {
	
	}
	
}
