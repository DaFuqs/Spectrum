package de.dafuqs.spectrum.api.interaction;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;

public interface ItemProvider {
	
	int getItemCount(PlayerEntity player, ItemStack stack, Item requestedItem);
	
	int provideItems(PlayerEntity player, ItemStack stack, Item requestedItem, int amount);
	
}
