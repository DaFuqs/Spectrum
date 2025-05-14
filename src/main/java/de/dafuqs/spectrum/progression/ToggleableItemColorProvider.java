package de.dafuqs.spectrum.progression;

import net.minecraft.client.color.item.*;
import net.minecraft.world.item.*;

public class ToggleableItemColorProvider implements ItemColor {
	
	final ItemColor vanillaProvider;
	boolean shouldApply;
	
	public ToggleableItemColorProvider(ItemColor vanillaProvider) {
		this.vanillaProvider = vanillaProvider;
		this.shouldApply = true;
	}
	
	public void setShouldApply(boolean shouldApply) {
		this.shouldApply = shouldApply;
	}
	
	@Override
	public int getColor(ItemStack stack, int tintIndex) {
		if (shouldApply) {
			return vanillaProvider.getColor(stack, tintIndex);
		} else {
			// no tint
			return 16777215;
		}
	}
}
