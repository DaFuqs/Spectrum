package de.dafuqs.spectrum.api.item;

import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.item.*;

public interface SplittableItem {
	
	ItemStack getSplitResult(ServerPlayer player, ItemStack parent);
	
	boolean canSplit(ServerPlayer player, InteractionHand activeHand, ItemStack stack);
	
	default void sign(ServerPlayer player, ItemStack stack) {
		stack.set(SpectrumDataComponentTypes.PAIRED_ITEM, new PairedItemComponent(player.level().getGameTime() + player.getUUID().getMostSignificantBits()));
	}
	
	void playSound(ServerPlayer player);
}
