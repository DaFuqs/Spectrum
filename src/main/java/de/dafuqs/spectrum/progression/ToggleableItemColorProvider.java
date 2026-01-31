package de.dafuqs.spectrum.progression;

import net.minecraft.client.color.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.event.*;

public class ToggleableItemColorProvider implements ItemColor {
	
	final RegisterColorHandlersEvent.Item event;
	final ItemStack vanillaStack;
	boolean shouldApply;
	
	public ToggleableItemColorProvider(RegisterColorHandlersEvent.Item event, ItemStack vanillaStack) {
		this.event = event;
		this.vanillaStack = vanillaStack;
		this.shouldApply = true;
	}
	
	public void setShouldApply(boolean shouldApply) {
		this.shouldApply = shouldApply;
	}
	
	@Override
	public int getColor(ItemStack stack, int tintIndex) {
		if (shouldApply) {
			return event.getItemColors().getColor(Blocks.OAK_LEAVES.asItem().getDefaultInstance(), tintIndex);
		} else {
			// no tint
			return 16777215;
		}
	}
}
