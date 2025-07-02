package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.world.item.*;

public interface MergeableItem {
	
	ItemStack getMergeResult(ServerPlayer player, ItemStack firstHalf, ItemStack secondHalf);
	
	boolean canMerge(ServerPlayer player, ItemStack parent, ItemStack other);
	
	default boolean verify(ItemStack parent, ItemStack other) {
		if (!parent.getEnchantments().equals(other.getEnchantments()))
			return false;
		
		var comp = parent.get(SpectrumDataComponentTypes.PAIRED_ITEM);
		var otherComp = other.get(SpectrumDataComponentTypes.PAIRED_ITEM);
		return comp != null && otherComp != null && comp.signature() == otherComp.signature();
	}
	
	void playSound(ServerPlayer player);
}
