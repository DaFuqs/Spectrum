package de.dafuqs.spectrum.api.item;

import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;

public interface SplittableItem {
	
	ItemStack getResult(ServerPlayerEntity player, ItemStack parent);
	
	boolean canSplit(ServerPlayerEntity player, Hand activeHand, ItemStack stack);
	
	default void sign(ServerPlayerEntity player, ItemStack stack) {
		stack.getOrCreateNbt().putLong("pairSignature", player.getWorld().getTime() + player.getUuid().getMostSignificantBits());
	}
	
	void playSound(ServerPlayerEntity player);
}
