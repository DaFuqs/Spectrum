package de.dafuqs.spectrum.items.tooltip;

import net.minecraft.client.item.*;
import net.minecraft.item.*;

import java.util.*;

public class PresentTooltipData implements TooltipData {
	
	private final List<ItemStack> itemStacks;
	
	public PresentTooltipData(List<ItemStack> itemStacks) {
		this.itemStacks = itemStacks;
	}
	
	public List<ItemStack> getItemStacks() {
		return this.itemStacks;
	}
	
}
