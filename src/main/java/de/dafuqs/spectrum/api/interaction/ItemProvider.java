package de.dafuqs.spectrum.api.interaction;

import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;

// TODO: migrate to Capabilities.ItemHandler.ITEM
public interface ItemProvider {
	
	int getItemCount(Player player, ItemStack stack, Item requestedItem);
	
	int provideItems(Player player, ItemStack stack, Item requestedItem, int amount);
	
}
